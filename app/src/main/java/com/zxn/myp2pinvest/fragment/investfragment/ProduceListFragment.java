package com.zxn.myp2pinvest.fragment.investfragment;

import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.RequestParams;
import com.zxn.myp2pinvest.R;
import com.zxn.myp2pinvest.adapter.ProductAdapter;
import com.zxn.myp2pinvest.adapter.ProductAdapter3;
import com.zxn.myp2pinvest.bean.Product;
import com.zxn.myp2pinvest.common.AppNetConfig;
import com.zxn.myp2pinvest.common.BaseFragment;
import com.zxn.myp2pinvest.ui.MyTextView;

import java.util.List;

import butterknife.Bind;

/**
 * Created by zxn on 2017-08-13.
 * listview的使用，1.listview2.baseadapter3.item layout4.集合数据
 */

public class ProduceListFragment extends BaseFragment {

    @Bind(R.id.lv_product_list)
    ListView lv_product_list;
    @Bind(R.id.tv_product_title)
    MyTextView tvProductTitle;
    private List<Product> products;
    private ProductAdapter adapter;

    @Override
    protected RequestParams getParams() {
        return null;
    }

    @Override
    protected String getUrl() {
        return AppNetConfig.PORDUCT;
    }

    @Override
    protected void initData(String content) {
//        //使得当前的textview获取焦点，自定义控件已经默认有焦点了,跑马灯效果
//        tvProductTitle.setFocusable(true);
//        tvProductTitle.setFocusableInTouchMode(true);
//        tvProductTitle.requestFocus();
        JSONObject jsonObject= JSON.parseObject(content);
        boolean success=jsonObject.getBoolean("success");
        if (success){
//            JSONArray dataArray=jsonObject.getJSONArray("data");
            String data=jsonObject.getString("data");
            //获取集合数据
            products = JSON.parseArray(data, Product.class);
            //方式一，没有抽取
//            adapter=new ProductAdapter(products);
//            lv_product_list.setAdapter(adapter);
            //方式二，抽取了，抽取的力度不够
//            ProductAdapter1 productAdapter1=new ProductAdapter1(products);
//            lv_product_list.setAdapter(productAdapter1);
            //方式三，抽取了，但是没有使用ViewHolder，getView()优化的不够
//            ProductAdapter2 productAdapter2=new ProductAdapter2(products);
//            lv_product_list.setAdapter(productAdapter2);
            //方式四，抽取了，最好的方式
            ProductAdapter3 productAdapter3=new ProductAdapter3(products);
            lv_product_list.setAdapter(productAdapter3);
        }
    }

    @Override
    protected void initTitle() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_productlist;
    }

}
