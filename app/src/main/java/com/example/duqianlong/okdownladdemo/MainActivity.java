package com.example.duqianlong.okdownladdemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    ProgressBar progressBar;
    TextView textView;
    CircleProgressBar circleProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.myProgressbar);
        textView = findViewById(R.id.mtv);
        circleProgressBar = findViewById(R.id.circu);
        circleProgressBar.setPathWidth(10);

        findViewById(R.id.mbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String destfiledir = Environment.getExternalStorageDirectory().getPath() + "/OAdownload";
                String destfilename = "OA.apk";


                OkGo.<File>get("http://immobile.r93535.com:8086/LN/GetExtApp/extapp/ExtApp/A/1111/LatestVersion").execute(new FileCallback(destfiledir, destfilename) {
                    @Override
                    public void onStart(Request<File, ? extends Request> request) {
                        super.onStart(request);
                        Log.e("gogogo", "正在加载中");
                        Toast.makeText(MainActivity.this, "开始下载", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        super.downloadProgress(progress);
                        long totalSize = progress.totalSize;//总长度
                        long currentSize = progress.currentSize;//本次下载的大小
                        //正常进度条
                        progressBar.setMax((int) totalSize);
                        progressBar.setProgress((int) currentSize);
                        //自定义的进度条
                        circleProgressBar.setMax((int) totalSize);
                        circleProgressBar.setProgress((int) currentSize);

                        textView.setText(String.valueOf(progress.fraction * 100 + "%"));  //下载进度
                    }

                    @Override
                    public void onError(Response<File> response) {
                        super.onError(response);
                        Log.e("gogogo", "下载出错");
                        Toast.makeText(MainActivity.this, "下载出错，请检查网络。", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(Response<File> response) {
                        Log.e("gogogo", "下载成功");
                        Toast.makeText(MainActivity.this, "下载成功，启动安装", Toast.LENGTH_SHORT).show();
                        String s = destfiledir + "/OA.apk";
                        install(s);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        Log.e("gogogo", "结束");
//                        File file = new File(destfiledir + "/OA.apk");
//                        installApk(file);

                    }
                });
            }
        });
    }

    /**
     * 安装
     */
    private void install(String filePath) {
        Log.i("file", "开始执行安装: " + filePath);
        File apkFile = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Log.w("file", "版本大于 N ，开始使用 fileProvider 进行安装");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(
                    this
                    , "com.example.duqianlong.okdownladdemo.fileprovider"
                    , apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            Log.w("file", "正常进行安装");
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        startActivity(intent);
    }

}
