package com.morningcx.ms.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.morningcx.ms.blog.entity.Article;
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

}
