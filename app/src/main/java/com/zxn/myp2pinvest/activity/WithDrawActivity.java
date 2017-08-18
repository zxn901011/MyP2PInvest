package com.zxn.myp2pinvest.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zxn.myp2pinvest.R;
import com.zxn.myp2pinvest.common.BaseActivity;
import com.zxn.myp2pinvest.utils.UiUtils;

import butterknife.Bind;
import butterknife.OnClick;

public class WithDrawActivity extends BaseActivity {

    @Bind(R.id.iv_title)
    ImageView ivTitle;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_setting)
    ImageView ivSetting;
    @Bind(R.id.account_zhifubao)
    TextView accountZhifubao;
    @Bind(R.id.select_bank)
    RelativeLayout selectBank;
    @Bind(R.id.chongzhi_text)
    TextView chongzhiText;
    @Bind(R.id.view)
    View view;
    @Bind(R.id.et_input_money)
    EditText etInputMoney;
    @Bind(R.id.chongzhi_text2)
    TextView chongzhiText2;
    @Bind(R.id.textView5)
    TextView textView5;
    @Bind(R.id.btn_tixian)
    Button btnTixian;

    @Override
    protected void initTitle() {
        ivTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("提现");
        ivSetting.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.iv_title)
    public void back(View view){
        removeCurrentActivity();
    }
    @Override
    protected void initData() {
        etInputMoney.setClickable(false);
        etInputMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String withDrawMoney = etInputMoney.getText().toString().trim();
                if (TextUtils.isEmpty(withDrawMoney)){
                    btnTixian.setClickable(false);
                    btnTixian.setBackgroundResource(R.drawable.btn_02);
                }else {
                    btnTixian.setClickable(true);
                    btnTixian.setBackgroundResource(R.drawable.btn_01);
                }
            }
        });
    }

    @OnClick(R.id.btn_tixian)
    public void withDraw(View view){
        //要将提现的数额发送给后台，由后台连接第三方平台，完成金额的提现操作
        //提示用户信息
        UiUtils.toast("您的提现申请已被成功受理，审核通过后，24小时内，你的钱就会到账，请注意查收",false);
        UiUtils.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                removeCurrentActivity();
            }
        },2000);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_with_draw;
    }
}
