package com.morningcx.ms.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.morningcx.ms.blog.base.exception.BusinessException;
import com.morningcx.ms.blog.base.util.EntityUtil;
import com.morningcx.ms.blog.base.util.RequestUtil;
import com.morningcx.ms.blog.entity.Category;
import com.morningcx.ms.blog.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author gcx
 * @date 2019/3/8
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

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
        wrapper.eq("user_id", RequestUtil.getLoginId());
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
        // 检测同一目录下是否存在同名文件
        EntityUtil.trim(category);
        Integer userId = RequestUtil.getLoginId();
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("pid", category.getPid());
        wrapper.eq("name", category.getName());
        Category oldCategory = categoryMapper.selectOne(wrapper);
        BusinessException.throwIf(oldCategory != null, "目录下存在同名文件");
        // 插入新分类
        Date now = new Date();
        category.setUserId(userId);
        category.setCreateTime(now);
        category.setUpdateTime(now);
        category.setDeleted(0);
        categoryMapper.insert(category);
        return category.getId();
    }

}
