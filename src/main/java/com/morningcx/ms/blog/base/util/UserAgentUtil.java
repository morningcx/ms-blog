package com.morningcx.ms.blog.base.util;

import cz.mallat.uasparser.OnlineUpdater;
import cz.mallat.uasparser.UASparser;
import cz.mallat.uasparser.UserAgentInfo;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author gcx
 * @date 2019/3/16
 */
public class UserAgentUtil {
    static UASparser uasParser = null;

    static {
        try {
            uasParser = new UASparser(OnlineUpdater.getVendoredInputStream());
            // java.lang.UnsupportedClassVersionError:
            // cz/mallat/uasparser/UASparser : Unsupported major.minor version 51.0
            // 用jdk1.6测试时会报以上错，需要jdk1.7以上版本支持
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        for (String str : getWorthAgent()) {
            System.out.println("==============================" + str);
            UserAgentInfo userAgentInfo = UserAgentUtil.uasParser.parse(str);
            System.out.println("操作系统家族：" + userAgentInfo.getOsFamily());
            System.out.println("操作系统详细名称：" + userAgentInfo.getOsName());
            System.out.println("浏览器名称和版本:" + userAgentInfo.getUaName());
            System.out.println("类型：" + userAgentInfo.getType());
            System.out.println("浏览器名称：" + userAgentInfo.getUaFamily());
            System.out.println("浏览器版本：" + userAgentInfo.getBrowserVersionInfo());
            System.out.println("设备类型：" + userAgentInfo.getDeviceType());
            System.out.println("--------------------------------------");
            UserAgent userAgent = UserAgent.parseUserAgentString(str);
            Browser browser = userAgent.getBrowser();
            OperatingSystem operatingSystem = userAgent.getOperatingSystem();
            /*System.out.println("操作系统大写：" + operatingSystem.name());*/
            System.out.println("操作系统：" + operatingSystem.getName());
            System.out.println(userAgent.getBrowser().getGroup().getName());
            System.out.println(userAgent.getBrowserVersion().getVersion());
            System.out.println("设备类型：" + operatingSystem.getDeviceType().getName());

            System.out.println("浏览器名称：" + browser.getName() + " (" + browser.getVersion(str) + ")");
            System.out.println("浏览器类型：" + browser.getBrowserType().getName());
            int i = 0;
        }
    }


    private static List<String> getWorthAgent() {
        return Arrays.asList(
                // Max os X
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/601.7.7 (KHTML, like Gecko) Version/9.1.2 Safari/601.7.7"
                // ios
                , "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1 (compatible; Baiduspider-render/2.0; +http://www.baidu.com/search/spider.html)"
                // ie 9
                , "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)"
                // ie 8
                , "Mozilla/4.0 (BindOk; SocketInit; Compatible win32 71.126.6.164; MSIE 8.0; Windows NT 6.1; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; .NET4.0C; .NET4.0E)"
                // window 10
                , "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36"
        );
    }
}
