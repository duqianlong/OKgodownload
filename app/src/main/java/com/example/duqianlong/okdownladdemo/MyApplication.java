package com.example.duqianlong.okdownladdemo;

import android.app.Application;

import com.lzy.okgo.OkGo;

import okhttp3.OkHttpClient;

/**
 * Created by Duqianlong on 2019/3/7.
 */

public class MyApplication extends Application {
    OkHttpClient build;
    @Override
    public void onCreate() {
        super.onCreate();
          build = new OkHttpClient.Builder()
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                .build();
        OkGo.getInstance().init(this).setOkHttpClient(build);
    }
}
