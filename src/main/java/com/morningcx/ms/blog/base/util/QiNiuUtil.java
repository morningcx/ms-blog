package com.morningcx.ms.blog.base.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.morningcx.ms.blog.base.exception.BizException;
import com.morningcx.ms.blog.entity.Image;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * @author gcx
 * @date 2019/2/28
 */
@Slf4j
public class QiNiuUtil {

    private static final String ACCESS_KEY = "k0oDcLMUVApMpSLbWcao58ETYr89JN0HHxqQ8MJg";
    private static final String SECRET_KEY = "j5KJ-GFpoa4c8-BLSLXVupnG3MEhHr1VcPmX4TCf";
    private static final String BUCKET = "msblog";
    private static final String DOMAIN = "image.morningcx.com";
    private static StringMap imagePutPolicy = getImagePutPolicy();


    /**
     * 上传图片至七牛云
     *
     * @param imageFile
     * @return
     * @throws IOException
     */
    public static Image uploadImage(MultipartFile imageFile) throws IOException {
        String fileType = checkImage(imageFile);
        // 构造一个带指定Zone对象的配置类，zone0代表华东
        Configuration config = new Configuration(Zone.zone0());
        UploadManager uploadManager = new UploadManager(config);
        // 指定文件前缀以及名称，不指定默认用hash值作为文件名
        String key = UUID.randomUUID() + fileType;
        // 验证密钥，生成上传凭证
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        // token有效时长3600s，策略为<bucket>:<key>则同名文件覆盖，为<bucket>则报错
        String token = auth.uploadToken(BUCKET, key, 3600, imagePutPolicy);
        Response response = uploadManager.put(imageFile.getBytes(), key, token);
        Image image = new ObjectMapper().readValue(response.bodyString(), Image.class);
        image.setName(imageFile.getOriginalFilename());
        image.setDomain(DOMAIN);
        image.setPath("http://" + image.getDomain() + "/" + image.getFkey());
        image.setCreateTime(new Date());
        image.setDeleted(0);
        return image;
    }

    /**
     * 删除图片
     *
     * @param bucket
     * @param key
     */
    public static void deletedImage(String bucket, String key) throws IOException {
        Configuration config = new Configuration(Zone.zone0());
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        BucketManager bucketManager = new BucketManager(auth, config);
        bucketManager.delete(bucket, key);
    }

    /**
     * 检测文件类型，并返回后缀名
     *
     * @param imageFile
     * @return
     */
    private static String checkImage(MultipartFile imageFile) {
        BizException.throwIf(imageFile == null || imageFile.isEmpty(), "上传图片不能为空");
        String fileName = imageFile.getOriginalFilename();
        String fileType = fileName.substring(fileName.lastIndexOf("."), fileName.length()).toLowerCase();
        if (!(".jpg".equals(fileType) || ".jpeg".equals(fileType)
                || ".gif".equals(fileType) || ".png".equals(fileType))) {
            BizException.throwBy("上传图片类型只支持jpg、jpeg、gif、png");
        }
        return fileType;
    }


    /**
     * 获取image的json获取策略
     *
     * @return
     */
    private static StringMap getImagePutPolicy() {
        return imagePutPolicy = new StringMap().put("returnBody", new StringBuilder()
                .append("{")
                .append("\"fkey\":$(key)").append(",")
                .append("\"hash\":$(etag)").append(",")
                .append("\"bucket\":$(bucket)").append(",")
                .append("\"size\":$(fsize)").append(",")
                .append("\"type\":$(mimeType)").append(",")
                .append("\"width\":$(imageInfo.width)").append(",")
                .append("\"height\":$(imageInfo.height)").append(",")
                .append("\"average\":$(imageAve.RGB)")
                .append("}")
        );
    }
}
