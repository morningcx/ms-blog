package com.morningcx.ms.blog.base.util;

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

    public static void main(String[] args) throws IOException {
        System.out.println(" " + null + " ");
        for (String str : getWorthAgent()) {
            System.out.println("--------------------------------------");
            UserAgent userAgent = UserAgent.parseUserAgentString(str);

            OperatingSystem operatingSystem = userAgent.getOperatingSystem();
            System.out.println("-----操作系统家族：" + operatingSystem.getGroup().getName());
            System.out.println("-----操作系统：" + operatingSystem.getName());
            System.out.println("-----设备类型：" + operatingSystem.getDeviceType().getName());
            System.out.println("~");


            Browser browser = userAgent.getBrowser();
            System.out.println("-----浏览器家族：" + browser.getGroup().getName());
            // 浏览器名称+主要版本
            System.out.println("浏览器名称：" + userAgent.getBrowser().getName());
            System.out.println("-----浏览器版本：" + userAgent.getBrowserVersion());
            System.out.println("浏览器类型：" + browser.getBrowserType().getName());

            int i = 0;
        }
    }


    private static List<String> getWorthAgent() {
        return Arrays.asList(
                "Mozilla/5.0 (iPhone; CPU iPhone OS 12_1_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/16D57 QQ/7.9.9.445 V1_IPH_SQ_7.9.9_1_APP_A Pixel/750 Core/WKWebView Device/Apple(iPhone 6s) NetType/4G QBWebViewType/1 WKType/1",
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
                , "Mozilla/5.0 (Linux; Android 6.0.1; Nexus 5X Build/MMB29P) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.96 Mobile Safari/537.36 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)"
                , "Mozilla/5.0 AppleWebKit/537.36 (KHTML, like Gecko; compatible; Googlebot/2.1; +http://www.google.com/bot.html) Safari/537.36"
                , "PostmanRuntime/7.4.0"
                , "Mozilla/5.0 (Linux; U; Android 7.0; zh-cn; MI NOTE Pro Build/NRD90M) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/61.0.3163.128 Mobile Safari/537.36 XiaoMi/MiuiBrowser/10.3.0"
        );
    }
}
