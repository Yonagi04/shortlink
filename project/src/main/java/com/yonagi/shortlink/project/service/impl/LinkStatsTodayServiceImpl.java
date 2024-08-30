package com.yonagi.shortlink.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yonagi.shortlink.project.dao.entity.LinkStatsTodayDO;
import com.yonagi.shortlink.project.dao.mapper.LinkStatsTodayMapper;
import com.yonagi.shortlink.project.service.LinkStatsTodayService;
import org.springframework.stereotype.Service;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 短链接今日统计实现
 * @date 2024/08/30 10:50
 */
@Service
public class LinkStatsTodayServiceImpl extends ServiceImpl<LinkStatsTodayMapper, LinkStatsTodayDO> implements LinkStatsTodayService {
}
