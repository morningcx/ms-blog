package com.morningcx.ms.blog.base.util;

import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.util.*;

/**
 * nmap工具类
 *
 * @author gcx
 * @date 2019/3/20
 */
@Slf4j
public class NmapUtil {
    /**
     * nmap.exe的可执行绝对路径
     */
    private final static String NMAP = "nmap";

    private NmapUtil() {
    }

    /**
     * 批量主机发现
     *
     * @param hosts
     * @throws Exception
     */
    public static Map<String, List<String>> hostDiscovery(Collection<String> hosts) throws Exception {
        StringBuilder sb = new StringBuilder();
        for (String host : hosts) {
            if (IpUtil.isValidIp(host)) {
                sb.append(" ").append(host);
            }
        }
        return hostDiscoveryExec("-v -n -sn -oX -", sb.toString());
    }

    /**
     * 单个主机发现
     *
     * @param host
     * @throws Exception
     */
    public static boolean hostDiscovery(String host) throws Exception {
        if (IpUtil.isValidIp(host)) {
            return hostDiscoveryExec("-v -n -sn -oX -", host).containsKey("up");
        }
        return false;
    }

    /**
     * 执行主机发现指令并处理xml结果
     *
     * @param options
     * @param target
     * @throws Exception
     */
    private static Map<String, List<String>> hostDiscoveryExec(String options, String target) throws Exception {
        Map<String, List<String>> map = new HashMap<>(2);
        Process p = exec(options, target);
        // 解析xml
        Document document = new SAXReader().read(p.getInputStream());
        Element nmaprun = document.getRootElement();
        Iterator hosts = nmaprun.elementIterator("host");
        while (hosts.hasNext()) {
            Element host = (Element) hosts.next();
            Element status = host.element("status");
            Element address = host.element("address");
            String ip = address.attribute("addr").getValue();
            String state = status.attribute("state").getValue();
            map.computeIfAbsent(state, k -> new ArrayList<>()).add(ip);
        }
        return map;
    }


    /**
     * 执行nmap命令
     *
     * @param options 选项
     * @param target  目标
     * @return
     * @throws IOException
     */
    private static Process exec(String options, String target) throws IOException {
        String cmd = NMAP + " " + options + " " + target;
        log.info("NMap Command：" + cmd);
        return Runtime.getRuntime().exec(cmd);
    }

    public static void main(String[] args) throws Exception {
        /*Map<String, List<String>> stringListMap = hostDiscovery(Arrays.asList("120.78.197.77", "120.78.197.78"));
        for (Map.Entry<String, List<String>> entry : stringListMap.entrySet()) {
            System.out.println(entry.getKey() + entry.getValue());
        }*/
        System.out.println(hostDiscovery("120.78.197.77"));
    }
}
