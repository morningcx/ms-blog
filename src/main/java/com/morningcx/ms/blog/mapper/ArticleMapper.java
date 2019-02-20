package com.morningcx.ms.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.morningcx.ms.blog.entity.Article;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author guochenxiao
 * @date 2019/2/19
 */
public interface ArticleMapper extends BaseMapper<Article> {

    @Select("select author_id from t_article")
    List<Article> getAll();
}
