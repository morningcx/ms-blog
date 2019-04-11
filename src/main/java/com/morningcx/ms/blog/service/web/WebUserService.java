package com.morningcx.ms.blog.service.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.morningcx.ms.blog.base.exception.BizException;
import com.morningcx.ms.blog.entity.Article;
import com.morningcx.ms.blog.entity.ArticleTag;
import com.morningcx.ms.blog.entity.User;
import com.morningcx.ms.blog.mapper.ArticleMapper;
import com.morningcx.ms.blog.mapper.ArticleTagMapper;
import com.morningcx.ms.blog.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author gcx
 * @date 2019/4/9
 */
@Service
public class WebUserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleTagMapper articleTagMapper;

    /**
     * 查询用户信息和文章分类标签数量
     *
     * @param id
     * @return
     */
    public User getUserInfo(Integer id) {
        User user = userMapper.selectOne(new QueryWrapper<User>()
                .eq("id", id)
                .select("id", "name", "github", "zhihu", "wechat", "qq",
                        "email", "signature", "head_image_url"));
        BizException.throwIfNull(user, "用户不存在");
        // 公开文章的数量
        List<Object> publicArticleIds = articleMapper.selectObjs(new QueryWrapper<Article>()
                .eq("author_id", id)
                .eq("modifier", 0)
                .eq("recycle", 0)
                .select("id"));
        // 公开文章所引用标签的数量
        Integer tagCount = publicArticleIds.size() == 0 ? 0 :
                articleTagMapper.selectCount(new QueryWrapper<ArticleTag>()
                        .in("article_id", publicArticleIds)
                        .select("DISTINCT tag_id"));
        // 非空公开分类，分类下没有文章或者没有公开文章则不算进内
        Integer categoryCount = articleMapper.selectCount(new QueryWrapper<Article>()
                .eq("author_id", id)
                .eq("modifier", 0)
                .eq("recycle", 0)
                .select("DISTINCT category_id"));
        user.setArticleCount(publicArticleIds.size());
        user.setTagCount(tagCount);
        user.setCategoryCount(categoryCount);
        return user;
    }
}
