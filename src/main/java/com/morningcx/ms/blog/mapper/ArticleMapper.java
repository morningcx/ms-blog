package com.morningcx.ms.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.morningcx.ms.blog.entity.Article;

/**
 * @author gcx
 * @date 2019/2/19
 */
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 根据文章id更新文章的浏览次数
     *
     * @param id
     * @return
     */
    int updateViewsById(Integer id);


    /**
     * 根据文章id获取文章基本信息、作者、类别
     *
     * @param id
     * @return
     */
    Article getMetaById(Integer id);
}
