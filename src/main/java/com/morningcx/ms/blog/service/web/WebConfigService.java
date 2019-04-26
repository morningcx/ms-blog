package com.morningcx.ms.blog.service.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.morningcx.ms.blog.entity.Config;
import com.morningcx.ms.blog.mapper.ConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author gcx
 * @date 2019/4/26
 */
@Service
public class WebConfigService {
    @Autowired
    private ConfigMapper configMapper;

    /**
     * 获取页面信息
     *
     * @return
     */
    public Map<String, String> getWebsiteConfig() {
        return configMapper.selectList(new QueryWrapper<Config>().lambda()
                .inSql(Config::getPid, "select id from t_config where keyword='website'")
                .select(Config::getKeyword, Config::getValue))
                .stream().collect(Collectors.toMap(Config::getKeyword, Config::getValue));
    }
}
