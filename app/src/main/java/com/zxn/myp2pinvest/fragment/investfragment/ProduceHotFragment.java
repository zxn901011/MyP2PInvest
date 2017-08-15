package com.zxn.myp2pinvest.fragment.investfragment;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.zxn.myp2pinvest.R;
import com.zxn.myp2pinvest.common.BaseFragment;
import com.zxn.myp2pinvest.ui.FlowLayout;
import com.zxn.myp2pinvest.utils.DrawableUtils;
import com.zxn.myp2pinvest.utils.UiUtils;

import java.util.Random;

import butterknife.Bind;

/**
 * Created by zxn on 2017-08-13.
 */

public class ProduceHotFragment extends BaseFragment {

    @Bind(R.id.flow_hot)
    FlowLayout flowHot;

    private String[] datas = new String[]{"新手福利计划", "财神道90天计划", "硅谷计划", "30天理财计划", "180天理财计划", "月月升", "中情局投资商业经营", "大学老师购买车辆", "屌丝下海经商计划", "美人鱼影视拍摄投资", "Android培训老师自己周转", "养猪场扩大经营",
            "旅游公司扩大规模", "摩托罗拉洗钱计划", "铁路局回款计划", "屌丝迎娶白富美计划"
    };

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
        for (int i=0;i<datas.length;i++){
            final TextView textView=new TextView(getContext());
            textView.setText(datas[i]);
            ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
            marginLayoutParams.leftMargin= UiUtils.dip2px(5);
            marginLayoutParams.topMargin= UiUtils.dip2px(5);
            marginLayoutParams.rightMargin= UiUtils.dip2px(5);
            marginLayoutParams.bottomMargin= UiUtils.dip2px(5);
            textView.setLayoutParams(marginLayoutParams);//设置边距
            int padding=UiUtils.dip2px(5);
            textView.setPadding(padding,padding,padding,padding);//设置内边距
            textView.setTextSize(UiUtils.dip2px(8));
            Random random=new Random();
            int red=random.nextInt(211);
            int green=random.nextInt(211);
            int blue=random.nextInt(211);
//            textView.setBackground(DrawableUtils.getDrawable(Color.rgb(red,green,blue),UiUtils.dip2px(5)));设置单一背景
            //设置具有选择器功能的背景
            textView.setBackground(DrawableUtils.getSelector(DrawableUtils.getDrawable(Color.rgb(red,green,blue),UiUtils.dip2px(5)),DrawableUtils.getDrawable(Color.WHITE,UiUtils.dip2px(5))));
            //设置textView是可点击的
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UiUtils.toast(textView.getText().toString(),false);
                }
            });
            flowHot.addView(textView);
        }
    }

    @Override
    protected void initTitle() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_producthot;
    }

}
