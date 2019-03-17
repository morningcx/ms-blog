package com.morningcx.ms.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.morningcx.ms.blog.base.exception.BizException;
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
        BizException.throwIfNull(category, "分类不存在");
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
        wrapper.setEntity(EntityUtil.removeEmptyString(category));
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
        Integer userId = ContextUtil.getLoginId();
        if (category.getPid() == null) {
            category.setPid(0);
        } else {
            Integer pidCount = categoryMapper.selectCount(new QueryWrapper<Category>()
                    .eq("id", category.getPid())
                    .eq("user_id", userId)
            );
            BizException.throwIf(pidCount == 0, "上级分类不存在");
        }
        // 检测是否存在同名分类
        EntityUtil.trim(category);
        Category oldCategory = categoryMapper.selectOne(new QueryWrapper<Category>()
                .eq("user_id", userId)
                .eq("name", category.getName())
        );
        BizException.throwIf(oldCategory != null, "存在同名分类");
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
     * 更新分类
     *
     * @param category
     * @return
     */
    @Transactional
    public Integer update(Category category) {
        Integer userId = ContextUtil.getLoginId();
        // 只能更新自己的分类
        Integer selectCount = categoryMapper.selectCount(new QueryWrapper<Category>()
                .eq("id", category.getId())
                .eq("user_id", userId)
        );
        BizException.throwIf(selectCount == 0, "分类不存在");
        // 检测父类
        if (category.getPid() == null) {
            category.setPid(0);
        } else {
            BizException.throwIf(category.getPid().equals(category.getId()), "上级分类不能为自己");
            Integer pidCount = categoryMapper.selectCount(new QueryWrapper<Category>()
                    .eq("id", category.getPid())
                    .eq("user_id", userId)
            );
            BizException.throwIf(pidCount == 0, "上级分类不存在");
            // 上级分类不能是自己的子类
            Set<Integer> childNodeSet = getChildNodeSet(category.getId());
            BizException.throwIf(childNodeSet.contains(category.getPid()), "上级分类不能为子分类");
        }
        // 检测是否存在同名分类
        EntityUtil.trim(category);
        Integer oldCount = categoryMapper.selectCount(new QueryWrapper<Category>()
                // 同名文件不是自己
                .notIn("id", category.getId())
                .eq("user_id", userId)
                .eq("name", category.getName())
        );
        BizException.throwIf(oldCount != 0, "存在同名分类");
        Category updateCategory = new Category();
        updateCategory.setId(category.getId());
        updateCategory.setPid(category.getPid());
        updateCategory.setName(category.getName());
        updateCategory.setDescription(category.getDescription());
        return categoryMapper.updateById(updateCategory);
    }

    /**
     * 获取子分类
     *
     * @param pid
     * @return
     */
    private Set<Integer> getChildNodeSet(Integer pid) {
        Set<Integer> set = new HashSet<>();
        categoryMapper.selectList(new QueryWrapper<Category>().eq("pid", pid).select("id"))
                .forEach(child -> {
                    set.add(child.getId());
                    set.addAll(getChildNodeSet(child.getId()));
                });
        return set;
    }


    /**
     * 删除分类
     *
     * @param deleteIds
     * @return
     */
    @Transactional
    public Integer delete(List<Integer> deleteIds) {
        BizException.throwIf(deleteIds == null || deleteIds.size() == 0, "删除分类ID不能为空");
        // 限制用户只能删除自己的分类
        Integer selectCount = categoryMapper.selectCount(new QueryWrapper<Category>()
                .eq("user_id", ContextUtil.getLoginId())
                .in("id", deleteIds)
        );
        BizException.throwIf(deleteIds.size() != selectCount, "分类不存在");
        // 删除的分类下不存在子分类
        Integer childCount = categoryMapper.selectCount(new QueryWrapper<Category>()
                .in("pid", deleteIds)
        );
        BizException.throwIf(childCount != 0, "分类下存在子分类");
        // 删除的分类下不存在文章  todo 回收站中的文章恢复咋办
        Integer articleCount = articleMapper.selectCount(new QueryWrapper<Article>()
                .in("category_id", deleteIds)
        );
        BizException.throwIf(articleCount != 0, "分类下存在没有彻底删除的文章");
        // 删除分类
        int deleteCount = categoryMapper.deleteBatchIds(deleteIds);
        BizException.throwIf(deleteCount != deleteIds.size(), "删除失败");
        return deleteCount;
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
