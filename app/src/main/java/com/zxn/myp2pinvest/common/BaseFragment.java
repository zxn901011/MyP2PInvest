package com.zxn.myp2pinvest.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.RequestParams;
import com.zxn.myp2pinvest.ui.LoadingPager;

import butterknife.ButterKnife;

/**
 * Created by zxn on 2017-08-12.
 */

public abstract class BaseFragment extends Fragment {

    private LoadingPager loadingPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        loadingPager=new LoadingPager(container.getContext()){
            @Override
            public int layoutId() {
                return getLayoutId();
            }

            @Override
            protected void onSuccess(LoadingPager.ResultState resultState, View view_success) {
                ButterKnife.bind(BaseFragment.this, view_success);
                //初始化title
                initTitle();
                //初始化数据
                initData(resultState.getContent());
            }

            @Override
            protected RequestParams params() {
                return getParams();
            }

            @Override
            protected String url() {
                return getUrl();
            }
        };
        return loadingPager;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);;
        show();
    }

    private void show() {
        loadingPager.show();
    }

    protected abstract RequestParams getParams();

    protected abstract String getUrl();


    //初始化界面的数据
    protected abstract void initData(String content);

    //初始化title
    protected abstract void initTitle();

    //提供布局
    public abstract int getLayoutId();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
