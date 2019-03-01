package com.morningcx.ms.blog.controller;

import com.morningcx.ms.blog.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author gcx
 * @date 2019/2/28
 */
@RestController
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("mdImageUpload")
    public Map<String, Object> mdImageUpload(@RequestParam("editormd-image-file") MultipartFile file) {
        return imageService.mdImageUpload(file);
    }

    /*@PostMapping("mdImageUpload")
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
    }*/

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
