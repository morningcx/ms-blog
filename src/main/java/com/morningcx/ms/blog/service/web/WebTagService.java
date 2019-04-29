package com.morningcx.ms.blog.service.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.morningcx.ms.blog.base.exception.BizException;
import com.morningcx.ms.blog.entity.Tag;
import com.morningcx.ms.blog.mapper.TagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author gcx
 * @date 2019/4/18
 */
@Service
public class WebTagService {
    @Autowired
    private TagMapper tagMapper;

    /**
     * 根据id查询标签
     *
     * @param id
     * @return
     */
    public Tag getById(Integer id) {
        Tag tag = tagMapper.selectById(id);
        BizException.throwIfNull(tag, "标签" + id + "不存在");
        return tag;
    }

    /**
     * 列举所有标签(包括没有被引用的)
     *
     * @return
     */
    public List<Tag> listAll() {
        return tagMapper.selectList(
                new QueryWrapper<Tag>().lambda().select(Tag::getId, Tag::getName));
    }
}
