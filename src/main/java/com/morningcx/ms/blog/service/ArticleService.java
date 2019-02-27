package com.morningcx.ms.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.morningcx.ms.blog.base.exception.BusinessException;
import com.morningcx.ms.blog.base.util.EntityUtil;
import com.morningcx.ms.blog.base.util.RequestUtil;
import com.morningcx.ms.blog.entity.*;
import com.morningcx.ms.blog.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * @author gcx
 * @date 2019/2/21
 */
@Service
public class ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ContentMapper contentMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Autowired
    private CategoryMapper categoryMapper;


    /**
     * 根据id查询文章信息
     *
     * @param id
     * @return
     */
    public Article getArticleById(Integer id) {
        Article article = articleMapper.getMetaById(id);
        BusinessException.throwIfNull(article, "文章不存在");
        article.setTags(articleTagMapper.listTagsByArticleId(id));
        // 更新浏览次数
        articleMapper.updateViewsById(id);
        article.setContent(contentMapper.selectById(article.getContentId()));
        return article;
    }

    /**
     * 创建新文章
     *
     * @param article
     */
    @Transactional
    public int insertArticle(Article article) {
        // 检测登录
        User user = RequestUtil.getCurrentUser();
        BusinessException.throwIfNull(user, "登录超时");
        // 设置作者id
        article.setAuthorId(user.getId());
        // 插入文章内容，markdown内容开头或结尾可能存在空格，所以不需要进行trim操作
        Content content = article.getContent();
        contentMapper.insert(content);
        // 插入文章，对简介和标题字符串进行修剪
        EntityUtil.trim(article);
        article.setContentId(content.getId());
        article.setCreateTime(new Date());
        article.setViews(0);
        article.setLikes(0);
        article.setRecycle(0);
        article.setDeleted(0);
        articleMapper.insert(article);
        // 插入标签
        ArticleTag articleTag = new ArticleTag();
        articleTag.setArticleId(article.getId());
        Set<String> set = new HashSet<>();
        for (Tag tag : article.getTags()) {
            // 对标签名称进行修剪
            EntityUtil.trim(tag);
            // 标签去重
            if (set.add(tag.getName())) {
                Tag oldTag = tagMapper.selectOne(new QueryWrapper<Tag>().eq("name", tag.getName()));
                // 若原标签不存在，则插入
                if (oldTag == null) {
                    tagMapper.insert(tag);
                    articleTag.setTagId(tag.getId());
                } else {
                    articleTag.setTagId(oldTag.getId());
                }
                articleTagMapper.insert(articleTag);
            }
        }
        return article.getId();
    }


    /*public List<Tag> listTagsByArticleId(Integer id) {
        QueryWrapper<ArticleTag> wrapper = new QueryWrapper<>();
        wrapper.eq("article_id", id);
        List<ArticleTag> articleTags = articleTagMapper.selectList(wrapper);
        List<Integer> tagIds = new ArrayList<>();
        for (ArticleTag articleTag : articleTags) {
            tagIds.add(articleTag.getTagId());
        }
        return tagMapper.selectBatchIds(tagIds);
    }*/
}
