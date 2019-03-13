package com.morningcx.ms.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.morningcx.ms.blog.base.exception.BusinessException;
import com.morningcx.ms.blog.base.util.ContextUtil;
import com.morningcx.ms.blog.base.util.EntityUtil;
import com.morningcx.ms.blog.entity.Article;
import com.morningcx.ms.blog.entity.Category;
import com.morningcx.ms.blog.mapper.ArticleMapper;
import com.morningcx.ms.blog.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author gcx
 * @date 2019/3/8
 */
@Service
public class CategoryService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 根据ID查询分类
     *
     * @param id
     * @return
     */
    public Category getById(Integer id) {
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        wrapper.eq("user_id", ContextUtil.getLoginId());
        Category category = categoryMapper.selectOne(wrapper);
        BusinessException.throwIfNull(category, "分类不存在");
        return category;
    }

    /**
     * 分页查询
     *
     * @param category
     * @param page
     * @param limit
     * @return
     */
    public IPage<Category> listPage(Category category, Integer page, Integer limit) {
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", ContextUtil.getLoginId());
        category.setUserId(null);
        wrapper.setEntity(category);
        return categoryMapper.selectPage(new Page<>(page, limit), wrapper);
    }

    /**
     * 新增分类
     *
     * @param category
     * @return
     */
    @Transactional
    public Integer insert(Category category) {
        if (category.getPid() == null) {
            category.setPid(0);
        }
        // 检测是否存在同名分类
        EntityUtil.trim(category);
        Integer userId = ContextUtil.getLoginId();
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("name", category.getName());
        Category oldCategory = categoryMapper.selectOne(wrapper);
        BusinessException.throwIf(oldCategory != null, "存在同名分类");
        // 插入新分类
        Date now = new Date();
        category.setUserId(userId);
        category.setCreateTime(now);
        category.setUpdateTime(now);
        category.setDeleted(0);
        categoryMapper.insert(category);
        return category.getId();
    }


    /**
     * 获取分类树
     *
     * @return
     */
    public List<Map<String, Object>> getCategoryTree() {
        List<Map<String, Object>> nodes = new ArrayList<>();
        Integer userId = ContextUtil.getLoginId();
        // todo 文章节点
        List<Article> articles = articleMapper.selectList(new QueryWrapper<Article>()
                .eq("recycle", 0)
                .eq("author_id", userId)
                .select("id", "title", "category_id")
        );
        if (articles != null && articles.size() != 0) {
            articles.forEach(article -> {
                Map<String, Object> map = new HashMap<>();
                map.put("articleId", article.getId());
                map.put("name", article.getTitle());
                map.put("pId", article.getCategoryId());
                nodes.add(map);
            });
        }
        // 分类节点
        List<Category> categories = categoryMapper.selectList(
                new QueryWrapper<Category>().eq("user_id", userId));
        if (categories != null && categories.size() != 0) {
            categories.forEach(category -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", category.getId());
                map.put("name", category.getName());
                map.put("pId", category.getPid());
                map.put("isParent", true);
                map.put("open", true);
                nodes.add(map);
            });
        }
        return nodes;
    }
}
