package com.zxn.myp2pinvest.adapter;

import android.view.View;
import android.widget.TextView;

import com.zxn.myp2pinvest.R;
import com.zxn.myp2pinvest.bean.Product;
import com.zxn.myp2pinvest.ui.RoundProgress;
import com.zxn.myp2pinvest.utils.UiUtils;

import butterknife.Bind;

/**
 * Created by zxn on 2017-08-14.
 */

public class MyProductHolder extends BaseHolder<Product> {
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_money)
    TextView tvMoney;
    @Bind(R.id.tv_yearrate)
    TextView tvYearrate;
    @Bind(R.id.tv_suodingdays)
    TextView tvSuodingdays;
    @Bind(R.id.tv_minmoney)
    TextView tvMinmoney;
    @Bind(R.id.tv_minDays)
    TextView tvMinDays;
    @Bind(R.id.rp_progresss)
    RoundProgress rpProgresss;

    @Override
    protected void refreshData() {
        Product data = this.getData();
        //装数据
        tvName.setText(data.name);
        tvMoney.setText(data.money);
        tvYearrate.setText(data.yearRate);
        tvSuodingdays.setText(data.suodingDays);
        tvMinmoney.setText(data.minTouMoney + "起");
        tvMinDays.setText(data.memberNum);
        rpProgresss.setProgress(Integer.parseInt(data.progress));
    }

    @Override
    protected View initView() {
        return View.inflate(UiUtils.getContext(), R.layout.item_product_list, null);
    }
}
