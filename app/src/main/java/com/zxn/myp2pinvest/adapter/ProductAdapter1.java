package com.zxn.myp2pinvest.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxn.myp2pinvest.R;
import com.zxn.myp2pinvest.bean.Product;
import com.zxn.myp2pinvest.ui.RoundProgress;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zxn on 2017-08-14.
 */

public class ProductAdapter1 extends MyBaseAdapter1 {

    /**
     * 通过构造器初始化集合数据
     * @param list
     */
    public ProductAdapter1(List list) {
        super(list);
    }

    @Override
    public View myGetView(int position, View convertView, ViewGroup parent) {
        ViewHolder hodler;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_product_list, null);
            hodler = new ViewHolder(convertView);
            convertView.setTag(hodler);
        } else {
            hodler = (ViewHolder) convertView.getTag();
        }
        //装配数据
        Product product = (Product) list.get(position);
        hodler.tvName.setText(product.name);
        hodler.tvMoney.setText(product.money);
        hodler.tvYearrate.setText(product.yearRate);
        hodler.tvSuodingdays.setText(product.suodingDays);
        hodler.tvMinmoney.setText(product.minTouMoney + "起");
        hodler.tvMinDays.setText(product.memberNum);
        hodler.rpProgresss.setProgress(Integer.parseInt(product.progress));
        return convertView;
    }
    static class ViewHolder {
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

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
