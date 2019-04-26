package com.morningcx.ms.blog.base.util;


import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbMakerConfigException;
import org.lionsoul.ip2region.DbSearcher;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author gcx
 * @date 2019/3/9
 */
public class IpUtil {

    private static Pattern pattern = Pattern.compile("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");

    private IpUtil() {
    }

    /**
     * 判断输入的ipv4地址是否合法
     *
     * @param ipAddress
     * @return
     */
    public static boolean isValidIp(String ipAddress) {
        return ipAddress != null && pattern.matcher(ipAddress).matches();
    }

    /**
     * 数字ip转换为字符串ip
     *
     * @param ipNum
     * @return
     */
    public static String ipNum2Str(long ipNum) {
        StringBuilder str = new StringBuilder();
        str.append((ipNum >> 24) & 0xff).append(".").append((ipNum >> 16) & 0xff).append(".")
                .append((ipNum >> 8) & 0xff).append(".").append(ipNum & 0xff);
        return str.toString();
    }

    /**
     * 字符串ip转换为数字ip
     *
     * @param ipStr
     * @return
     * @throws Exception
     */
    public static long ipStr2Num(String ipStr) {
        if (!isValidIp(ipStr)) {
            return 0L;
        }
        String[] fields = ipStr.split("\\.");
        long ipNum = 0;
        for (int i = 0; i < fields.length; ++i) {
            ipNum |= Long.parseLong(fields[i]) << (24 - 8 * i);
        }
        return ipNum;
    }

    /**
     * 生成指定范围内的ip列表
     *
     * @param start 开始ip
     * @param end   结束ip
     * @return
     * @throws Exception
     */
    public static List<String> ipRange(String start, String end) {
        long s = ipStr2Num(start);
        long e = ipStr2Num(end);
        List<String> result = new ArrayList<>((int) (e - s + 1));
        while (s <= e) {
            result.add(ipNum2Str(s++));
        }
        return result;
    }

    /**
     * 获取真实ip地址
     *
     * @param request
     * @return
     */
    public static String getRealIp(HttpServletRequest request) {
        String unKnow = "unknown";
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || unKnow.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unKnow.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unKnow.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || unKnow.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || unKnow.equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || unKnow.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip.contains(",") ? ip.split(",")[0] : ip;
    }

    /**
     * 根据ip获取地理位置和运营商
     *
     * @param ip
     * @param dbPath
     * @return
     */
    public static String ip2region(String ip, String dbPath) {
        if (ip == null) {
            return null;
        }
        DbSearcher searcher = null;
        try {
            searcher = new DbSearcher(new DbConfig(), dbPath);
            // 只要ip不为null，即使格式错误也不会报错(显示内网ip)
            return searcher.btreeSearch(ip).getRegion();
        } catch (IOException | DbMakerConfigException e) {
            e.printStackTrace();
        } finally {
            if (searcher != null) {
                try {
                    searcher.close();
                } catch (IOException e) {
                    // to nothing
                }
            }
        }
        return null;
    }
}
