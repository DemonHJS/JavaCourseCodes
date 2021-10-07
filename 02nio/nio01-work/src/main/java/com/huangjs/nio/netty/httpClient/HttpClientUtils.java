package com.huangjs.nio.netty.httpClient;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @Author: HuangFu
 * @Date: 2021/9/7 22:57
 * @Description:
 */
public class HttpClientUtils {

    private static HttpClientUtils instance;

    public static CloseableHttpClient httpclient = HttpClients.createDefault();

    private static class SingletonHolder {
        private static HttpClientUtils instance = new HttpClientUtils();
    }

    public static HttpClientUtils getInstance(){ return SingletonHolder.instance;}

    private HttpClientUtils() {

    }

    // GET 调用
    public String getAsString(String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response1 = null;
        try {
            response1 = httpclient.execute(httpGet);
            System.out.println(response1.getStatusLine());
            HttpEntity entity1 = response1.getEntity();
            return EntityUtils.toString(entity1, "UTF-8");
        } finally {
            if (null != response1) {
                response1.close();
            }
        }
    }

    public static void main(String[] args) throws Exception {

        String url = "https://square.github.io/okhttp/";
        String text = HttpClientUtils.getInstance().getAsString(url);
        System.out.println("url: " + url + " ; response: \n" + text);
    }
}
