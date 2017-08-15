package com.zxn.myp2pinvest.utils;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.zxn.myp2pinvest.common.MyApplication;

/**
 * Created by zxn on 2017-08-10.
 * 专门提供为处理一些UI相关的问题而创建的工具类
 * 提供资源获取的通用方法，避免每次都写重复的代码获取结果
 */

public class UiUtils {

    public static Context getContext(){
        return MyApplication.context;
    }

    public static Handler getHandler(){
        return MyApplication.handler;
    }

    //返回指定颜色的colorId对应的颜色值
    public static int getColor(int colorId){
        return getContext().getResources().getColor(colorId);
    }

    //返回指定viewId的view
    public static View getView(int viewId){
        View view=View.inflate(getContext(),viewId,null);
        return view;
    }

    public static String[] getStringArr(int strArrId){
        String[] stringArray=getContext().getResources().getStringArray(strArrId);
        return stringArray;
    }

    //将dp转化成px
    public static int dip2px(int dp){
        //获取手机密度
        float density=getContext().getResources().getDisplayMetrics().density;
        return (int) (density*dp+0.5);//实现4舍5入
    }

    public static int px2dip(int px){
        float density=getContext().getResources().getDisplayMetrics().density;
        return (int) (px/density+0.5);
    }
    //保证runnable的操作是在主线程中进行的
    public static void runOnUiThread(Runnable runnable) {
        if (isInMainThread()){
            runnable.run();
        }else {
            UiUtils.getHandler().post(runnable);//自动的发送runnable到主线程，进行UI处理
        }
    }
    private static boolean isInMainThread() {
        int currentThreadId=android.os.Process.myTid();
        return MyApplication.mainThreadId==currentThreadId;
    }

    public static void toast(String message,boolean isLengthLong){
        Toast.makeText(UiUtils.getContext(), message, isLengthLong?Toast.LENGTH_LONG:Toast.LENGTH_SHORT).show();
    }
}
