package com.morningcx.ms.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.morningcx.ms.blog.base.result.Result;
import com.morningcx.ms.blog.entity.Article;
import com.morningcx.ms.blog.mapper.ArticleMapper;
import com.morningcx.ms.blog.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gcx
 * @date 2019/2/3
 */
@Slf4j
@RestController
public class ArticleController {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleService articleService;

    @GetMapping("getArticleById")
    public Article getArticleById(Integer id) {
        return articleService.getArticleById(id);
    }


    @PostMapping("insertArticle")
    public Result insertArticle(@Valid Article article) {
        return Result.success(articleService.insertArticle(article));
    }

    @GetMapping("getArticlesByCondition")
    public List<Article> getArticlesByCondition(Article article, Integer curr, Integer size) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>(article);
        return articleMapper.selectPage(
                new Page<>(curr == null ? 1 : curr, size == null ? 10 : size),
                queryWrapper).getRecords();
    }

    @PostMapping("deleteArticle")
    public Integer deleteArticle(Integer id) {
        return articleMapper.deleteById(id);
    }
    /*@GetMapping("getUser")
    public User getUser(Integer id) {
        BusinessException.cause("我测试的");

    }*/

    /*@GetMapping("findByAuthor")
    public List<Article> findByAuthor(Integer author) {
        return articleJpa.findByAuthor(author);
    }*/

    @PostMapping("updateArticle")
    public Article updateArticle(Article article, String test) {
        log.info(test);
        articleMapper.updateById(article);
        return article;
    }

    @PostMapping("mdImageUpload")
    public Map<String, Object> mdImageUpload(@RequestParam("editormd-image-file") MultipartFile file) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            String staticPath = ResourceUtils.getURL("classpath:").getPath() + "/static/";
            String targetPath = "/upload/";
            String fullPath = staticPath + targetPath;
            String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
            File targetFile = new File(fullPath, fileName);
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            log.info(fullPath);
            log.info(fileName);
            file.transferTo(targetFile);
            resultMap.put("success", 1);
            resultMap.put("message", "上传成功！");
            resultMap.put("url", targetPath + fileName);
        } catch (Exception e) {
            resultMap.put("success", 0);
            resultMap.put("message", "上传失败！" + e.getMessage());
            e.printStackTrace();
        }
        return resultMap;
    }

    /**
     * 上传文件
     * 消费者，媒体类型
     */
    /*@PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadFile(@RequestParam("file_name") MultipartFile file) throws Exception {
        log.debug(file.getOriginalFilename());
        OutputStream out = new FileOutputStream("target" + File.separator + file.getOriginalFilename());
        IOUtils.copy(file.getInputStream(), out);

        out.close();
    }

    @GetMapping(value = "/getfile", produces = MediaType.ALL_VALUE)
    public byte[] downloadFile() throws Exception {
        File file = new File("target" + File.separator + "111.txt");
        InputStream is = new FileInputStream(file);
        byte[] bytes = new byte[(int) file.length()];
        IOUtils.readFully(is, bytes);
        return bytes;
    }*/
}
