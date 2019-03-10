package com.morningcx.ms.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.morningcx.ms.blog.base.exception.BusinessException;
import com.morningcx.ms.blog.base.util.ContextUtil;
import com.morningcx.ms.blog.entity.Article;
import com.morningcx.ms.blog.entity.Content;
import com.morningcx.ms.blog.mapper.ArticleMapper;
import com.morningcx.ms.blog.mapper.ContentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gcx
 * @date 2019/3/10
 */
@Service
public class ContentService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ContentMapper contentMapper;

    /**
     * 根据文章id获取文章内容和标题(管理端编辑常用)
     *
     * @param articleId
     * @return
     */
    public Map<String, Object> getByArticleId(Integer articleId) {
        // 查询当前登录用户未回收、未删除的文章
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.eq("id", articleId);
        wrapper.eq("recycle", 0);
        wrapper.eq("author_id", ContextUtil.getLoginId());
        wrapper.select("content_id", "title");
        Article article = articleMapper.selectOne(wrapper);
        String notFind = "文章不存在";
        BusinessException.throwIfNull(article, notFind);
        // 查询文章对应的内容
        Content content = contentMapper.selectById(article.getContentId());
        BusinessException.throwIfNull(content, notFind);
        Map<String, Object> map = new HashMap<>();
        map.put("title", article.getTitle());
        map.put("content", content);
        return map;
    }

    /**
     * 根据文章id更新内容
     *
     * @param articleId
     * @param newContent
     * @return
     */
    public int updateByArticleId(Integer articleId, String newContent) {
        // 查询当前登录用户未回收、未删除的文章
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.eq("id", articleId);
        wrapper.eq("recycle", 0);
        wrapper.eq("author_id", ContextUtil.getLoginId());
        wrapper.select("content_id");
        Article article = articleMapper.selectOne(wrapper);
        BusinessException.throwIfNull(article, "文章不存在");
        // 更新内容
        Content content = new Content();
        content.setId(article.getContentId());
        content.setContent(newContent);
        return contentMapper.updateById(content);
    }
}
