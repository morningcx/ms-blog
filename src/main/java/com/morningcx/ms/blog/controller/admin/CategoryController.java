package com.morningcx.ms.blog.controller.admin;

import com.morningcx.ms.blog.base.annotation.Log;
import com.morningcx.ms.blog.base.enums.LogTypeEnum;
import com.morningcx.ms.blog.base.result.Result;
import com.morningcx.ms.blog.entity.Category;
import com.morningcx.ms.blog.service.admin.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author gcx
 * @date 2019/3/8
 */
@RestController
@RequestMapping(path = "category", name = "分类")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Log(type = LogTypeEnum.Query, desc = "查询分类id:{id}信息")
    @GetMapping("getById")
    public Result getById(Integer id) {
        return Result.ok(categoryService.getById(id));
    }

    @Log(type = LogTypeEnum.PAGE, desc = "分页查询分类 页码：{page} 页量：{limit}")
    @GetMapping("listPage")
    public Result listPage(Category category, Integer page, Integer limit) {
        return Result.ok(categoryService.listPage(category, page, limit));
    }

    @Log(type = LogTypeEnum.CREATE, desc = "新增分类{category.id}:{category.name}")
    @PostMapping("insert")
    public Result insert(@Valid Category category) {
        return Result.ok(categoryService.insert(category));
    }

    @Log(type = LogTypeEnum.UPDATE, desc = "更新分类id:{id}的{level}为{value}")
    @PostMapping("updateLevel")
    public Result updateLevel(Integer id, String level, Integer value) {
        return Result.ok(categoryService.updateLevel(id, level, value));
    }

    @Log(type = LogTypeEnum.UPDATE, desc = "修改ID为{category.id}的分类")
    @PostMapping("update")
    public Result update(@Valid Category category) {
        return Result.ok(categoryService.update(category));
    }

    @Log(type = LogTypeEnum.DELETE, desc = "删除分类ID：{deleteIds}")
    @PostMapping("delete")
    public Result delete(@RequestBody List<Integer> deleteIds) {
        return Result.ok(categoryService.delete(deleteIds));
    }

    @Log(type = LogTypeEnum.Query, desc = "查询分类树")
    @GetMapping("getTree")
    public Result getCategoryTree() {
        return Result.ok(categoryService.getCategoryTree());
    }
}
