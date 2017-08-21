package com.zxn.myp2pinvest.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zxn.myp2pinvest.R;
import com.zxn.myp2pinvest.bean.UpdateInfo;
import com.zxn.myp2pinvest.common.AppManager;
import com.zxn.myp2pinvest.common.AppNetConfig;
import com.zxn.myp2pinvest.utils.UiUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WelcomeActivity extends Activity {

    private static final int TO_MAIN = 1;
    private static final int UPDATE = 2;
    private static final int DOWNLOAD_APK_FAIL = 3;
    private static final int DOWNLOAD_APK_SUCCESS = 4;
    @Bind(R.id.iv_welcome_icon)
    ImageView ivWelcomeIcon;
    @Bind(R.id.tv_welcome_version)
    TextView tvWelcomeVersion;
    @Bind(R.id.rl_welcome)
    RelativeLayout rlWelcome;
    private long startTime;
    private UpdateInfo updateInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        AppManager.newInstance().addActivity(this);
        //提供自动动画
        setAnimation();
        //联网更新应用
        updateApkFile();
    }

    private void updateApkFile() {
        //获取系统当前时间
        startTime = System.currentTimeMillis();
        boolean connect=isConnect();
        if (!connect){
            UiUtils.toast("当前没有移动数据网络",false);
            toMain();
        }else {//有数据网络
            //联网获取apk版本
            String url= AppNetConfig.UPDATE;
            RequestParams params=new RequestParams();
            AsyncHttpClient client=new AsyncHttpClient();
            client.post(url,params,new AsyncHttpResponseHandler(){
                @Override
                public void onSuccess(String content) {
                    updateInfo = JSON.parseObject(content, UpdateInfo.class);
                    handler.sendEmptyMessage(UPDATE);
                }

                @Override
                public void onFailure(Throwable error, String content) {
                    UiUtils.toast("请求联网数据失败",false);
                }
            });
        }
    }

    private void toMain() {
        long currentTime = System.currentTimeMillis();
        long delayTime=3000-(currentTime-startTime);
        if (delayTime<0){
            delayTime=0;
        }
        handler.sendEmptyMessageDelayed(TO_MAIN,delayTime);
    }

    private boolean isConnect() {
        boolean connect=false;
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager!=null) {
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info != null) {
                connect = info.isConnected();
            }
            return connect;
        }
        return false;
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case TO_MAIN:
                    finish();
                    Intent intent=new Intent(WelcomeActivity.this,MainActivity.class);
                    startActivity(intent);
                    AppManager.newInstance().removeActivity(WelcomeActivity.this);
                    break;
                case UPDATE:
                    //获取当前应用的版本信息
                    String version=getVersion();
                    tvWelcomeVersion.setText(version);//更新页面显示的版本信息
                    if (version.equals(updateInfo.version)){
                        UiUtils.toast("当前应用已经是最新版本",false);
                        toMain();
                    }else {
                        new AlertDialog.Builder(WelcomeActivity.this)
                                .setTitle("下载最新版本")
                                .setMessage(updateInfo.desc)
                                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        downLoadApkFile(updateInfo.apkUrl);
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        toMain();
                                    }
                                })
                                .show();
                    }
                    break;
                case DOWNLOAD_APK_FAIL:
                    UiUtils.toast("联网下载数据失败",false);
                    toMain();
                    break;
                case DOWNLOAD_APK_SUCCESS:
                    UiUtils.toast("下载应用数据成功",false);
                    dialog.dismiss();
                    installApk();//安装下载的apk
                    finish();
                    break;
            }
        }
    };

    private void installApk() {
        Intent intent=new Intent("android.intent.action.INSTALL_PACKAGE");
        intent.setData(Uri.parse("file:"+apkFile.getAbsolutePath()));
        startActivity(intent);
    }

    private ProgressDialog dialog;
    private File apkFile;

    private void downLoadApkFile(final String apkUrl) {
        //初始化水平进度条
        dialog=new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.show();
        //初始化数据要保存的位置
        File fileDir;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            fileDir=this.getExternalFilesDir("");
        }else {
            fileDir=this.getFilesDir();
        }
        apkFile=new File(fileDir,"update.apk");
        //启动一个分线程联网下载数据
        new Thread(){
            @Override
            public void run() {
                InputStream is=null;
                FileOutputStream fos=null;
                HttpURLConnection urlConnection =null;
                try {
                    URL url = new URL(apkUrl);
                    urlConnection=(HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setConnectTimeout(3000);
                    urlConnection.setReadTimeout(5000);
                    urlConnection.connect();
                    if (urlConnection.getResponseCode()==200){
                        dialog.setMax(urlConnection.getContentLength());
                        is = urlConnection.getInputStream();
                        fos=new FileOutputStream(apkFile);
                        byte[] buffer=new byte[1024];
                        int len;
                        while ((len=is.read(buffer))!=-1){
                            fos.write(buffer,0,len);
                            dialog.incrementProgressBy(len);//更新dialog的进度
                            SystemClock.sleep(1);
                        }
                        handler.sendEmptyMessage(DOWNLOAD_APK_SUCCESS);
                    }else {
                        handler.sendEmptyMessage(DOWNLOAD_APK_FAIL);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if (is!=null){
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fos!=null){
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (urlConnection!=null){
                        urlConnection.disconnect();
                    }
                }
            }
        }.start();
    }

    private String getVersion() {
        String version="未知版本";
        PackageManager manager=getPackageManager();
        if (manager!=null) {
            try {
                PackageInfo info =manager.getPackageInfo(getPackageName(),0);
                version=info.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return version;
    }

    private void setAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);//0.完全透明1.完全不透明
        alphaAnimation.setDuration(3000);
        alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());//设置动画的变化率
        rlWelcome.setAnimation(alphaAnimation);
    }
}
