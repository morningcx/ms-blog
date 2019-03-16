package com.morningcx.ms.blog.base.util;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author gcx
 * @date 2019/3/9
 */
public class IpUtil {

    private IpUtil() {
    }

    /**
     *
     * @param ip
     * @return
     */
    public static String getLocation(String ip) {
        String result;
        HttpURLConnection connection = null;
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL("http://ip.taobao.com/service/getIpInfo.php?ip=" + ip);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.setRequestMethod("GET");
            // Connection timed out: connect
            // Connection reset
            // 还有可能Server returned HTTP response code: 502 for URL: http://ip.taobao.com/service/getIpInfo.php?ip=0:0:0:0:0:0:0:1
            connection.connect();
            InputStream in = connection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            // Read timed out || null ipv6会出现null
            String json =  bufferedReader.readLine();
            // log.info(json);
            result = parseLocationJSON(json);
        } catch (Exception e) {
            result = e.getMessage();
        } finally {
            IOUtils.closeQuietly(bufferedReader);
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }

    private static String parseLocationJSON(String json) {
        /*JSONObject data = JSON.parseObject(json).getJSONObject("data");
        if (data == null) {
            // code = 1，readLine没有读全parseObject时就会抛异常
            return json;
        }
        StringBuilder result = new StringBuilder();
        result.append(data.getString("country")).append("-")
                .append(data.getString("region")).append("-")
                .append(data.getString("city")).append("-")
                .append(data.getString("isp"));
        System.out.println(json);*/
        return null;
    }

    public static void main(String[] args) throws IOException {
        String json = "{\"code\":0,\"data\":{\"ip\":\"120.78.197.77\",\"country\":\"中国\",\"area\":\"\",\"region\":\"广东\",\"city\":\"深圳\",\"county\":\"XX\",\"isp\":\"阿里云\",\"country_id\":\"CN\",\"area_id\":\"\",\"region_id\":\"440000\",\"city_id\":\"440300\",\"county_id\":\"xx\",\"isp_id\":\"1000323\"}}\n";
        JsonNode jsonNode = new ObjectMapper().readTree(json);
        parseLocationJSON(getLocation("120.78.197.77"));
        System.out.println(jsonNode.get("data"));
    }
}
