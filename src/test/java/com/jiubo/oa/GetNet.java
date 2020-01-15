package com.jiubo.oa;

import okhttp3.*;

import javax.net.ssl.*;
import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * @desc:
 * @date: 2020-01-14 13:04
 * @author: dx
 * @version: 1.0
 */
public class GetNet {
    public static void main(String[] args) throws Exception {

//        System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,TLSv1,SSLv3");
//        new GetNet().getNet();
//        new GetNet().method();
    }

    public void getNet() throws Exception {
//        TrustManager[] trustAllCerts = buildTrustManagers();
//        final SSLContext sslContext = SSLContext.getInstance("SSL");
//        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
//
//        final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
//
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
//        builder.hostnameVerifier((hostname, session) -> true);
//        OkHttpClient okHttpClient = builder.build();
        OkHttpClient okHttpClient = new OkHttpClient();
        //请求参数
        FormBody requestBody = new FormBody.Builder()
                .build();

        //.post(requestBody)
        Request request = new Request.Builder()
                .url("https://www.zitian.cn/UserCenter/Product/Domain/Manager.ashx?cmd=GetDomainList")
                .addHeader("Cookie", "ASP.NET_SessionId=obolk1ew3kwwsloxawxvyqy1; cart_tempuserid=1e67f120-80b9-4079-b2e6-bb2ebc95cff1; __51cke__=; Hm_lvt_2cf4d478633e232c6911b3b298b577e6=1578973754; __tins__20400249=%7B%22sid%22%3A%201578978478341%2C%20%22vd%22%3A%201%2C%20%22expires%22%3A%201578980278341%7D; __51laig__=2; Hm_lpvt_2cf4d478633e232c6911b3b298b577e6=1578978478; ASP.NET_IdentityId=3140e561c409448f82af3f8bc04adc33")
                .addHeader("Host", "www.zitian.cn")
                .build();
//        Response execute = okHttpClient.newCall(request).execute();
//        System.out.println(execute.body().string());
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(call + "请求失败:" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println(response.body().string());
            }
        });
    }

    public void method() throws Exception {
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, null, null);

        SSLSocketFactory factory = (SSLSocketFactory) context.getSocketFactory();
        SSLSocket socket = (SSLSocket) factory.createSocket();

        String[] protocols = socket.getSupportedProtocols();

        System.out.println("Supported Protocols: " + protocols.length);
        for (int i = 0; i < protocols.length; i++) {
            System.out.println(" " + protocols[i]);
        }

        protocols = socket.getEnabledProtocols();

        System.out.println("Enabled Protocols: " + protocols.length);
        for (int i = 0; i < protocols.length; i++) {
            System.out.println(" " + protocols[i]);
        }
    }

    private static TrustManager[] buildTrustManagers() {
        return new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };
    }

    public void ss(){
        File f = new File(GetNet.class.getResource("").getPath());
        System.out.println(f.getParent());
        System.out.println(System.getProperty("catalina.home"));
        System.out.println(System.getProperty("user.dir"));
        System.out.println(GetNet.class.getResource("/") );
    }
}
