package com.morningcx.ms.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.morningcx.ms.blog.base.exception.BaseException;
import com.morningcx.ms.blog.entity.Tag;
import com.morningcx.ms.blog.mapper.TagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author gcx
 * @date 2019/2/22
 */
@Service
public class TagService {
    @Autowired
    private TagMapper tagMapper;


    public Tag insertTag(Tag tag) {
        BaseException.throwIfNull(tag.getTag(), "标签内容不能为空");
        Tag oldTag = tagMapper.selectOne(new QueryWrapper<Tag>().eq("tag", tag.getTag()));
        if (oldTag == null) {
            tagMapper.insert(tag);
            return tag;
        }
        return oldTag;
    }

    @Transactional
    public Collection<Tag> insertTags(Collection<Tag> tags) {
        // lombok重写了hashcode和equals方法，可以直接添加
        Set<Tag> tagsSet = new HashSet<>(tags);
        tagsSet.forEach(this::insertTag);
        return tagsSet;
    }
}
