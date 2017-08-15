package com.zxn.myp2pinvest.fragment.investfragment;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.zxn.myp2pinvest.R;
import com.zxn.myp2pinvest.common.BaseFragment;
import com.zxn.myp2pinvest.ui.randomLayout.StellarMap;
import com.zxn.myp2pinvest.utils.UiUtils;

import java.util.Random;

import butterknife.Bind;

/**
 * Created by zxn on 2017-08-13.
 * 如何在布局中添加子视图
 * 1.直接在布局文件中，以标签的形式添加
 * 2.在java代码中，动态添加子视图
 * --->addView(),直接或间接的继承自ViewGroup，ViewGroup又实现了ViewManager
 * 3.设置adapter的方式来装配数据
 */

public class ProduceRecommendFragment extends BaseFragment {

    @Bind(R.id.stellar_map)
    StellarMap stellarMap;
    //提供装配的数据
    private String[] datas = new String[]{"新手福利计划", "财神道90天计划", "硅谷钱包计划", "30天理财计划(加息2%)", "180天理财计划(加息5%)", "月月升理财计划(加息10%)",
            "中情局投资商业经营", "大学老师购买车辆", "屌丝下海经商计划", "美人鱼影视拍摄投资", "Android培训老师自己周转", "养猪场扩大经营",
            "旅游公司扩大规模", "铁路局回款计划", "屌丝迎娶白富美计划"
    };

    private String[] oneDatas=new String[datas.length/2];
    private String[] twoDatas=new String[datas.length-datas.length/2];

    private Random random=new Random();
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
            if (i<datas.length/2){
                oneDatas[i]=datas[i];
            }else {
                twoDatas[i-datas.length/2]=datas[i];
            }
        }
        StellarAdapter adapter=new StellarAdapter();
        stellarMap.setAdapter(adapter);
        int leftPadding=UiUtils.dip2px(10);
        int topPadding=UiUtils.dip2px(10);
        int rightPadding=UiUtils.dip2px(10);
        int bottomPadding=UiUtils.dip2px(10);
        stellarMap.setInnerPadding(leftPadding,topPadding,rightPadding,bottomPadding);

        //必须调用如下的两个方法
        //设置显示在x，y轴上的稀疏度
        stellarMap.setRegularity(5,7);
        //设置初始化显示的组别，以及是否需要使用动画
        stellarMap.setGroup(0,true);
    }

    @Override
    protected void initTitle() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_productrecommend;
    }

    class StellarAdapter implements StellarMap.Adapter{
        @Override
        public int getGroupCount() {
            return 2;
        }

        //返回每组中显示的数据的个数
        @Override
        public int getCount(int group) {
            if (group==0){
                return datas.length/2;
            }else {
                return datas.length-datas.length/2;
            }
        }
        //返回一个具体的view
        //position：不同的组别，position都是从0开始的
        @Override
        public View getView(int group, int position, View convertView) {
            final TextView textView = new TextView(getActivity());
            if (group==0){
                textView.setText(oneDatas[position]);
            }else {
                textView.setText(twoDatas[position]);
            }

            //设置字体的大小
            textView.setTextSize(UiUtils.dip2px(5)+UiUtils.dip2px(random.nextInt(10)));
            //设置字体的颜色
            int red=random.nextInt(211);//00~ff;0-255
            int blue=random.nextInt(211);
            int green=random.nextInt(211);
            textView.setTextColor(Color.rgb(red,green,blue));

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UiUtils.toast(textView.getText().toString(),false);
                }
            });
            return textView;
        }
        //返回下一组显示平移动画的组别，查看源码发现此方法不会被调用，所以可以不重写
        @Override
        public int getNextGroupOnPan(int group, float degree) {

            return 0;
        }
        //返回下一组显示缩放动画的组别
        @Override
        public int getNextGroupOnZoom(int group, boolean isZoomIn) {
            if (0==group){
                return 1;
            }else {
                return 0;
            }
        }
    }
}
