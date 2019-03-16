package com.morningcx.ms.blog.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.morningcx.ms.blog.entity.Tag;
import org.apache.ibatis.annotations.Param;

/**
 * @author gcx
 * @date 2019/2/21
 */
public interface TagMapper extends BaseMapper<Tag> {
    /**
     * 查询用户标签以及引用次数
     *
     * @param page
     * @param wrapper
     * @return
     */
    IPage<Tag> listReferencePage(IPage<Tag> page, @Param("ew") Wrapper<Tag> wrapper);
}
