package com.morningcx.ms.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.morningcx.ms.blog.base.exception.BizException;
import com.morningcx.ms.blog.base.util.ContextUtil;
import com.morningcx.ms.blog.entity.Article;
import com.morningcx.ms.blog.entity.Content;
import com.morningcx.ms.blog.mapper.ArticleMapper;
import com.morningcx.ms.blog.mapper.ContentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
     * 根据文章id获取文章内容和标题(编辑常用)
     *
     * @param articleId
     * @return
     */
    public Map<String, Object> getByArticleId(Integer articleId) {
        // 查询当前登录用户未回收、未删除的文章
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.eq("id", articleId);
        // wrapper.eq("recycle", 0); 回收站需要查看内容
        wrapper.eq("author_id", ContextUtil.getLoginId());
        wrapper.select("content_id", "title", "recycle");
        Article article = articleMapper.selectOne(wrapper);
        String notFind = "文章不存在";
        BizException.throwIfNull(article, notFind);
        // 查询文章对应的内容
        Content content = contentMapper.selectById(article.getContentId());
        BizException.throwIfNull(content, notFind);
        // 返回文章标题和文章内容
        Map<String, Object> map = new HashMap<>();
        map.put("title", article.getTitle() + ((article.getRecycle() ==  0) ? "" : "(回收站)"));
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
    @Transactional
    public int updateByArticleId(Integer articleId, String newContent) {
        // 查询当前登录用户未回收、未删除的文章
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.eq("id", articleId);
        // 回收站文章不能修改
        wrapper.eq("recycle", 0);
        wrapper.eq("author_id", ContextUtil.getLoginId());
        wrapper.select("content_id");
        Article article = articleMapper.selectOne(wrapper);
        BizException.throwIfNull(article, "文章不存在");
        // 更新文章修改时间
        Article updateTime = new Article();
        updateTime.setId(articleId);
        updateTime.setUpdateTime(new Date());
        articleMapper.updateById(updateTime);
        // 更新内容
        Content content = new Content();
        content.setId(article.getContentId());
        content.setContent(newContent);
        return contentMapper.updateById(content);
    }
}
