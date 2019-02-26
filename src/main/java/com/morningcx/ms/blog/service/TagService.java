package com.morningcx.ms.blog.service;

import com.morningcx.ms.blog.entity.Tag;
import com.morningcx.ms.blog.mapper.ArticleTagMapper;
import com.morningcx.ms.blog.mapper.TagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * @author gcx
 * @date 2019/2/22
 */
@Service
public class TagService {
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Transactional
    public void insertArticleTags(Integer articleId, Collection<Tag> tags) {
        /*ArticleTag articleTag = new ArticleTag();
        articleTag.setArticleId(articleId);
        Set<String> set = new HashSet<>();
        for (Tag tag : tags) {
            // 标签名称消除两边空格
            String tagName = BaseException.throwIfEmpty(tag.getName(), "标签内容不能为空");
            tag.setName(tagName);
            // 消除重复标签
            if (set.add(tagName)) {
                Tag oldTag = tagMapper.selectOne(new QueryWrapper<Tag>().eq("name", tagName));
                // 若原标签不存在，则插入
                if (oldTag == null) {
                    tag.setId(null);
                    tagMapper.insert(tag);
                    articleTag.setTagId(tag.getId());
                } else {
                    articleTag.setTagId(oldTag.getId());
                }
            }
            articleTagMapper.insert(articleTag);
        }*/
    }
}
