package com.morningcx.ms.blog.web.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.morningcx.ms.blog.base.exception.BizException;
import com.morningcx.ms.blog.entity.*;
import com.morningcx.ms.blog.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author gcx
 * @date 2019/3/27
 */
@Service
public class WebArticleService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ContentMapper contentMapper;

    /**
     * 热门文章列表
     *
     * @param userId
     * @return
     */
    public List<Article> listHotArticles(Integer userId) {
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.eq("author_id", userId);
        wrapper.eq("modifier", 0);
        wrapper.eq("recycle", 0);
        wrapper.orderByDesc("views");
        wrapper.select("id", "title", "create_time", "category_id", "views");
        List<Article> articles = articleMapper.selectPage(new Page<>(1, 5), wrapper).getRecords();
        /*List<Integer> categoryIds = articles.stream().map(Article::getCategoryId).collect(Collectors.toList());
        List<Category> categories = categoryMapper.selectList(
                new QueryWrapper<Category>().in("id", categoryIds).select("name"));*/

        return articles;
    }

    /**
     * 获取文章全部信息
     *
     * @param id
     * @return
     */
    public Article getFullById(Integer id) {
        Article article = articleMapper.selectOne(new QueryWrapper<Article>()
                .eq("id", id)
                .eq("modifier", 0)
                .eq("recycle", 0));
        BizException.throwIfNull(article, "文章不存在");
        // 作者
        User user = userMapper.selectOne(new QueryWrapper<User>()
                .eq("id", article.getAuthorId())
                .select("name"));
        article.setAuthor(user == null ? "" : user.getName());
        // 分类
        Category category = categoryMapper.selectOne(new QueryWrapper<Category>()
                .eq("id", article.getCategoryId())
                .select("name"));
        article.setCategory(category == null ? "" : category.getName());
        // 标签
        List<Object> tagIds = articleTagMapper.selectObjs(new QueryWrapper<ArticleTag>()
                .eq("article_id", article.getId())
                .select("tag_id"));
        List<Tag> tags = tagMapper.selectList(new QueryWrapper<Tag>()
                .in("id", tagIds)
                .select("name"));
        article.setTagNames(tags.stream().map(Tag::getName).collect(Collectors.toList()));
        // 内容
        Content content = contentMapper.selectById(article.getContentId());
        article.setContent(content);
        return article;
    }

}
