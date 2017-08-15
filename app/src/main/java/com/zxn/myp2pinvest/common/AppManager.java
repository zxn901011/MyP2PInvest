package com.zxn.myp2pinvest.common;

import android.app.Activity;

import com.zxn.myp2pinvest.activity.MainActivity;

import java.util.Stack;

/**
 * Created by zxn on 2017-08-10.
 * 统一应用程序中所有的activity的栈管理（单例）
 * 涉及到activity的添加、删除当前、删除所有，返回栈大小的方法
 */

public class AppManager {

    /**
     * 饿汉式
     */
    private AppManager(){
    }

    private static AppManager appManager=new AppManager();

    public static AppManager newInstance(){
        return appManager;
    }

    private Stack<Activity> activityStack=new Stack<>();

    public void addActivity(Activity activity) {
        if (activity != null) {
            activityStack.push(activity);
        }
    }

    //删除指定的activity
    public void removeActivity(Activity activity){
        if (activity!=null){
            for (int i=activityStack.size()-1;i>=0;i--){
                Activity currentActivity=activityStack.get(i);
                if (currentActivity.getClass().equals(activity.getClass())){
                    currentActivity.finish();
                    activityStack.remove(i);
                }
            }
        }
    }

    //删除当前的activity

    public void removeCurrent(){
        //方法1
//        Activity activity=activityStack.get(activityStack.size()-1);
//        activity.finish();
//        activityStack.remove(activityStack.size()-1);

        //方法2
        Activity activity=activityStack.lastElement();
        activity.finish();
        activityStack.remove(activity);
    }

    //删除所有
    public void removeAll(){
        for (int i=activityStack.size()-1;i>=0;i--){
            Activity activity=activityStack.get(i);
            activity.finish();
            activityStack.remove(activity);
        }
    }

    //返回栈的大小
    public int size(){
        return activityStack.size();
    }

    public void add(BaseActivity mainActivity) {

    }
}
