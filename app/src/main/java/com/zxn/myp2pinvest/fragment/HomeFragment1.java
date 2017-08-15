/*
package com.zxn.myp2pinvest.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
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
import com.zxn.myp2pinvest.utils.UiUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

*
 * 作者：zxn


public class HomeFragment1 extends Fragment {


    @Bind(R.id.iv_title)
    ImageView ivTitle;
    @Bind(R.id.iv_setting)
    ImageView ivSetting;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.vp_home)
    ViewPager vpHome;
    @Bind(R.id.tv_protect)
    TextView tvProtect;
    @Bind(R.id.tv_salary)
    TextView tvSalary;
    @Bind(R.id.tv_interest)
    TextView tvInterest;
    @Bind(R.id.tv_home_yearrate)
    TextView tvHomeYearrate;
    @Bind(R.id.ll_point_group)
    LinearLayout llPointGroup;
    @Bind(R.id.tv_home)
    TextView tvHome;
    @Bind(R.id.btn_progress)
    RoundProgress btnProgress;
    private Index index;
    private Runnable runnable=new Runnable() {
        @Override
        public void run() {
            btnProgress.setMax(100);
            for (int i=0;i<currentProgress;i++){
                btnProgress.setProgress(i+1);
                SystemClock.sleep(30);//延时2.7秒
                //强制重绘
//                btnProgress.invalidate();//只有主线程中才这么写
                btnProgress.postInvalidate();//主线程和子线程也能这么写
            }

        }
    };
    private int currentProgress;

    public HomeFragment() {}

    @Override
    protected RequestParams getParams() {
        return null;
    }

    @Override
    protected String getUrl() {
        return null;
    }

    public void initData(String content) {
        index = new Index();
        AsyncHttpClient httpClient = new AsyncHttpClient();
        //访问的url
        String url = AppNetConfig.INDEX;
        httpClient.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                //响应成功，200响应码
                //解析json数据，FastJson
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
                currentProgress=Integer.parseInt(index.product.progress);
                new Thread(runnable).start();
                //在子线程中，实现进度的动态变化
                vpHome.setAdapter(new MyViewPagerAdapter());
                //加点点
                addPoint();
                vpHome.addOnPageChangeListener(new MyPageChangeListener());
                //用ViewPagerIndicator
//                cpHomeIndicator.setViewPager(vpHome);加点点
            }
            @Override
            public void onFailure(Throwable error, String content) {
                //响应失败
                Toast.makeText(UiUtils.getContext(), "联网获取数据失败", Toast.LENGTH_SHORT).show();
                super.onFailure(error, content);
            }
        });
    }

    public void initTitle() {
        ivTitle.setVisibility(View.GONE);
        tvTitle.setText("首页");
        ivSetting.setVisibility(View.GONE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.home_fragment;
    }

    class MyViewPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            List<Image> images = index.imageList;
            return images == null ? 0 : images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(getActivity());
            //1.imageview显示图片
            String imageUrl = index.imageList.get(position).IMAURL;
            LogUtil.e("图片的路径是:" + imageUrl);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//居中的效果
            Picasso.with(getActivity()).load(imageUrl)
                    .into(imageView);//使用picasso联网加载图片
            //2.imageview添加到容器中
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private void addPoint() {
        int size = index.imageList.size();
        llPointGroup.removeAllViews();//移除所有的红点
        for (int i = 0; i < size; i++) {
            ImageView imageView = new ImageView(context);
            //设置背景选择器
            imageView.setBackgroundResource(R.drawable.point_selector);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UiUtils.dip2px(10), UiUtils.dip2px(10));
            if (i == 0) {
                imageView.setEnabled(true);
            } else {
                imageView.setEnabled(false);
                params.leftMargin = UiUtils.dip2px(8);
            }
            imageView.setLayoutParams(params);
            llPointGroup.addView(imageView);
        }
    }

    //之前高亮的位置
    private int prePosition;

    class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }
        @Override
        public void onPageSelected(int position) {
            //把之前的变成灰色
            llPointGroup.getChildAt(prePosition).setEnabled(false);
            //把当前设置红色
            llPointGroup.getChildAt(position).setEnabled(true);
            prePosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }
}
            <android.support.v4.view.ViewPager
                android:id="@+id/vp_home"
                android:layout_width="match_parent"
                android:layout_height="match_parent">
            </android.support.v4.view.ViewPager>
            <LinearLayout
                android:id="@+id/ll_point_group"
                android:gravity="center"
                android:layout_gravity="center_horizontal|bottom"
                android:layout_marginBottom="5dp"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:orientation="horizontal" />
*/
