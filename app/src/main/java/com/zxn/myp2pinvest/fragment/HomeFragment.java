package com.zxn.myp2pinvest.fragment;

import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;
import com.zxn.myp2pinvest.R;
import com.zxn.myp2pinvest.bean.Image;
import com.zxn.myp2pinvest.bean.Index;
import com.zxn.myp2pinvest.bean.Product;
import com.zxn.myp2pinvest.common.AppNetConfig;
import com.zxn.myp2pinvest.common.BaseFragment;
import com.zxn.myp2pinvest.ui.RoundProgress;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;

/**
 * 作者：zxn
 */
public class HomeFragment extends BaseFragment {

    @Bind(R.id.iv_title)
    ImageView ivTitle;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_setting)
    ImageView ivSetting;
    @Bind(R.id.tv_home)
    TextView tvHome;
    @Bind(R.id.tv_protect)
    TextView tvProtect;
    @Bind(R.id.tv_salary)
    TextView tvSalary;
    @Bind(R.id.tv_interest)
    TextView tvInterest;
    @Bind(R.id.btn_progress)
    RoundProgress btnProgress;
    @Bind(R.id.tv_home_yearrate)
    TextView tvHomeYearrate;
    @Bind(R.id.banner)
    Banner banner;
    private Index index;
    private int currentProgress;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            btnProgress.setMax(100);
            for (int i = 0; i < currentProgress; i++) {
                btnProgress.setProgress(i + 1);
                SystemClock.sleep(30);//延时2.7秒
                //强制重绘
//                btnProgress.invalidate();//只有主线程中才这么写
                btnProgress.postInvalidate();//主线程和子线程都能这么写
            }
        }
    };

    @Override
    protected RequestParams getParams() {
//        return new RequestParams();
        return null;
    }

    @Override
    protected String getUrl() {
        return AppNetConfig.INDEX;
    }

    @Override
    protected void initData(String content) {
        //响应成功，200响应码
        if (!TextUtils.isEmpty(content)) {
            index = new Index();
            //解析json数据，fastJson
            JSONObject jsonObject = JSON.parseObject(content);
            String proInfo = jsonObject.getString("proInfo");
            Product product = JSON.parseObject(proInfo, Product.class);
            String imageArr = jsonObject.getString("imageArr");
            List<Image> images = JSONObject.parseArray(imageArr, Image.class);
            index.product = product;
            index.imageList = images;
            //更新页面数据
            tvHome.setText(product.name);
            tvHomeYearrate.setText(product.yearRate + "%");
            //获取进度中的进度值
            currentProgress = Integer.parseInt(index.product.progress);
            //在子线程中，实现进度的动态变化
            new Thread(runnable).start();
            //设置banner样式
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
            //设置图片加载器
            banner.setImageLoader(new GlideImageLoader());
            //设置图片地址集合
            ArrayList<String> imageUrl = new ArrayList<>(index.imageList.size());
            for (int i = 0; i < index.imageList.size(); i++) {
                imageUrl.add(index.imageList.get(i).IMAURL);
            }
            banner.setImages(imageUrl);
            //设置banner动画效果
            banner.setBannerAnimation(Transformer.DepthPage);
            //设置标题集合（当banner样式有显示title时）
            String[] titles = new String[]{"分享砍学费", "人脉总动员", "想不到你是这样的App", "购物节狂欢"};
            banner.setBannerTitles(Arrays.asList(titles));
            //设置自动轮播，默认为true
            banner.isAutoPlay(true);
            //设置轮播时间
            banner.setDelayTime(2000);
            //设置指示器位置（当banner模式中有指示器时）
            banner.setIndicatorGravity(BannerConfig.CENTER);
            //banner设置方法全部调用完毕时最后调用
            banner.start();
        }
    }

    @Override
    protected void initTitle() {
        ivTitle.setVisibility(View.GONE);
        tvTitle.setText("首页");
        ivSetting.setVisibility(View.GONE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.home_fragment;
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            //Picasso 加载图片简单用法
            Picasso.with(context).load((String) path).into(imageView);
        }
    }
}
