package com.zxn.myp2pinvest.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zxn.myp2pinvest.R;
import com.zxn.myp2pinvest.common.BaseActivity;
import com.zxn.myp2pinvest.fragment.HomeFragment;
import com.zxn.myp2pinvest.fragment.InvestFragment;
import com.zxn.myp2pinvest.fragment.MeFragment;
import com.zxn.myp2pinvest.fragment.MoreFragment;
import com.zxn.myp2pinvest.utils.UiUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    @Bind(R.id.fl_main)
    FrameLayout flMain;
    @Bind(R.id.iv_main_home)
    ImageView ivMainHome;
    @Bind(R.id.ll_main_home)
    LinearLayout llMainHome;
    @Bind(R.id.iv_main_invest)
    ImageView ivMainInvest;
    @Bind(R.id.ll_main_invest)
    LinearLayout llMainInvest;
    @Bind(R.id.iv_main_me)
    ImageView ivMainMe;
    @Bind(R.id.ll_main_me)
    LinearLayout llMainMe;
    @Bind(R.id.iv_main_more)
    ImageView ivMainMore;
    @Bind(R.id.ll_main_more)
    LinearLayout llMainMore;
    @Bind(R.id.tv_main_home)
    TextView tvMainHome;
    @Bind(R.id.tv_main_invest)
    TextView tvMainInvest;
    @Bind(R.id.tv_main_me)
    TextView tvMainMe;
    @Bind(R.id.tv_main_more)
    TextView tvMainMore;
    private HomeFragment homeFragment;
    private InvestFragment investFragment;
    private MeFragment meFragment;
    private MoreFragment moreFragment;
    private FragmentTransaction ft;


    @Override
    protected void initTitle() {
    }

    @Override
    protected void initData() {
        selectFragment(0);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @OnClick({R.id.ll_main_home, R.id.ll_main_invest, R.id.ll_main_me, R.id.ll_main_more})
    public void showTab(View view) {
//        Toast.makeText(this, "选择了具体的tab", Toast.LENGTH_SHORT).show();
        switch (view.getId()) {
            case R.id.ll_main_home:
                selectFragment(0);
                break;
            case R.id.ll_main_invest:
                selectFragment(1);
                break;
            case R.id.ll_main_me:
                selectFragment(2);
                break;
            case R.id.ll_main_more:
                selectFragment(3);
                break;
        }
    }

    //提供相应的fragment显示
    private void selectFragment(int i) {
        FragmentManager manager = this.getSupportFragmentManager();
        ft = manager.beginTransaction();
        //隐藏所有fragment的显示
        hideFragments();
        //重置ImageView和TextView的显示状态
        resetTab();
        switch (i) {
            case 0:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();//创建对象以后，并不会马上调用生命周期方法。而是在commit之后方才调用
                    ft.add(R.id.fl_main, homeFragment);
                }
                ft.show(homeFragment);
//                homeFragment.show();错误的写法
                //改变选中项的图片和文本颜色的变化
                ivMainHome.setImageResource(R.drawable.bottom02);
                tvMainHome.setTextColor(UiUtils.getColor(R.color.home_back_selected));
                break;
            case 1:
                if (investFragment == null) {
                    investFragment = new InvestFragment();
                    ft.add(R.id.fl_main, investFragment);
                }
                ft.show(investFragment);
                //改变选中项的图片和文本颜色的变化
                ivMainInvest.setImageResource(R.drawable.bottom04);
                tvMainInvest.setTextColor(UiUtils.getColor(R.color.home_back_selected));
                break;
            case 2:
                if (meFragment == null) {
                    meFragment = new MeFragment();
                    ft.add(R.id.fl_main, meFragment);
                }
                ft.show(meFragment);
                //改变选中项的图片和文本颜色的变化
                ivMainMe.setImageResource(R.drawable.bottom06);
                tvMainMe.setTextColor(UiUtils.getColor(R.color.home_back_selected01));
                break;
            case 3:
                if (moreFragment == null) {
                    moreFragment = new MoreFragment();
                    ft.add(R.id.fl_main, moreFragment);
                }
                ft.show(moreFragment);
                //改变选中项的图片和文本颜色的变化
                ivMainMore.setImageResource(R.drawable.bottom08);
                tvMainMore.setTextColor(UiUtils.getColor(R.color.home_back_selected));
                break;
        }
        ft.commit();//提交事务

    }

    private void resetTab() {
        ivMainHome.setImageResource(R.drawable.bottom01);
        ivMainInvest.setImageResource(R.drawable.bottom03);
        ivMainMe.setImageResource(R.drawable.bottom05);
        ivMainMore.setImageResource(R.drawable.bottom07);
        tvMainHome.setTextColor(UiUtils.getColor(R.color.home_back_unselected));
        tvMainInvest.setTextColor(UiUtils.getColor(R.color.home_back_unselected));
        tvMainMe.setTextColor(UiUtils.getColor(R.color.home_back_unselected));
        tvMainMore.setTextColor(UiUtils.getColor(R.color.home_back_unselected));

    }

    private void hideFragments() {
        if (homeFragment != null) {
            ft.hide(homeFragment);
        }
        if (investFragment != null) {
            ft.hide(investFragment);
        }
        if (meFragment != null) {
            ft.hide(meFragment);
        }
        if (moreFragment != null) {
            ft.hide(moreFragment);
        }
    }

    /**
     * onKeyUp(),记录两次点击，方可退出应用
     */
    private static final int RESET_BACK = 1;
    private boolean flag = true;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case RESET_BACK:
                    flag = true;
                    break;
            }
        }
    };


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && flag) {
            Toast.makeText(this, "再点击一次，退出当前应用", Toast.LENGTH_SHORT).show();
            flag = false;
            //发送延迟消息
            handler.sendEmptyMessageDelayed(RESET_BACK, 2000);
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    //为了避免内存泄漏
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        //操作方式1,移除所有的未被执行的消息
        handler.removeCallbacksAndMessages(null);
        //操作方式2
       // handler.removeMessages(RESET_BACK);
    }
}
