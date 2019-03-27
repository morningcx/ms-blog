package com.morningcx.ms.blog.web.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.morningcx.ms.blog.entity.Article;
import com.morningcx.ms.blog.mapper.ArticleMapper;
import com.morningcx.ms.blog.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author gcx
 * @date 2019/3/27
 */
@Service
public class WebArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CategoryMapper categoryMapper;

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
}
