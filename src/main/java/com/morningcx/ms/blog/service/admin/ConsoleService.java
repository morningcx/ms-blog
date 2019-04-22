package com.morningcx.ms.blog.service.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.morningcx.ms.blog.entity.Article;
import com.morningcx.ms.blog.entity.Category;
import com.morningcx.ms.blog.entity.Image;
import com.morningcx.ms.blog.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gcx
 * @date 2019/4/22
 */
@Service
public class ConsoleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ImageMapper imageMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 统计文章、分类、标签、图片四个模块的数据总数
     *
     * @return
     */
    public Map<String, Object> getModuleCount() {
        Integer articleCount = articleMapper.selectCount(
                new QueryWrapper<Article>().lambda().eq(Article::getRecycle, 0));
        Integer articlePublishCount = articleMapper.selectCount(
                new QueryWrapper<Article>().lambda().eq(Article::getRecycle, 0).eq(Article::getModifier, 0));
        Integer categoryCount = categoryMapper.selectCount(null);
        Integer categoryTopCount = categoryMapper.selectCount(
                new QueryWrapper<Category>().lambda().eq(Category::getPid, 0));
        Integer tagCount = tagMapper.selectCount(null);
        // todo 删除或者回收文章的引用
        Integer tagReferenceCount = articleTagMapper.selectCount(null);
        Integer imageCount = imageMapper.selectCount(null);
        Object imageSize = imageMapper.selectObjs(
                new QueryWrapper<Image>().select("sum(size)")).get(0);
        Map<String, Object> countMap = new HashMap<>(4);
        countMap.put("article", Arrays.asList(articleCount, articlePublishCount));
        countMap.put("category", Arrays.asList(categoryCount, categoryTopCount));
        countMap.put("tag", Arrays.asList(tagCount, tagReferenceCount));
        countMap.put("image", Arrays.asList(imageCount, imageSize == null ? 0 : imageSize));
        return countMap;
    }

    /**
     * 查询文章点击排行
     *
     * @return
     */
    public List<Article> listTopViewArticle() {
        return articleMapper.selectPage(new Page<>(1, 8), new QueryWrapper<Article>()
                .select("title", "views", "likes")
                .orderByDesc("views"))
                .getRecords();
    }
}
