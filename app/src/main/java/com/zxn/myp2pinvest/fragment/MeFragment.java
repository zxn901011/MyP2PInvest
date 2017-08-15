package com.zxn.myp2pinvest.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.zxn.myp2pinvest.R;
import com.zxn.myp2pinvest.activity.LoginActivity;
import com.zxn.myp2pinvest.bean.User;
import com.zxn.myp2pinvest.common.BaseActivity;
import com.zxn.myp2pinvest.common.BaseFragment;

import butterknife.Bind;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 作者：zxn
 */
public class MeFragment extends BaseFragment {

    @Bind(R.id.iv_title)
    ImageView ivTitle;
    @Bind(R.id.iv_setting)
    ImageView ivSetting;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.circle_image)
    CircleImageView circleImage;
    @Bind(R.id.rl_me_icon)
    RelativeLayout rlMeIcon;
    @Bind(R.id.tv_me_name)
    TextView tvMeName;
    @Bind(R.id.rl_me)
    RelativeLayout rlMe;
    @Bind(R.id.recharge)
    ImageView recharge;
    @Bind(R.id.withdraw)
    ImageView withdraw;
    @Bind(R.id.ll_touzi)
    TextView llTouzi;
    @Bind(R.id.ll_touzi_zhiguan)
    TextView llTouziZhiguan;
    @Bind(R.id.ll_zichan)
    TextView llZichan;

    public MeFragment() {
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
        isLogin();
    }

    private void isLogin() {
        SharedPreferences sp=this.getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String name = sp.getString("name", "");
        if (TextUtils.isEmpty(name)){
            //本地没有保存过用户信息，给出提示，要求用户登陆
            doLogin();
        }else {
            //已经登陆过，则加载用户的信息并显示
            doUser();
        }
    }

    private void doUser() {
        //1.读取本地保存的用户信息
        User user = ((BaseActivity) (this.getActivity())).readUser();
        //2.获取对象信息，并设置给相应的视图显示
        tvMeName.setText(user.getName());
        Picasso.with(this.getActivity()).load(user.getImageurl())
                .into(circleImage);

    }

    //给出提示，要求用户登陆
    private void doLogin() {
        new AlertDialog.Builder(this.getActivity())
                .setTitle("提示")
                .setMessage("您还没有登陆哦！亲")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        UiUtils.toast("进入登陆界面",false);
                        ((BaseActivity)MeFragment.this.getActivity()).goToActivity(LoginActivity.class,null);
                    }
                })
                .setCancelable(false)//用户不可以取消
                .show();
    }

    @Override
    protected void initTitle() {
        ivTitle.setVisibility(View.GONE);
        tvTitle.setText("我的资产");
        ivSetting.setVisibility(View.GONE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.me_fragment;
    }
    
}
