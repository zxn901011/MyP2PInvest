package com.zxn.myp2pinvest.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.viewpagerindicator.TabPageIndicator;
import com.zxn.myp2pinvest.R;
import com.zxn.myp2pinvest.common.BaseFragment;
import com.zxn.myp2pinvest.fragment.investfragment.ProduceHotFragment;
import com.zxn.myp2pinvest.fragment.investfragment.ProduceListFragment;
import com.zxn.myp2pinvest.fragment.investfragment.ProduceRecommendFragment;
import com.zxn.myp2pinvest.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 作者：zxn
 */
public class InvestFragment extends BaseFragment {

    @Bind(R.id.iv_title)
    ImageView ivTitle;
    @Bind(R.id.iv_setting)
    ImageView ivSetting;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.tabpage_invest)
    TabPageIndicator tabpageInvest;
    private MyViewPagerAdapter adapter;

//    private List<String> titleList=new ArrayList<>();

    public InvestFragment() {
    }

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
        //1.加载三个不同的fragment
        initFragments();
//        titleList.add("全部理财");
//        titleList.add("推荐理财");
//        titleList.add("热门理财");
        //2.viewpager中设置三个fragment的显示
        adapter = new MyViewPagerAdapter(getFragmentManager());
        viewPager.setAdapter(adapter);
        tabpageInvest.setViewPager(viewPager);
//        //将viewpager设置到tablayout中
//        tabLayout.setupWithViewPager(viewPager);
//        //设置滑动或者固定
//        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
//        tabLayout.setTabTextColors(ColorStateList.valueOf(Color.BLACK));
    }

    private List<Fragment> fragmentList = new ArrayList<>();

    //
    private void initFragments() {
        ProduceListFragment produceListFragment = new ProduceListFragment();
        ProduceRecommendFragment produceRecommendFragment = new ProduceRecommendFragment();
        ProduceHotFragment produceHotFragment = new ProduceHotFragment();
        fragmentList.add(produceListFragment);
        fragmentList.add(produceRecommendFragment);
        fragmentList.add(produceHotFragment);
    }

    @Override
    protected void initTitle() {
        ivTitle.setVisibility(View.GONE);
        tvTitle.setText("投资");
        ivSetting.setVisibility(View.GONE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.invest_fragment;
    }
    /**
     * 提供pagerAdapter的实现
     * 如果viewpager中加载的是fragment，则提供的Adapter可以继承具体的FragmentStatePgaerAdapter或者FragmentPagerAdapter
     * FragmentStatePgaerAdapter:如果viewpager中加载的fragment过多，会根据最近最少使用算法，实现内存中fragment的清理，避免内存的溢出
     * FragmentPagerAdapter：适用于加载fragment比较少的时候，系统不会清理已经加载的fragment
     */
    class MyViewPagerAdapter extends FragmentPagerAdapter {


        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }

        //提供tabPagerIndicator的显示内容
        @Override
        public CharSequence getPageTitle(int position) {
            return UiUtils.getStringArr(R.array.invest_tab)[position];
        }
    }
}
