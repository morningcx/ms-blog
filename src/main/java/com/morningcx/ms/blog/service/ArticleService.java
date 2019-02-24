package com.morningcx.ms.blog.service;

import com.morningcx.ms.blog.base.exception.BaseException;
import com.morningcx.ms.blog.entity.Article;
import com.morningcx.ms.blog.entity.Category;
import com.morningcx.ms.blog.entity.Tag;
import com.morningcx.ms.blog.entity.User;
import com.morningcx.ms.blog.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


/**
 * @author guochenxiao
 * @date 2019/2/21
 */
@Service
public class ArticleService {

    @Autowired
    private UserService userService;


    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ContentMapper contentMapper;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 根据id查询文章信息
     *
     * @param id
     * @param loadTags    是否加载标签
     * @param loadContent 是否加载文章内容
     * @return
     */
    public Article getArticleById(Integer id, boolean loadTags, boolean loadContent) {
        Article article = articleMapper.selectById(id);
        // id和昵称
        User author = userService.getUserById(article.getAuthorId(), false);
        Category category = categoryMapper.selectById(article.getCategoryId());
        article.setAuthor(author);
        article.setCategory(category);
        if (loadTags) {
            List<Tag> tags = articleTagMapper.listTagsByArticleId(id);
            article.setTags(tags);
        }
        if (loadContent) {
            // 更新浏览次数
            articleMapper.updateViewsById(id);
            article.setContent(contentMapper.selectById(article.getContentId()));
        }
        return article;
    }

    @Transactional
    public int insertArticle(Article article) {
        // todo 判断用户登录
        BaseException.throwIfNull(article.getTitle(), "文章标题不能为空");
        // todo introduction再说 content也再说
        BaseException.throwIfNull(article.getContent().getContent(), "文章内容不能为空");
        BaseException.throwIfNull(article.getCategoryId(), "文章分类不能为空");
        BaseException.throwIfNull(article.getType(), "文章类型不能为空");
        BaseException.throwIfNull(article.getState(), "文章状态不能为空");
        BaseException.throwIfNull(article.getModifier(), "文章修饰符不能为空");
        List<Tag> tags = article.getTags();
        BaseException.throwIf(tags == null || tags.size() == 0, "文章标签不能为空");
        BaseException.throwIf(tags.size() > 5, "文章最多5个标签");
        // 插入内容
        contentMapper.insert(article.getContent());
        // 插入文章
        article.setCategoryId(article.getContent().getId());
        article.setCreateTime(new Date());
        article.setDeleted(0);
        articleMapper.insert(article);
        // 插入标签
        return 0;
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
