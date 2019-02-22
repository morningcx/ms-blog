package com.morningcx.ms.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.morningcx.ms.blog.entity.ArticleTag;
import com.morningcx.ms.blog.entity.Tag;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author guochenxiao
 * @date 2019/2/21
 */
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {
    /**
     * 通过文章id获取标签列表
     *
     * @param articleId
     * @return
     */
    @Select("select t.* from t_article_tag as a LEFT JOIN t_tag as t on a.tag_id = t.id where a.article_id = #{id}")
    List<Tag> listTagsByArticleId(Integer articleId);
}
