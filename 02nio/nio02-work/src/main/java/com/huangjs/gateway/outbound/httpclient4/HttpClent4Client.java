package com.huangjs.gateway.outbound.httpclient4;

import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.reactor.IOReactorConfig;

/**
 * @Author: HuangFu
 * @Date: 2021/9/28 8:22
 * @Description:
 */
public class HttpClent4Client {

    private CloseableHttpAsyncClient httpclient;

    private static class SingletonHolder {
        private static HttpClent4Client instance = new HttpClent4Client();
    }

    private  HttpClent4Client(){
        int cores = Runtime.getRuntime().availableProcessors();
        IOReactorConfig ioConfig = IOReactorConfig.custom()
                .setConnectTimeout(1000)
                .setSoTimeout(1000)
                .setIoThreadCount(cores)
                .setRcvBufSize(32 * 1024)
                .build();
        httpclient = HttpAsyncClients.custom().setMaxConnTotal(40)
                .setMaxConnPerRoute(8)
                .setDefaultIOReactorConfig(ioConfig)
                .setKeepAliveStrategy((response,context) -> 6000)
                .build();
        httpclient.start();
    }

    public static HttpClent4Client getInstance(){ return SingletonHolder.instance;}

    public CloseableHttpAsyncClient getHttpclient(){
        return this.httpclient;
    }


}
