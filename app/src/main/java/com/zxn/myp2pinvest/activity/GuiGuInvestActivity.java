package com.zxn.myp2pinvest.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxn.myp2pinvest.R;
import com.zxn.myp2pinvest.common.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

public class GuiGuInvestActivity extends BaseActivity {

    @Bind(R.id.iv_title)
    ImageView ivTitle;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_setting)
    ImageView ivSetting;

    @Override
    protected void initTitle() {
        ivTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("关于硅谷理财");
        ivSetting.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.iv_title)
    public void back(View view){
        removeCurrentActivity();
    }
    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_gui_gu_invest;
    }
}
