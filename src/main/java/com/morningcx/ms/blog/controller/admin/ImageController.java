package com.morningcx.ms.blog.controller.admin;

import com.morningcx.ms.blog.base.annotation.Log;
import com.morningcx.ms.blog.base.enums.LogTypeEnum;
import com.morningcx.ms.blog.base.result.Result;
import com.morningcx.ms.blog.service.admin.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author gcx
 * @date 2019/2/28
 */
@RestController
@RequestMapping(path = "image", name = "图片")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("mdImageUpload")
    @Log(type = LogTypeEnum.UPLOAD, desc = "上传MarkDown图片")
    public Map<String, Object> mdImageUpload(@RequestParam("editormd-image-file") MultipartFile file) {
        return imageService.mdImageUpload(file);
    }

    @PostMapping("imageUpload")
    @Log(type = LogTypeEnum.UPLOAD, desc = "上传常规图片")
    public Result imageUpload(MultipartFile file) throws IOException {
        return Result.ok(imageService.imageUpload(file));
    }

    @GetMapping("listPage")
    @Log(type = LogTypeEnum.PAGE, desc = "页码：{page} 页量：{limit}")
    public Result listPage(Integer page, Integer limit) {
        return Result.ok(imageService.listPage(page, limit));
    }

    @PostMapping("delete")
    @Log(type = LogTypeEnum.DELETE, desc = "删除图片{deleteIds}")
    public Result delete(@RequestBody List<Integer> deleteIds) throws IOException {
        return Result.ok(imageService.delete(deleteIds));
    }
}
