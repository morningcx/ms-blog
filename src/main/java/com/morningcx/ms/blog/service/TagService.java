package com.morningcx.ms.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.morningcx.ms.blog.base.util.ContextUtil;
import com.morningcx.ms.blog.entity.Tag;
import com.morningcx.ms.blog.mapper.TagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author gcx
 * @date 2019/2/22
 */
@Service
public class TagService {
    @Autowired
    private TagMapper tagMapper;

    /**
     *
     * @param tag
     * @param page
     * @param limit
     * @return
     */
    public IPage<Tag> listReferencePage(Tag tag, Integer page, Integer limit) {
        // mp不支持自定义wrapper的entity条件
        // 这里需要手动判断是否为null，再进行条件添加
        QueryWrapper<Tag> wrapper = new QueryWrapper<>();
        wrapper.like(tag.getName() != null, "name", tag.getName());
        wrapper.eq("recycle", 0);
        wrapper.eq("deleted", 0);
        wrapper.eq("author_id", ContextUtil.getLoginId());
        return tagMapper.listReferencePage(new Page<>(page, limit), wrapper);
    }


}
