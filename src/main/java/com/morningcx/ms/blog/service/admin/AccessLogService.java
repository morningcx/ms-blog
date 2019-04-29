package com.morningcx.ms.blog.service.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.morningcx.ms.blog.base.util.EntityUtil;
import com.morningcx.ms.blog.entity.AccessLog;
import com.morningcx.ms.blog.mapper.AccessLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author gcx
 * @date 2019/4/29
 */
@Service
public class AccessLogService {
    @Autowired
    private AccessLogMapper accessLogMapper;

    public IPage<AccessLog> listPage(AccessLog accessLog, Integer page, Integer limit) {
        return accessLogMapper.selectPage(new Page<>(page, limit), new QueryWrapper<AccessLog>()
                .setEntity(EntityUtil.removeEmptyString(accessLog))
                .orderByDesc("time"));
    }
}
