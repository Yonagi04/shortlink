package com.yonagi.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yonagi.shortlink.project.common.constant.RedisKeyConstant;
import com.yonagi.shortlink.project.common.convention.exception.ClientException;
import com.yonagi.shortlink.project.common.convention.exception.ServerException;
import com.yonagi.shortlink.project.common.enums.ValidDateTypeEnum;
import com.yonagi.shortlink.project.config.GotoDomainWhiteListConfiguration;
import com.yonagi.shortlink.project.dao.entity.ShortLinkDO;
import com.yonagi.shortlink.project.dao.entity.ShortLinkGotoDO;
import com.yonagi.shortlink.project.dao.mapper.ShortLinkGotoMapper;
import com.yonagi.shortlink.project.dao.mapper.ShortLinkMapper;
import com.yonagi.shortlink.project.dto.biz.ShortLinkStatsRecordDTO;
import com.yonagi.shortlink.project.dto.req.ShortLinkBatchCreateReqDTO;
import com.yonagi.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.yonagi.shortlink.project.dto.req.ShortLinkPageReqDTO;
import com.yonagi.shortlink.project.dto.req.ShortLinkUpdateReqDTO;
import com.yonagi.shortlink.project.dto.resp.*;
import com.yonagi.shortlink.project.mq.producer.ShortLinkStatsSaveProducer;
import com.yonagi.shortlink.project.service.ShortLinkService;
import com.yonagi.shortlink.project.toolkit.HashUtil;
import com.yonagi.shortlink.project.toolkit.LinkUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 短链接服务层实现
 * @date 2024/08/15 15:27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements ShortLinkService {

    private final ShortLinkGotoMapper shortLinkGotoMapper;
    private final ShortLinkStatsSaveProducer shortLinkStatsSaveProducer;
    private final GotoDomainWhiteListConfiguration gotoDomainWhiteListConfiguration;

    private final RBloomFilter<String> shortUriCreateCachePenetrationBloomFilter;
    private final RedissonClient redissonClient;
    private final StringRedisTemplate stringRedisTemplate;

    @Value("${short-link.domain.default}")
    private String defaultDomain;

    @SneakyThrows
    @Override
    public void restoreUrl(String shortUri, HttpServletRequest request, HttpServletResponse response) {
        String serverName = request.getServerName();
        String serverPort = Optional.of(request.getServerPort())
                .filter(each -> !Objects.equals(each, 80))
                .map(String::valueOf)
                .map(each -> ":" + each)
                .orElse("");
        String fullShortUrl = serverName + serverPort + "/" + shortUri;
        String originUrl = stringRedisTemplate.opsForValue()
                .get(String.format(RedisKeyConstant.GOTO_SHORT_LINK_KEY, fullShortUrl));
        if (StrUtil.isNotBlank(originUrl)) {
            ShortLinkStatsRecordDTO statsRecord = buildLinkStatsRecordAndSetUser(fullShortUrl, request, response);
            shortLinkStats(fullShortUrl, null, statsRecord);
            response.sendRedirect(originUrl);
            return;
        }
        if (!shortUriCreateCachePenetrationBloomFilter.contains(fullShortUrl)) {
            response.sendRedirect("/page/404");
            return;
        }
        String gotoIsNullShortLink = stringRedisTemplate.opsForValue().get(String.format(RedisKeyConstant.GOTO_IS_NULL_SHORT_LINK_KEY, fullShortUrl));
        if (StrUtil.isNotBlank(gotoIsNullShortLink)) {
            response.sendRedirect("/page/404");
            return;
        }
        RLock lock = redissonClient.getLock(String.format(RedisKeyConstant.LOCK_GOTO_SHORT_LINK_KEY, fullShortUrl));
        lock.lock();
        try {
            originUrl = stringRedisTemplate.opsForValue()
                    .get(String.format(RedisKeyConstant.GOTO_SHORT_LINK_KEY, fullShortUrl));
            if (StrUtil.isNotBlank(originUrl)) {
                ShortLinkStatsRecordDTO statsRecord = buildLinkStatsRecordAndSetUser(fullShortUrl, request, response);
                shortLinkStats(fullShortUrl, null, statsRecord);
                response.sendRedirect(originUrl);
                return;
            }
            gotoIsNullShortLink = stringRedisTemplate.opsForValue().get((String.format(RedisKeyConstant.GOTO_IS_NULL_SHORT_LINK_KEY, fullShortUrl));
            if (StrUtil.isNotBlank(gotoIsNullShortLink)) {
                response.sendRedirect("/page/404");
                return;
            }
            LambdaQueryWrapper<ShortLinkGotoDO> linkGotoQueryWrapper = Wrappers.lambdaQuery(ShortLinkGotoDO.class)
                    .eq(ShortLinkGotoDO::getFullShortUrl, fullShortUrl);
            ShortLinkGotoDO shortLinkGotoDO = shortLinkGotoMapper.selectOne(linkGotoQueryWrapper);
            if (shortLinkGotoDO == null) {
                stringRedisTemplate.opsForValue()
                        .set(String.format(RedisKeyConstant.GOTO_IS_NULL_SHORT_LINK_KEY, fullShortUrl),
                                "-", 30, TimeUnit.MINUTES);
                response.sendRedirect("/page/404");
                return;
            }
            LambdaQueryWrapper<ShortLinkDO> shortLinkQueryWrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                    .eq(ShortLinkDO::getGid, shortLinkGotoDO.getGid())
                    .eq(ShortLinkDO::getFullShortUrl, fullShortUrl)
                    .eq(ShortLinkDO::getDelFlag, 0)
                    .eq(ShortLinkDO::getEnableStatus, 0);
            ShortLinkDO shortLinkDO = baseMapper.selectOne(shortLinkQueryWrapper);
            if (shortLinkDO == null || (shortLinkDO.getValidDate() != null && shortLinkDO.getValidDate().before(new Date()))) {
                stringRedisTemplate.opsForValue().set(
                        String.format(RedisKeyConstant.GOTO_IS_NULL_SHORT_LINK_KEY, fullShortUrl),
                        "-",
                        30,
                        TimeUnit.MINUTES
                );
                response.sendRedirect("/page/404");
                return;
            }
            stringRedisTemplate.opsForValue().set(
                    String.format(RedisKeyConstant.GOTO_SHORT_LINK_KEY, fullShortUrl),
                    shortLinkDO.getOriginUrl(),
                    LinkUtil.getLinkCacheValidDate(shortLinkDO.getValidDate()),
                    TimeUnit.MILLISECONDS
            );
            ShortLinkStatsRecordDTO statsRecord = buildLinkStatsRecordAndSetUser(fullShortUrl, request, response);
            shortLinkStats(fullShortUrl, shortLinkDO.getGid(), statsRecord);
            response.sendRedirect(shortLinkDO.getOriginUrl());
        } finally {
            lock.unlock();
        }
    }

    private ShortLinkStatsRecordDTO buildLinkStatsRecordAndSetUser(String fullShortUrl, HttpServletRequest request, HttpServletResponse response) {
        AtomicBoolean uvFirstFlag = new AtomicBoolean();
        Cookie[] cookies = request.getCookies();
        AtomicReference<String> uv = new AtomicReference<>();
        Runnable addResponseCookieTask = () -> {
            uv.set(UUID.fastUUID().toString());
            Cookie uvCookie = new Cookie("uv", uv.get());
            uvCookie.setMaxAge(60 * 60 * 24 * 30);
            uvCookie.setPath(StrUtil.sub(fullShortUrl, fullShortUrl.indexOf("/"), fullShortUrl.length()));
            response.addCookie(uvCookie);
            uvFirstFlag.set(Boolean.TRUE);
            stringRedisTemplate.opsForSet().add(RedisKeyConstant.UV_SHORT_LINK_KEY + fullShortUrl, uv.get());
        };
        if (ArrayUtil.isNotEmpty(cookies)) {
            Arrays.stream(cookies)
                    .filter(each -> Objects.equals(each.getName(), "uv"))
                    .findFirst()
                    .map(Cookie::getValue)
                    .ifPresentOrElse(each -> {
                        uv.set(each);
                        Long uvAdded = stringRedisTemplate.opsForSet().add(RedisKeyConstant.UV_SHORT_LINK_KEY + fullShortUrl, each);
                        uvFirstFlag.set(uvAdded != null && uvAdded > 0L);
                    }, addResponseCookieTask);
        } else {
            addResponseCookieTask.run();
        }
        String remoteAddr = LinkUtil.getIp(request);
        String os = LinkUtil.getOs(request);
        String browser = LinkUtil.getBrowser(request);
        String device = LinkUtil.getDevice(request);
        String network = LinkUtil.getNetwork(request);
        Long uipAdded = stringRedisTemplate.opsForSet().add(RedisKeyConstant.UIP_SHORT_LINK_KEY + fullShortUrl, remoteAddr);
        boolean uipFirstFlag = uipAdded != null && uipAdded > 0L;
        return ShortLinkStatsRecordDTO.builder()
                .fullShortUrl(fullShortUrl)
                .uv(uv.get())
                .uvFirstFlag(uvFirstFlag.get())
                .uipFirstFlag(uipFirstFlag)
                .remoteAddr(remoteAddr)
                .os(os)
                .browser(browser)
                .device(device)
                .network(network)
                .build();
    }

    @Override
    public void shortLinkStats(String fullShortUrl, String gid, ShortLinkStatsRecordDTO statsRecord) {
        Map<String, String> producerMap = new HashMap<>();
        producerMap.put("fullShortUrl", fullShortUrl);
        producerMap.put("gid", gid);
        producerMap.put("statsRecord", JSON.toJSONString(statsRecord));
        shortLinkStatsSaveProducer.send(producerMap);
    }

    @Override
    public ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO requestParam) {
        verificationWhiteList(requestParam.getOriginUrl());
        String shortLinkSuffix = generateSuffix(requestParam);
        String fullShortUrl = defaultDomain + "/" + shortLinkSuffix;
        ShortLinkDO shortLinkDO = ShortLinkDO.builder()
                .domain(defaultDomain)
                .originUrl(requestParam.getOriginUrl())
                .gid(requestParam.getGid())
                .createType(requestParam.getCreateType())
                .validDateType(requestParam.getValidDateType())
                .validDate(requestParam.getValidDate())
                .describe(requestParam.getDescribe())
                .favicon(getFavicon(requestParam.getOriginUrl()))
                .shortUri(shortLinkSuffix)
                .fullShortUrl(fullShortUrl)
                .enableStatus(0)
                .delTime(0L)
                .totalPv(0)
                .totalUv(0)
                .totalUip(0)
                .build();
        ShortLinkGotoDO shortLinkGotoDO = ShortLinkGotoDO.builder()
                .fullShortUrl(fullShortUrl)
                .gid(requestParam.getGid())
                .build();
        try {
            baseMapper.insert(shortLinkDO);
            shortLinkGotoMapper.insert(shortLinkGotoDO);
        } catch (DuplicateKeyException e) {
            /*
             *  短链接不可能重复
             *  首先我们在构造短链接的时候，在原URL后面追加了一个当前毫秒数
             *  因此，对于不同时刻走到构造短链接这一行的多个线程，创建出来的短链接也应该是不同的，哪怕多个线程携带的原URL是相同的
             *  即使相同时刻走到了构造短链接这一行，创建了同一个短链接，后面也可以通过唯一索引来阻止相同短链接的重复插入
             *  因此我们没必要在出现duplicateKeyException之后，再做查询操作，这样只会增加数据库压力
             *  那么接下来需要做的一个工作就是，对于已经创建过短链接的原URL，我们希望不要重复创建，因为多余的短链接会占用布隆过滤器空间
             */
            log.error("短链接: {}重复入库", fullShortUrl);
            throw new ServerException("短链接重复生成，请稍后再试");
        }
        stringRedisTemplate.opsForValue().set(
                String.format(RedisKeyConstant.GOTO_SHORT_LINK_KEY, fullShortUrl),
                requestParam.getOriginUrl(),
                LinkUtil.getLinkCacheValidDate(requestParam.getValidDate()),
                TimeUnit.MILLISECONDS
        );
        shortUriCreateCachePenetrationBloomFilter.add(fullShortUrl);
        return ShortLinkCreateRespDTO.builder()
                .fullShortUrl("http://" + shortLinkDO.getFullShortUrl())
                .originUrl(requestParam.getOriginUrl())
                .gid(requestParam.getGid())
                .build();
    }

    private void verificationWhiteList(String originUrl) {
        Boolean enable = gotoDomainWhiteListConfiguration.getEnable();
        if (enable == null || !enable) {
            return;
        }
        String domain = LinkUtil.extractDomain(originUrl);
        if (StrUtil.isBlank(domain)) {
            throw new ClientException("跳转链接填写有误");
        }
        if (!gotoDomainWhiteListConfiguration.getDetails().contains(domain)) {
            throw new ClientException("系统禁止跳转该网站，可跳转以下网站: " + gotoDomainWhiteListConfiguration.getNames());
        }
    }

    @Override
    public ShortLinkBatchCreateRespDTO batchCreateShortLink(ShortLinkBatchCreateReqDTO requestParam) {
        List<String> originalUrls = requestParam.getOriginalUrls();
        List<String> describes = requestParam.getDescribes();
        List<ShortLinkBaseInfoRespDTO> result = new ArrayList<>();
        for (int i = 0; i < originalUrls.size(); i++) {
            ShortLinkCreateReqDTO shortLinkCreateReqDTO = BeanUtil.toBean(requestParam, ShortLinkCreateReqDTO.class);
            shortLinkCreateReqDTO.setOriginUrl(originalUrls.get(i));
            shortLinkCreateReqDTO.setDescribe(describes.get(i));
            try {
                ShortLinkCreateRespDTO shortLinkCreateRespDTO = createShortLink(shortLinkCreateReqDTO);
                ShortLinkBaseInfoRespDTO shortLinkBaseInfoRespDTO = ShortLinkBaseInfoRespDTO.builder()
                        .fullShortUrl(shortLinkCreateRespDTO.getFullShortUrl())
                        .describe(shortLinkCreateReqDTO.getDescribe())
                        .originalUrl(shortLinkCreateRespDTO.getOriginUrl())
                        .build();
                result.add(shortLinkBaseInfoRespDTO);
            } catch (Exception e) {
                log.error("批量创建短链接失败，原始参数:{}", originalUrls.get(i));
                throw new ServerException("批量创建短链接失败");
            }
        }
        return ShortLinkBatchCreateRespDTO.builder()
                .total(result.size())
                .baseListInfos(result)
                .build();
    }

    private String generateSuffix(ShortLinkCreateReqDTO requestParam) {
        int customGenerateCount = 0;
        String shortUri;
        while (true) {
            if (customGenerateCount > 10) {
                throw new ServerException("短链接生成超时，请稍后再试");
            }
            String originUrl = requestParam.getOriginUrl();
            originUrl += System.currentTimeMillis();
            shortUri = HashUtil.hashToBase62(originUrl);
            if (!shortUriCreateCachePenetrationBloomFilter.contains(defaultDomain + "/" + shortUri)) {
                break;
            }
            customGenerateCount++;
        }
        return shortUri;
    }

    @Override
    public IPage<ShortLinkPageRespDTO> pageShortLink(ShortLinkPageReqDTO requestParam) {
        IPage<ShortLinkDO> resultPage = baseMapper.pageLink(requestParam);
        return resultPage.convert(each -> {
            ShortLinkPageRespDTO result = BeanUtil.toBean(each, ShortLinkPageRespDTO.class);
            result.setDomain("http://" + result.getDomain());
            return result;
        });
    }

    @Override
    public List<ShortLinkCountQueryRespDTO> listGroupShortLinkCount(List<String> requestParam) {
        QueryWrapper<ShortLinkDO> queryWrapper = Wrappers.query(new ShortLinkDO())
                .select("gid as gid, count(*) as shortLinkCount")
                .in("gid", requestParam)
                .eq("enable_status", 0)
                .eq("del_flag", 0)
                .eq("del_time", 0L)
                .groupBy("gid");
        List<Map<String, Object>> shortLinkDOList = baseMapper.selectMaps(queryWrapper);
        return BeanUtil.copyToList(shortLinkDOList, ShortLinkCountQueryRespDTO.class);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateShortLink(ShortLinkUpdateReqDTO requestParam) {
        verificationWhiteList(requestParam.getOriginUrl());
        LambdaQueryWrapper<ShortLinkDO> queryWrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                .eq(ShortLinkDO::getGid, requestParam.getOriginGid())
                .eq(ShortLinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                .eq(ShortLinkDO::getDelFlag, 0)
                .eq(ShortLinkDO::getEnableStatus, 0);
        ShortLinkDO hasShortLinkDO = baseMapper.selectOne(queryWrapper);
        if (hasShortLinkDO == null) {
            throw new ClientException("短链接记录不存在");
        }
        // 如果gid没有变动，也就是分组不变，就在原来的基础上update
        if (Objects.equals(hasShortLinkDO.getGid(), requestParam.getGid())) {
            LambdaUpdateWrapper<ShortLinkDO> updateWrapper = Wrappers.lambdaUpdate(ShortLinkDO.class)
                    .eq(ShortLinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                    .eq(ShortLinkDO::getGid, requestParam.getGid())
                    .eq(ShortLinkDO::getDelFlag, 0)
                    .eq(ShortLinkDO::getEnableStatus, 0)
                    .set(Objects.equals(requestParam.getValidDateType(), ValidDateTypeEnum.PERMANENT.getType()), ShortLinkDO::getValidDate, null);
            ShortLinkDO shortLinkDO = ShortLinkDO.builder()
                    .domain(hasShortLinkDO.getDomain())
                    .shortUri(hasShortLinkDO.getShortUri())
                    .favicon(hasShortLinkDO.getFavicon())
                    .createType(hasShortLinkDO.getCreateType())
                    .originUrl(requestParam.getOriginUrl())
                    .validDateType(requestParam.getValidDateType())
                    .validDate(requestParam.getValidDate())
                    .describe(requestParam.getDescribe())
                    .gid(requestParam.getGid())
                    .build();
            baseMapper.update(shortLinkDO, updateWrapper);
        } else {
            // 如果gid发生改变，也就是分组改变了，那就先删除后添加
            RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(String.format(RedisKeyConstant.LOCK_GID_UPDATE_KEY, requestParam.getFullShortUrl()));
            RLock rLock = readWriteLock.writeLock();
            if (!rLock.tryLock()) {
                throw new ServerException("短链接正在被访问，请稍后再试");
            }
            try {
                LambdaUpdateWrapper<ShortLinkDO> updateWrapper = Wrappers.lambdaUpdate(ShortLinkDO.class)
                        .eq(ShortLinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                        .eq(ShortLinkDO::getGid, requestParam.getGid())
                        .eq(ShortLinkDO::getDelFlag, 0)
                        .eq(ShortLinkDO::getDelTime, 0L)
                        .eq(ShortLinkDO::getEnableStatus, 0);
                ShortLinkDO delShortLinkDO = ShortLinkDO.builder()
                        .delTime(System.currentTimeMillis())
                        .build();
                delShortLinkDO.setDelFlag(1);
                baseMapper.update(delShortLinkDO, updateWrapper);
                ShortLinkDO shortLinkDO = ShortLinkDO.builder()
                        .domain(defaultDomain)
                        .originUrl(requestParam.getOriginUrl())
                        .gid(requestParam.getGid())
                        .createType(hasShortLinkDO.getCreateType())
                        .validDateType(requestParam.getValidDateType())
                        .validDate(requestParam.getValidDate())
                        .describe(requestParam.getDescribe())
                        .shortUri(hasShortLinkDO.getShortUri())
                        .enableStatus(hasShortLinkDO.getEnableStatus())
                        .totalPv(hasShortLinkDO.getTotalPv())
                        .totalUv(hasShortLinkDO.getTotalUv())
                        .totalUip(hasShortLinkDO.getTotalUip())
                        .fullShortUrl(hasShortLinkDO.getFullShortUrl())
                        .favicon(getFavicon(requestParam.getOriginUrl()))
                        .delTime(0L)
                        .build();
                baseMapper.insert(shortLinkDO);
                LambdaQueryWrapper<ShortLinkGotoDO> linkGotoQueryWrapper = Wrappers.lambdaQuery(ShortLinkGotoDO.class)
                        .eq(ShortLinkGotoDO::getFullShortUrl, requestParam.getFullShortUrl())
                        .eq(ShortLinkGotoDO::getGid, hasShortLinkDO.getGid());
                ShortLinkGotoDO shortLinkGotoDO = shortLinkGotoMapper.selectOne(linkGotoQueryWrapper);
                shortLinkGotoMapper.deleteById(linkGotoQueryWrapper);
                shortLinkGotoDO.setGid(requestParam.getGid());
                shortLinkGotoMapper.insert(shortLinkGotoDO);
            } finally {
                rLock.unlock();
            }
        }
        if (!Objects.equals(hasShortLinkDO.getValidDateType(), requestParam.getValidDateType())
                || !Objects.equals(hasShortLinkDO.getValidDate(), requestParam.getValidDate())
                || !Objects.equals(hasShortLinkDO.getOriginUrl(), requestParam.getOriginUrl())) {
            stringRedisTemplate.delete(String.format(RedisKeyConstant.GOTO_SHORT_LINK_KEY, requestParam.getFullShortUrl()));
            if (hasShortLinkDO.getValidDate() != null && hasShortLinkDO.getValidDate().before(new Date())) {
                if (Objects.equals(requestParam.getValidDateType(), ValidDateTypeEnum.PERMANENT.getType()) || requestParam.getValidDate().after(new Date())) {
                    stringRedisTemplate.delete(String.format(RedisKeyConstant.GOTO_IS_NULL_SHORT_LINK_KEY, requestParam.getFullShortUrl()));
                }
            }
        }
    }

    @SneakyThrows
    private String getFavicon(String url) {
        if (!url.startsWith("https://") && !url.startsWith("http://")) {
            url = "https://" + url;
        }
        URL targetUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) targetUrl.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            Document document = Jsoup.connect(url).get();
            Element faviconLink = document.select("link[rel~=(?i)^(shortcut )?icon]").first();
            if (faviconLink != null) {
                return faviconLink.attr("abs:href");
            }
        }
        return null;
    }
}
