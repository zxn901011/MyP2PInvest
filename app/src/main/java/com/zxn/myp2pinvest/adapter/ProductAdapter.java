package com.zxn.myp2pinvest.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zxn.myp2pinvest.R;
import com.zxn.myp2pinvest.bean.Product;
import com.zxn.myp2pinvest.ui.RoundProgress;
import com.zxn.myp2pinvest.utils.LogUtil;
import com.zxn.myp2pinvest.utils.UiUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zxn on 2017-08-14.
 */

public class ProductAdapter extends BaseAdapter {
    private final List<Product> productList;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @Override
    public int getCount() {
        return productList == null ? 0 : productList.size()+1;
    }

    /**
     * 返回不同类型的item的个数
     * @return
     */
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    /**
     * 不同的position位置上，显示的具体的item的type值
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (position==3){
            return 0;
        }else {
            return 1;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LogUtil.e("parent=" + parent.getClass().toString());
        LogUtil.e("parent.getContext()==" + parent.getContext());
        int itemViewType=getItemViewType(position);
        if (itemViewType==0){
            TextView textView=new TextView(parent.getContext());
            textView.setText("友谊的小船，说翻就翻");
            textView.setTextColor(UiUtils.getColor(R.color.text_progress));
            textView.setTextSize(UiUtils.dip2px(10));
            return textView;
        }
        if (position>3){
            position--;
        }
        ViewHolder hodler;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_product_list, null);
            hodler = new ViewHolder(convertView);
            convertView.setTag(hodler);
        } else {
            hodler = (ViewHolder) convertView.getTag();
        }
        //装配数据
        Product product = productList.get(position);
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
