package com.zxn.myp2pinvest.adapter;

import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by zxn on 2017-08-14.
 */

public abstract class BaseHolder<T> {

    private View rootView;

    public View getRootView() {
        return rootView;
    }
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
        refreshData();
    }

    //装配过程
    protected abstract void refreshData();

    public BaseHolder(){
        rootView=initView();
        rootView.setTag(this);
        ButterKnife.bind(this,rootView);
    }
    protected abstract View initView();
}
