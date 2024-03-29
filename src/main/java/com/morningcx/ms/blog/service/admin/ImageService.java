package com.morningcx.ms.blog.service.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.morningcx.ms.blog.base.exception.BizException;
import com.morningcx.ms.blog.base.util.QiNiuUtil;
import com.morningcx.ms.blog.entity.Config;
import com.morningcx.ms.blog.entity.Image;
import com.morningcx.ms.blog.mapper.ConfigMapper;
import com.morningcx.ms.blog.mapper.ImageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author gcx
 * @date 2019/2/28
 */
@Service
public class ImageService {
    @Autowired
    private ImageMapper imageMapper;
    @Autowired
    private ConfigMapper configMapper;

    /**
     * markdown上传图片到七牛
     *
     * @param imageFile
     * @return
     */
    @Transactional
    public Map<String, Object> mdImageUpload(MultipartFile imageFile) {
        Map<String, Object> result = new HashMap<>(3);
        try {
            Image image = QiNiuUtil.uploadImage(imageFile, getOssConfig());
            imageMapper.insert(image);
            result.put("success", 1);
            result.put("message", "上传成功");
            result.put("url", image.getPath());
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", 0);
            result.put("message", "上传失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 通用图片上传
     *
     * @param imageFile
     * @return
     */
    @Transactional
    public String imageUpload(MultipartFile imageFile) throws IOException {
        Image image = QiNiuUtil.uploadImage(imageFile, getOssConfig());
        imageMapper.insert(image);
        return image.getPath();
    }

    /**
     * 根据id查询图片
     *
     * @param id
     * @return
     */
    public Image getById(Integer id) {
        Image image = imageMapper.selectById(id);
        BizException.throwIfNull(image, "图片不存在");
        return image;
    }


    /**
     * 分页查询图片
     *
     * @param page
     * @param limit
     * @return
     */
    public IPage<Image> listPage(Integer page, Integer limit) {
        return imageMapper.selectPage(new Page<>(page, limit),
                new QueryWrapper<Image>().orderByDesc("create_time"));
    }

    /**
     * 图片墙
     *
     * @param page
     * @param limit
     * @return
     */
    public IPage<Image> listWall(Integer page, Integer limit) {
        return imageMapper.selectPage(new Page<>(page, limit),
                new QueryWrapper<Image>().select("id", "path").orderByDesc("create_time"));
    }

    /**
     * 删除图片
     *
     * @param deleteIds
     * @return
     */
    public int delete(List<Integer> deleteIds) throws IOException {
        BizException.throwIf(deleteIds == null || deleteIds.size() == 0, "删除图片ID不能为空");
        List<Image> images = imageMapper.selectList(new QueryWrapper<Image>()
                .in("id", deleteIds)
                .select("id", "bucket", "fkey"));
        Map<String, String> ossConfig = getOssConfig();
        for (Image image : images) {
            QiNiuUtil.deletedImage(image.getBucket(), image.getFkey(), ossConfig);
            imageMapper.deleteById(image.getId());
        }
        return images.size();
    }


    /**
     * 获取对象存储配置
     *
     * @return
     */
    private Map<String, String> getOssConfig() {
        // todo redis
        return configMapper.selectList(new QueryWrapper<Config>().lambda()
                .inSql(Config::getPid, "select id from t_config where keyword='oss'")
                .select(Config::getKeyword, Config::getValue))
                .stream().collect(Collectors.toMap(Config::getKeyword, Config::getValue));
    }

}
