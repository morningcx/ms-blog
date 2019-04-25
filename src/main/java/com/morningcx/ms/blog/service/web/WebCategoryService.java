package com.morningcx.ms.blog.service.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.morningcx.ms.blog.base.exception.BizException;
import com.morningcx.ms.blog.entity.Article;
import com.morningcx.ms.blog.entity.Category;
import com.morningcx.ms.blog.mapper.ArticleMapper;
import com.morningcx.ms.blog.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author gcx
 * @date 2019/4/7
 */
@Service
public class WebCategoryService {
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 根据id获取分类
     *
     * @param id
     * @return
     */
    public Category getById(Integer id) {
        Category category = categoryMapper.selectOne(new QueryWrapper<Category>().lambda()
                .eq(Category::getId, id)
                .select(Category::getId, Category::getName, Category::getDescription, Category::getCover));
        BizException.throwIfNull(category, "分类不存在");
        return category;
    }

    /**
     * 列举包含文章的分类(分类下没有可见文章的不显示)
     *
     * @return
     */
    public List<Category> listAll() {
        QueryWrapper<Article> wrapper = new QueryWrapper<Article>()
                .eq("modifier", 0)
                .eq("recycle", 0)
                .select("category_id")
                .groupBy("category_id");
        List<Object> categoryIds = articleMapper.selectObjs(wrapper);
        // todo 引用次数
        if (categoryIds.size() == 0) {
            return new ArrayList<>();
        }
        return categoryMapper.selectList(new QueryWrapper<Category>()
                .in("id", categoryIds)
                .select("id", "name", "description"));
    }

    /**
     * 列举推荐分类
     *
     * @return
     */
    public List<Category> listRecommendCategories() {
        // todo 优化
        List<Category> records = categoryMapper.selectPage(new Page<>(1, 4), new QueryWrapper<Category>()
                .eq("recommend", 1)
                .select("id", "name")
                .orderByDesc("update_time")).getRecords();
        if (records == null || records.size() == 0) {
            return records;
        }
        records.forEach(category -> {
            Set<Object> childNodeIds = getChildNodeIds(Collections.singletonList(category.getId()), new HashSet<>());
            List<Article> articleList = articleMapper.selectPage(new Page<>(1, 9), new QueryWrapper<Article>()
                    .eq("modifier", 0)
                    .eq("recycle", 0)
                    .in("category_id", childNodeIds)
                    .select("id", "title", "create_time")
                    .orderByDesc("create_time")).getRecords();
            category.setArticleList(articleList);
        });
        return records;
    }


    /**
     * 获取子分类id，BFS
     *
     * @param pids
     * @return
     */
    private Set<Object> getChildNodeIds(List<Object> pids, Set<Object> set) {
        if (pids == null || pids.size() == 0) {
            return set;
        }
        for (Object pid : pids) {
            // 避免无限递归
            if (!set.add(pid)) {
                return set;
            }
        }
        List<Object> childIds = categoryMapper.selectObjs(
                new QueryWrapper<Category>().in("pid", pids).select("id"));
        return getChildNodeIds(childIds, set);
    }
}
