package com.morningcx.ms.blog.service.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.morningcx.ms.blog.base.util.EntityUtil;
import com.morningcx.ms.blog.entity.OperationLog;
import com.morningcx.ms.blog.mapper.OperationLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author gcx
 * @date 2019/4/8
 */
@Service
public class OperationLogService {
    @Autowired
    private OperationLogMapper operationLogMapper;

    public IPage<OperationLog> listPage(OperationLog operationLog, Integer page, Integer limit) {
        return operationLogMapper.selectPage(new Page<>(page, limit), new QueryWrapper<OperationLog>()
                /*.eq("user_id", ContextUtil.getLoginId())*/
                .setEntity(EntityUtil.removeEmptyString(operationLog))
                .orderByDesc("time")
                /*.select("ip", "module", "type", "content", "time",
                        "country", "province", "city", "isp", "browser", "os", "device")*/);
    }
}
