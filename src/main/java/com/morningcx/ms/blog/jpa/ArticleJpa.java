package com.morningcx.ms.blog.jpa;

import com.morningcx.ms.blog.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author guochenxiao
 * @date 2019/2/3
 */
public interface ArticleJpa extends JpaRepository<Article, Integer> {
}
