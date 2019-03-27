package com.morningcx.ms.blog.controller;

import com.morningcx.ms.blog.base.annotation.Log;
import com.morningcx.ms.blog.base.enums.OpEnum;
import com.morningcx.ms.blog.base.result.Result;
import com.morningcx.ms.blog.entity.Tag;
import com.morningcx.ms.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gcx
 * @date 2019/3/16
 */
@RestController
@RequestMapping(path = "tag", name = "标签")
public class TagController {

    @Autowired
    private TagService tagService;

    @Log(type = OpEnum.PAGE, desc = "分页查询引用标签")
    @GetMapping("listReferencePage")
    public Result listReferenceByPage(Tag tag, Integer page, Integer limit) {
        return Result.ok(tagService.listReferencePage(tag, page, limit));
    }

    @Log(type = OpEnum.READ, desc = "查询所有标签名称")
    @GetMapping("listAllTagsName")
    public Result listAllTagsName() {
        return Result.ok(tagService.listAllTagsName());
    }
}
