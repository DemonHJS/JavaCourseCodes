package com.huangjs.nio.netty.okhttp;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @Author: HuangFu
 * @Date: 2021/9/7 22:30
 * @Description:
 */
public class OkHttpUtils {
    // 缓存客户端实例
    public OkHttpClient client;

    private static class SingletonHolder {
        private static OkHttpUtils instance = new OkHttpUtils();
    }

    private OkHttpUtils(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        client = builder.connectTimeout(5L, TimeUnit.SECONDS)
                .writeTimeout(5L, TimeUnit.SECONDS)
                .readTimeout(5L, TimeUnit.SECONDS)
//                .sslSocketFactory(sslSocketFactory, trustManager)
                .hostnameVerifier((s, sslSession) -> true)
                .build();
    }

    public static OkHttpUtils getInstance() {
        return SingletonHolder.instance;
    }

    // GET 调用
    public String getAsString(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }



    public static void main(String[] args) throws Exception {

        String url = "https://square.github.io/okhttp/";
        String text = OkHttpUtils.getInstance().getAsString(url);
        System.out.println("url: " + url + " ; response: \n" + text);
    }
}
