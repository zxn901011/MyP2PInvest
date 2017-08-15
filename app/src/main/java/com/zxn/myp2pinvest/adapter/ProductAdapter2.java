package com.zxn.myp2pinvest.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.zxn.myp2pinvest.R;
import com.zxn.myp2pinvest.bean.Product;

import java.util.List;

/**
 * Created by zxn on 2017-08-14.
 */

public class ProductAdapter2 extends MyBaseAdapter2<Product> {

    /**
     * 通过构造器初始化集合数据
     * @param list
     */
    public ProductAdapter2(List list) {
        super(list);
    }

    @Override
    protected void setData(View convertView, Product product) {
        ((TextView)convertView.findViewById(R.id.tv_name)).setText(product.name);
    }

    @Override
    protected View initView(Context context) {
        return View.inflate(context, R.layout.item_product_list,null);
    }
}
