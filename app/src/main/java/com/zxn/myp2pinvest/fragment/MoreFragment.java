package com.zxn.myp2pinvest.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.zxn.myp2pinvest.R;
import com.zxn.myp2pinvest.common.BaseFragment;

import butterknife.Bind;

/**
 * 作者：zxn
 */
public class MoreFragment extends BaseFragment {

    @Bind(R.id.iv_title)
    ImageView ivTitle;
    @Bind(R.id.iv_setting)
    ImageView ivSetting;
    @Bind(R.id.tv_title)
    TextView tvTitle;

    public MoreFragment() {
    }

    @Override
    protected RequestParams getParams() {
        return null;
    }

    @Override
    protected String getUrl() {
        return null;
    }

    @Override
    protected void initData(String content) {
    }
    @Override
    protected void initTitle() {
        ivTitle.setVisibility(View.GONE);
        tvTitle.setText("更多");
        ivSetting.setVisibility(View.GONE);
    }
    @Override
    public int getLayoutId() {
        return R.layout.more_fragment;
    }
}
