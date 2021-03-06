package com.zxn.myp2pinvest.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zxn.myp2pinvest.R;
import com.zxn.myp2pinvest.common.AppNetConfig;
import com.zxn.myp2pinvest.common.BaseActivity;
import com.zxn.myp2pinvest.utils.MD5Utils;
import com.zxn.myp2pinvest.utils.UiUtils;

import butterknife.Bind;
import butterknife.OnClick;

public class UserRegisterActivity extends BaseActivity {

    @Bind(R.id.iv_title)
    ImageView ivTitle;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_setting)
    ImageView ivSetting;
    @Bind(R.id.et_register_number)
    EditText etRegisterNumber;
    @Bind(R.id.et_register_name)
    EditText etRegisterName;
    @Bind(R.id.et_register_pwd)
    EditText etRegisterPwd;
    @Bind(R.id.et_register_pwdagain)
    EditText etRegisterPwdagain;
    @Bind(R.id.btn_register)
    Button btnRegister;

    @Override
    protected void initTitle() {
        ivTitle.setVisibility(View.VISIBLE);
        ivSetting.setVisibility(View.INVISIBLE);
        tvTitle.setText("用户注册");
    }
    @OnClick(R.id.iv_title)
    public void back(View view){
        removeCurrentActivity();
    }

    @Override
    protected void initData() {
        //注册button的点击事件
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.获取用户注册的信息
                String name=etRegisterName.getText().toString().trim();
                String number=etRegisterNumber.getText().toString().trim();
                String password=etRegisterPwd.getText().toString().trim();
                String pwdAgain=etRegisterPwdagain.getText().toString().trim();
                //2.所填写的信息不能为空
                if (TextUtils.isEmpty(name)||TextUtils.isEmpty(number)||TextUtils.isEmpty(password)||TextUtils.isEmpty(pwdAgain)){
                    UiUtils.toast("填写信息不能为空",false);
                }else if (!password.equals(pwdAgain)){//两次密码必须一致
                    UiUtils.toast("两次密码不一致",false);
                }else {
                    //3.联网发送用户注册信息
                    String url = AppNetConfig.USER_REGISTER;
                    RequestParams params = new RequestParams();
                    params.put("name", name);
                    params.put("password", MD5Utils.MD5(password));
                    params.put("phone", number);
                    client.post(url, params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(String content) {
                            JSONObject jsonObject = JSON.parseObject(content);
                            Boolean isExist = jsonObject.getBoolean("isExist");
                            if (isExist) {
                                UiUtils.toast("此用户已注册", false);
                            } else {
                                UiUtils.toast("注册成功", false);
                            }
                        }
                        @Override
                        public void onFailure(Throwable error, String content) {
                            UiUtils.toast("联网请求失败", false);
                        }
                    });
                }
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_register;
    }
}
