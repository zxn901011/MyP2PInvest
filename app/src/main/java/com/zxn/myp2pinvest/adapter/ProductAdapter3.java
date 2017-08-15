package com.zxn.myp2pinvest.adapter;

import com.zxn.myp2pinvest.bean.Product;

import java.util.List;

/**
 * Created by zxn on 2017-08-14.
 */

public class ProductAdapter3 extends MyBaseAdapter3<Product> {


    /**
     * 通过构造器初始化集合数据
     * @param list
     */
    public ProductAdapter3(List<Product> list) {
        super(list);
    }

    @Override
    protected BaseHolder<Product> getHolder() {
        return new MyProductHolder();
    }
}
