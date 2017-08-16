package com.zxn.myp2pinvest.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zxn.myp2pinvest.R;
import com.zxn.myp2pinvest.bean.User;
import com.zxn.myp2pinvest.common.AppNetConfig;
import com.zxn.myp2pinvest.common.BaseActivity;
import com.zxn.myp2pinvest.utils.MD5Utils;
import com.zxn.myp2pinvest.utils.UiUtils;

import butterknife.Bind;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @Bind(R.id.iv_title)
    ImageView ivTitle;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_setting)
    ImageView ivSetting;
    @Bind(R.id.tv_login_number)
    TextView tvLoginNumber;
    @Bind(R.id.et_login_number)
    EditText etLoginNumber;
    @Bind(R.id.rl_login)
    RelativeLayout rlLogin;
    @Bind(R.id.tv_login_pwd)
    TextView tvLoginPwd;
    @Bind(R.id.et_login_pwd)
    EditText etLoginPwd;
    @Bind(R.id.btn_login)
    Button btnLogin;

    @Override
    protected void initTitle() {
        ivTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("用户登陆");
        ivSetting.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.iv_title)
    public void back(View view){
        removeAll();
        goToActivity(MainActivity.class,null);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @OnClick(R.id.btn_login)
    public void login(View view){//登陆按钮的点击事件
        final String number= etLoginNumber.getText().toString().trim();
        String password = etLoginPwd.getText().toString().trim();
        if (!TextUtils.isEmpty(number)&&!TextUtils.isEmpty(password)){
            String url= AppNetConfig.LOGIN;
            RequestParams params=new RequestParams();
            params.put("phone",number);
            params.put("password", MD5Utils.MD5(password));
            client.post(url,params,new AsyncHttpResponseHandler(){
                @Override
                public void onSuccess(String content) {//200 404
                    //解析json
                    JSONObject jsonObject= JSON.parseObject(content);
                    boolean success=jsonObject.getBoolean("success");
                    if (success){
                        String data = jsonObject.getString("data");
                        User user = JSON.parseObject(data, User.class);
                        saveUser(user);//保存用户信息
                        //重新加载界面
                        removeAll();
                        goToActivity(MainActivity.class,null);
                    }else {
                        Toast.makeText(LoginActivity.this, "用户名不存在或密码不正确", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Throwable error, String content) {
                    UiUtils.toast("联网失败",false);
                }
            });
        }else {
            UiUtils.toast("用户名和密码不能为空",false);
        }
    }
}
