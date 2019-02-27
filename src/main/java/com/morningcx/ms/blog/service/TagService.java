package com.morningcx.ms.blog.service;

import com.morningcx.ms.blog.mapper.ArticleTagMapper;
import com.morningcx.ms.blog.mapper.TagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author gcx
 * @date 2019/2/22
 */
@Service
public class TagService {
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private ArticleTagMapper articleTagMapper;
}
