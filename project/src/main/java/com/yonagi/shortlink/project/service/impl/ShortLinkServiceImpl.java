package com.yonagi.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yonagi.shortlink.project.common.convention.exception.ServerException;
import com.yonagi.shortlink.project.dao.entity.ShortLinkDO;
import com.yonagi.shortlink.project.dao.mapper.ShortLinkMapper;
import com.yonagi.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.yonagi.shortlink.project.dto.req.ShortLinkPageReqDTO;
import com.yonagi.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.yonagi.shortlink.project.dto.resp.ShortLinkPageRespDTO;
import com.yonagi.shortlink.project.service.ShortLinkService;
import com.yonagi.shortlink.project.toolkit.HashUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

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

    private final RBloomFilter<String> shortUriCreateCachePenetrationBloomFilter;

    @Override
    public ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO requestParam) {
        // TODO 对于已经创建过短链接的URL，我们希望不要重复创建
        String shortLinkSuffix = generateSuffix(requestParam);
        String fullShortUrl = requestParam.getDomain() + "/" + shortLinkSuffix;
        ShortLinkDO shortLinkDO = ShortLinkDO.builder()
                .domain(requestParam.getDomain())
                .originUrl(requestParam.getOriginUrl())
                .gid(requestParam.getGid())
                .createType(requestParam.getCreateType())
                .validDateType(requestParam.getValidDateType())
                .validDate(requestParam.getValidDate())
                .describe(requestParam.getDescribe())
                .shortUri(shortLinkSuffix)
                .fullShortUrl(fullShortUrl)
                .enableStatus(0)
                .build();
        try {
            baseMapper.insert(shortLinkDO);
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
        shortUriCreateCachePenetrationBloomFilter.add(shortLinkSuffix);
        return ShortLinkCreateRespDTO.builder()
                .fullShortUrl(shortLinkDO.getFullShortUrl())
                .originUrl(requestParam.getOriginUrl())
                .gid(requestParam.getGid())
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
            if (!shortUriCreateCachePenetrationBloomFilter.contains(requestParam.getDomain() + "/" + shortUri)) {
                break;
            }
            customGenerateCount++;
        }
        return shortUri;
    }

    @Override
    public IPage<ShortLinkPageRespDTO> pageShortLink(ShortLinkPageReqDTO requestParam) {
        LambdaQueryWrapper<ShortLinkDO> queryWrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                .eq(ShortLinkDO::getGid, requestParam.getGid())
                .eq(ShortLinkDO::getEnableStatus, 0)
                .eq(ShortLinkDO::getDelFlag, 0)
                .orderByDesc(ShortLinkDO::getCreateTime);
        IPage<ShortLinkDO> resultPage = baseMapper.selectPage(requestParam, queryWrapper);
        return resultPage.convert(each -> BeanUtil.toBean(each, ShortLinkPageRespDTO.class));
    }
}
