package com.morningcx.ms.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.morningcx.ms.blog.entity.Config;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author gcx
 * @date 2019/4/25
 */
public interface ConfigMapper extends BaseMapper<Config> {
    /**
     * 通过keyword获取配置值
     *
     * @param keyword
     * @return
     */
    @Select("select value from t_config where keyword = #{keyword}")
    String getValue(String keyword);

    /**
     * 根据keyword更新配置信息
     *
     * @param keyword
     * @param value
     * @return
     */
    @Update("UPDATE t_config SET value=#{value},update_time=now() WHERE keyword=#{keyword} ")
    int updateValue(@Param("keyword") String keyword, @Param("value") String value);
}
