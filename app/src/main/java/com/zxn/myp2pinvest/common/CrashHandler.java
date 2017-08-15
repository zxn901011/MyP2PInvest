package com.zxn.myp2pinvest.common;

import android.os.Build;
import android.os.Looper;
import android.widget.Toast;

import com.zxn.myp2pinvest.utils.LogUtil;
import com.zxn.myp2pinvest.utils.UiUtils;

/**
 * Created by zxn on 2017-08-10.
 * 程序中的未捕获的全局异常的捕获（单例）
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler{

    private static final String TAG = "CrashHandler";

    //系统默认的处理未捕获异常的处理器
    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;
    //懒汉式
    //本身实例化未捕获异常的处理器的操作就是系统在一个单独的线程中完成的，所以不设计到
    //多线程问题，所以使用懒汉式更好
    private CrashHandler(){
    }
    private static CrashHandler crashHandler=null;

    public static CrashHandler newInstance(){
        if (crashHandler==null) {
            crashHandler = new CrashHandler();
        }
        return crashHandler;
    }

    public void init(){
        uncaughtExceptionHandler=Thread.getDefaultUncaughtExceptionHandler();
        //将当前类设置为出现未捕获异常的处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 一旦系统出现为捕获的异常就会调用如下的方法
     * @param t
     * @param e
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
//        Log.e(TAG,"亲，出现了未捕获的异常！"+e.getMessage());
        new Thread(){
            @Override
            public void run() {
                super.run();
                Looper.prepare();
                //prepare()和loop()之间的操作就是在主线程中执行的
                //在android系统中，默认情况下，一个线程中是不可以调用Looper进行消息的处理的，除非是主线程
                Toast.makeText(UiUtils.getContext(), "亲，出现了未捕获的异常", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();

        collectException(e);
        try {
            Thread.sleep(2000);
            AppManager.newInstance().removeCurrent();
            //结束当前的进程
            android.os.Process.killProcess(android.os.Process.myPid());
            //结束虚拟机
            System.exit(0);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }

    private void collectException(Throwable e) {
        final String eMessage=e.getMessage();
        //收集具体的客户的手机、系统信息，Build类
        final String message= Build.DEVICE+":"+Build.MODEL+":"+Build.PRODUCT+":"+Build.VERSION.SDK_INT;
        //发送给后台
        new Thread(){
            @Override
            public void run() {
                super.run();
                //需要按照指定的url，访问后台的servlet，将异常信息发送过去
                LogUtil.e(eMessage);
                LogUtil.e(message);
            }
        }.start();
    }
}
