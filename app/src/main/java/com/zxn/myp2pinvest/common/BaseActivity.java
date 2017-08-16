package com.zxn.myp2pinvest.common;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.zxn.myp2pinvest.bean.User;

import butterknife.ButterKnife;

/**
 * Created by zxn on 2017-08-15.
 */

public abstract class BaseActivity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        AppManager.newInstance().addActivity(this);
        initTitle();
        initData();
    }

    protected abstract void initTitle();

    protected abstract void initData();

    protected abstract int getLayoutId();

    public AsyncHttpClient client=new AsyncHttpClient();

    //启动新的activity
    public void goToActivity(Class Activity, Bundle bundle) {
        Intent intent = new Intent(this, Activity);
        if (bundle != null && bundle.size() > 0) {
            intent.putExtra("data", bundle);
        }
        startActivity(intent);
    }

    //销毁当前的activity的操作
    public void removeCurrentActivity() {
        AppManager.newInstance().removeCurrent();
    }
    //销毁所有的activity
    public void removeAll(){
        AppManager.newInstance().removeAll();
    }

    public void saveUser(User user){
        SharedPreferences sharedPreferences = this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("name",user.getName());
        editor.putString("imageurl",user.getImageurl());
        editor.putBoolean("iscredit",user.isCredit());
        editor.putString("phone",user.getPhone());
        editor.commit();//必须提交，否则保存不成功
    }

    //读取用户信息
    public User readUser(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        User user=new User();
        String name = sharedPreferences.getString("name", "");
        String phone = sharedPreferences.getString("phone", "");
        String imageurl = sharedPreferences.getString("imageurl", "");
        boolean iscredit = sharedPreferences.getBoolean("iscredit", false);
        user.setName(name);
        user.setImageurl(imageurl);
        user.setPhone(phone);
        user.setCredit(iscredit);
        return user;
    }
}
