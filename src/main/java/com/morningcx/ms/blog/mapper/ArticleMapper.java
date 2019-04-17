package com.morningcx.ms.blog.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.morningcx.ms.blog.entity.Article;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

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
     * 根据文章id更新文章的点赞次数
     *
     * @param id
     * @return
     */
    int updateLikesById(Integer id);

    /**
     * 根据文章id以及登录用户id回收文章
     *
     * @param ids
     * @param authorId
     * @return
     */
    int recycleBatchIds(@Param("ids") Collection<Integer> ids, @Param("authorId") Integer authorId);

    /**
     * 根据文章id以及登录用户id恢复回收站中的文章
     *
     * @param ids
     * @param authorId
     * @return
     */
    int recoverBatchIds(@Param("ids") Collection<Integer> ids, @Param("authorId") Integer authorId);

    /**
     * 联表分页查询分类
     *
     *
     * @param page
     * @param wrapper
     * @return
     */
    IPage<Article> listSimplePage(IPage<Article> page, @Param("ew") Wrapper<Article> wrapper);
}
