package com.morningcx.ms.blog.service.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.morningcx.ms.blog.entity.Config;
import com.morningcx.ms.blog.mapper.ConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author gcx
 * @date 2019/4/25
 */
@Service
public class ConfigService {

    @Autowired
    private ConfigMapper configMapper;

    /**
     * 通过父级关键字查询该模块的所有配置信息
     *
     * @param parentKeyword
     * @return
     */
    public Map<String, String> getConfigGroupByParentKeyword(String parentKeyword) {
        Object pid = configMapper.selectObjs(new QueryWrapper<Config>().lambda()
                .eq(Config::getKeyword, parentKeyword).select(Config::getId)).get(0);
        List<Config> configs = configMapper.selectList(
                new QueryWrapper<Config>().lambda().eq(Config::getPid, pid));
        return configs.stream().collect(Collectors.toMap(Config::getKeyword, Config::getValue));
    }
}
