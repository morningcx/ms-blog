package com.morningcx.ms.blog.base.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.morningcx.ms.blog.entity.Article;
import com.morningcx.ms.blog.entity.User;

/**
 * 查询工具
 *
 * @author gcx
 * @date 2019/3/2
 */
public class SelectUtil {
    /**
     * 管理端文章查询基础条件，未删除&&未回收&&当前用户文章
     * 浏览端还要加上eq("modifier", 0)指定公开文章
     *
     * @return
     */
    public static QueryWrapper<Article> baseWrapper() {
        User author = RequestUtil.getCurrentUser();
        return new QueryWrapper<Article>()
                //.eq("deleted", 0) mp自动添加
                .eq("recycle", 0)
                .eq("author_id", author.getId());
    }
}
