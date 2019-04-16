package com.morningcx.ms.blog.service.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.morningcx.ms.blog.base.exception.BizException;
import com.morningcx.ms.blog.entity.Article;
import com.morningcx.ms.blog.entity.Category;
import com.morningcx.ms.blog.mapper.ArticleMapper;
import com.morningcx.ms.blog.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        Category category = categoryMapper.selectById(id);
        BizException.throwIfNull(category, "分类不存在");
        return category;
    }

    /**
     * 列举包含文章的分类(分类下没有可见文章的不显示)
     *
     * @param userId
     * @return
     */
    public List<Category> listAll(Integer userId) {
        QueryWrapper<Article> wrapper = new QueryWrapper<Article>()
                .eq("modifier", 0)
                .eq("recycle", 0)
                .eq("author_id", userId)
                .select("category_id")
                .groupBy("category_id");
        List<Object> categoryIds = articleMapper.selectObjs(wrapper);
        // todo 引用次数
        if (categoryIds.size() == 0) {
            return new ArrayList<>();
        }
        return categoryMapper.selectList(new QueryWrapper<Category>()
                .eq("user_id", userId)
                .in("id", categoryIds)
                .select("id", "name", "description"));
    }
}
