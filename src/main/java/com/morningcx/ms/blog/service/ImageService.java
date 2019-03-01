package com.morningcx.ms.blog.service;

import com.morningcx.ms.blog.base.exception.BusinessException;
import com.morningcx.ms.blog.base.util.QiNiuUtil;
import com.morningcx.ms.blog.entity.Image;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gcx
 * @date 2019/2/28
 */
@Service
public class ImageService {

    public Map<String, Object> mdImageUpload(MultipartFile file) {
        BusinessException.throwIf(file == null || file.isEmpty(), "上传图片不能为空");
        Map<String, Object> result = new HashMap<>(3);
        try {
            Image image = QiNiuUtil.uploadImage(file);
            result.put("success", 1);
            result.put("message", "上传成功");
            result.put("url", "http://" + image.getPath() + "/" + image.getKey());
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", 0);
            result.put("message", "上传失败：" + e.getMessage());
        }
        return result;
    }
}
