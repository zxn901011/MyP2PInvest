package com.zxn.myp2pinvest.ui;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zxn.myp2pinvest.R;
import com.zxn.myp2pinvest.utils.UiUtils;

/**
 * Created by zxn on 2017-08-12.
 */

public abstract class LoadingPager extends FrameLayout {


    //1.定义4种不同的显示状态
    private static final int STATE_LOADING=1;
    private static final int STATE_ERROR=2;
    private static final int STATE_SUCCESS_NODATA=3;
    private static final int STATE_SUCCESS_WITH_DATA=4;

    private static int CURRENTSTATE=STATE_LOADING;//默认情况下，当前状态正在加载

    //2.提供四种不同的界面
    private View view_loading;
    private View view_error;
    private View view_nodata;
    private View view_success;
    private LayoutParams params;

    private Context mContext;

    public LoadingPager(@NonNull Context context) {
        this(context,null);
    }

    public LoadingPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingPager(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext=context;
        initView();
    }

    //初始化方法
    private void initView() {
        params=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        if (view_loading==null){
            view_loading= UiUtils.getView(R.layout.page_loading);
            //添加到当前的frameLayout中
            addView(view_loading,params);
        }
        if (view_error==null){
            view_error= UiUtils.getView(R.layout.page_error);
            //添加到当前的frameLayout中
            addView(view_error,params);
        }
        if (view_nodata==null){
            view_nodata= UiUtils.getView(R.layout.page_empty);
            //添加到当前的frameLayout中
            addView(view_nodata,params);
        }
        showSafePage();
    }


    //保证如下的操作在主线程中执行的，更新界面
    private void showSafePage() {
        UiUtils.runOnUiThread(new Runnable(){
            @Override
            public void run() {
                //保证run()是在主线程中进行的
                showPager();
            }
        });
    }

    private void showPager() {
        view_loading.setVisibility(CURRENTSTATE==STATE_LOADING?View.VISIBLE:View.INVISIBLE);
        view_error.setVisibility(CURRENTSTATE==STATE_ERROR?View.VISIBLE:View.INVISIBLE);
        view_nodata.setVisibility(CURRENTSTATE==STATE_SUCCESS_NODATA?View.VISIBLE:View.INVISIBLE);
        if (view_success==null){
//            view_success=UiUtils.getView(layoutId());//加载布局使用的是Application的context
            view_success=View.inflate(mContext,layoutId(),null);
            addView(view_success,params);
        }
        view_success.setVisibility(CURRENTSTATE==STATE_SUCCESS_WITH_DATA? View.VISIBLE: ViewPager.INVISIBLE);
    }

    public abstract int layoutId();

    private ResultState resultState;

    //在show方法中实现联网操作加载数据
    public void show(){
        String url=url();
        if (TextUtils.isEmpty(url)){
            resultState=ResultState.SUCCESS_WITH_DATA;
            resultState.setContent("");
            loadImage();//修改Current_state，并且决定加载哪个页面:showSafePager();
            return;
        }else {
            UiUtils.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                    asyncHttpClient.get(url(), params(), new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(String content) {
                            if (TextUtils.isEmpty(content)) {
                                //将响应的结果全部封装在枚举类中
                                resultState = ResultState.SUCCESS_NODATA;
                                resultState.setContent("");
                            } else {
                                resultState = ResultState.SUCCESS_WITH_DATA;
                                resultState.setContent(content);
                            }
                            loadImage();
                        }
                        @Override
                        public void onFailure(Throwable error, String content) {
                            resultState = ResultState.ERROR;
                            resultState.setContent("");
                            loadImage();
                        }
                    });
                }
            },1000);
        }
    }

    private void loadImage() {
        switch (resultState){
            case ERROR:
                CURRENTSTATE=STATE_ERROR;
                break;
            case SUCCESS_NODATA:
                CURRENTSTATE=STATE_SUCCESS_NODATA;
                break;
            case SUCCESS_WITH_DATA:
                CURRENTSTATE=STATE_SUCCESS_WITH_DATA;
                break;
        }
        //根据修改以后的CURRENTSTATE的值，更新视图的显示
        showSafePage();
        if (CURRENTSTATE==STATE_SUCCESS_WITH_DATA){
            //回调
            onSuccess(resultState,view_success);
        }
    }

    protected abstract void onSuccess(ResultState resultState, View view_success);

    //提供联网的请求参数
    protected abstract RequestParams params();

    //提供联网的请求地址
    protected abstract String url();

    /**
     * 提供枚举类，封装联网以后的状态值和数据
     * 加一个权限
     */
    public enum ResultState{
        ERROR(2),SUCCESS_NODATA(3),SUCCESS_WITH_DATA(4);//三个对象
        int state;
        ResultState(int state){
            this.state=state;
        }

        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

}
