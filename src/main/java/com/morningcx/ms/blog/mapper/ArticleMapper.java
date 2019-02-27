package com.morningcx.ms.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.morningcx.ms.blog.entity.Article;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
    @Update("update t_article set views = views + 1 where id = #{id}")
    int updateViewsById(Integer id);

    /**
     * 根据文章id获取文章基本信息、作者、类别
     *
     * @param id
     * @return
     */
    @Select("SELECT a.*,u.`name` as author,c.`name` as category " +
            "from t_article as a " +
            "LEFT JOIN t_user as u on a.author_id = u.id " +
            "LEFT JOIN t_category as c on a.category_id = c.id" +
            "where a.deleted = 0 and a.modifier = 0")
    Article getMetaById(Integer id);

}
