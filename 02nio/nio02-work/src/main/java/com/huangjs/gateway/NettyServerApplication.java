package com.huangjs.gateway;

import com.huangjs.gateway.inbound.HttpInboundServer;
import lombok.Data;

import java.util.Arrays;

/**
 * @Author: HuangFu
 * @Date: 2021/9/8 8:23
 * @Description:
 */
@Data
public class NettyServerApplication {


    public static void main(String[] args) {

        String proxyPort = System.getProperty("proxyPort","8888");
        String proxyServers = System.getProperty("proxyServers","http://localhost:8801,http://localhost:8802");

        int port = Integer.parseInt(proxyPort);
        HttpInboundServer httpInboundServer = new HttpInboundServer(port, Arrays.asList(proxyServers));
        try {
            httpInboundServer.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
