package com.morningcx.ms.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.morningcx.ms.blog.base.util.ContextUtil;
import com.morningcx.ms.blog.entity.Tag;
import com.morningcx.ms.blog.mapper.TagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author gcx
 * @date 2019/2/22
 */
@Service
public class TagService {
    @Autowired
    private TagMapper tagMapper;

    /**
     * @param tag
     * @param page
     * @param limit
     * @return
     */
    public IPage<Tag> listReferencePage(Tag tag, Integer page, Integer limit) {
        // mp不支持自定义wrapper的entity条件
        // 这里需要手动判断是否为null，再进行条件添加
        QueryWrapper<Tag> wrapper = new QueryWrapper<>();
        wrapper.like(!StringUtils.isEmpty(tag.getName()), "name", tag.getName());
        wrapper.like(!StringUtils.isEmpty(tag.getDescription()), "description", tag.getDescription());
        wrapper.eq("recycle", 0);
        wrapper.eq("deleted", 0);
        wrapper.eq("author_id", ContextUtil.getLoginId());
        return tagMapper.listReferencePage(new Page<>(page, limit), wrapper);
    }

    /**
     * 列举出所有的标签(元信息编辑标签提示)
     *
     * @return
     */
    public List<String> listAllTagsName() {
        // todo 好像太多了。。
        return tagMapper.selectList(null).stream().map(Tag::getName).collect(Collectors.toList());
    }

}
