package com.morningcx.ms.blog.service;

import com.morningcx.ms.blog.entity.ArticleTag;
import com.morningcx.ms.blog.entity.Tag;
import com.morningcx.ms.blog.mapper.ArticleTagMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

/**
 * @author gcx
 * @date 2019/2/22
 */
public class ArticleTagService {

    @Autowired
    private TagService tagService;

    @Autowired
    private ArticleTagMapper articleTagMapper;

    public void insertArticleTags(Integer articleId, Collection<Tag> tags) {
        Collection<Tag> dbTags = tagService.insertTags(tags);
        ArticleTag articleTag = new ArticleTag();
        articleTag.setArticleId(articleId);
        dbTags.forEach(tag -> {
            articleTag.setTagId(tag.getId());
            articleTagMapper.insert(articleTag);
        });

    }
}
