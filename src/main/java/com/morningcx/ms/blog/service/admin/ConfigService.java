package com.morningcx.ms.blog.service.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.morningcx.ms.blog.entity.Config;
import com.morningcx.ms.blog.mapper.ConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author gcx
 * @date 2019/4/25
 */
@Service
public class ConfigService {

    @Autowired
    private ConfigMapper configMapper;

    /**
     * 获取所有配置组信息
     *
     * @return
     */
    public List<Config> listConfigParent() {
        return configMapper.selectList(new QueryWrapper<Config>().lambda()
                .eq(Config::getPid, 0)
                .select(Config::getKeyword, Config::getName, Config::getId));
    }

    /**
     * 通过父级关键字查询该模块的所有配置信息
     *
     * @param parentKeyword
     * @return
     */
    public List<Config> listConfigGroupByParentKeyword(String parentKeyword) {
        return configMapper.selectList(new QueryWrapper<Config>().lambda()
                .inSql(Config::getPid, "select id from t_config where keyword='" + parentKeyword + "'"));
    }

    /**
     * 根据keyword修改value
     *
     * @param configMap
     * @return
     */
    public int updateConfigGroupByKeyword(Map<String, String> configMap) {
        configMap.forEach((k, v) -> configMapper.updateValue(k, v));
        return configMap.size();
    }

}
