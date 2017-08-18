package com.zxn.myp2pinvest.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zxn.myp2pinvest.R;
import com.zxn.myp2pinvest.activity.GestureEditActivity;
import com.zxn.myp2pinvest.activity.UserRegisterActivity;
import com.zxn.myp2pinvest.common.AppNetConfig;
import com.zxn.myp2pinvest.common.BaseActivity;
import com.zxn.myp2pinvest.common.BaseFragment;
import com.zxn.myp2pinvest.utils.UiUtils;

import butterknife.Bind;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 作者：zxn
 */
public class MoreFragment extends BaseFragment {

    @Bind(R.id.iv_title)
    ImageView ivTitle;
    @Bind(R.id.iv_setting)
    ImageView ivSetting;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_register)
    TextView tvRegister;
    @Bind(R.id.toggle_more)
    ToggleButton toggleMore;
    @Bind(R.id.tv_reset)
    TextView tvReset;
    @Bind(R.id.rl_more_contact)
    RelativeLayout rlMoreContact;
    @Bind(R.id.tv_more_fankui)
    TextView tvMoreFankui;
    @Bind(R.id.tv_share)
    TextView tvShare;
    @Bind(R.id.tv_about)
    TextView tvAbout;
    @Bind(R.id.tv_contact)
    TextView tvContact;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;


    public MoreFragment() {
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
        sp = this.getActivity().getSharedPreferences("secret_protect", Context.MODE_PRIVATE);
        editor = sp.edit();
        //用户注册
        userRegister();
        //获取当前的设置手势密码的状态
        getGestrueStatus();
        //设置手势密码
        setGestruePassword();
        //重置手势密码
        resetGestrue();
        //联系客服
        contactService();
        //用户反馈
        Fankui();
        //分享给好友
        shareToFriends();
    }

    private void shareToFriends() {
        tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // 分享时Notification的图标和文字  2.5.9以后的版本不     调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.app_name));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://www.atguigu.com");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/1.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("https://www.hao123.com/");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是hao123");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("https://www.hao123.com/");
        // 启动分享GUI
        oks.show(MoreFragment.this.getActivity());
    }

    private String department="不明确";
    private void Fankui() {
        tvMoreFankui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = View.inflate(MoreFragment.this.getActivity(), R.layout.view_fankui, null);
                final RadioGroup rgFankui= (RadioGroup) view.findViewById(R.id.rg_fankui);
                final EditText etFankuiContent = (EditText) view.findViewById(R.id.et_fankui_content);
                rgFankui.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                        RadioButton rb = (RadioButton) rgFankui.findViewById(checkedId);
                        department=rb.getText().toString().trim();
                    }
                });
                new AlertDialog.Builder(MoreFragment.this.getActivity())
                        .setView(view)
                        .setPositiveButton("发送", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //获取反馈的部门信息
                                String content = etFankuiContent.getText().toString();
                                //联网发送信息
                                String url= AppNetConfig.FEEDBACK;
                                AsyncHttpClient client = new AsyncHttpClient();
                                RequestParams params=new RequestParams();
                                params.put("department",department);
                                params.put("content",content);
                                client.post(url,params,new AsyncHttpResponseHandler(){
                                    @Override
                                    public void onSuccess(String content) {
                                        UiUtils.toast("发送反馈信息成功",false);
                                    }
                                    @Override
                                    public void onFailure(Throwable error, String content) {

                                        UiUtils.toast("联网反馈信息失败",false);
                                    }
                                });
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
    }
    private void contactService() {
        rlMoreContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MoreFragment.this.getActivity())
                        .setTitle("联系客服")
                        .setMessage("是否现在联系客服")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String phoneNumber = tvContact.getText().toString().trim();
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse("tel:" + phoneNumber));
                                MoreFragment.this.getActivity().startActivity(intent);

                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
    }

    private void resetGestrue() {
        tvReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = toggleMore.isChecked();
                if (checked) {
                    ((BaseActivity) MoreFragment.this.getActivity()).goToActivity(GestureEditActivity.class, null);
                } else {
                    UiUtils.toast("手势密码操作已关闭，请开启后再设置", false);
                }
            }
        });
    }

    private void getGestrueStatus() {
        boolean isOpen = sp.getBoolean("isOpened", false);
        toggleMore.setChecked(isOpen);
    }

    private void setGestruePassword() {
        toggleMore.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
//                    UiUtils.toast("开启了手势密码",false);
//                    editor.putBoolean("isOpened",true).commit();
                    String inputCode = sp.getString("inputCode", "");
                    if (TextUtils.isEmpty(inputCode)) {//之前没有设置过过手势密码
                        new AlertDialog.Builder(MoreFragment.this.getActivity())
                                .setTitle("设置手势密码")
                                .setMessage("是否现在设置手势密码")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        UiUtils.toast("现在设置手势密码", false);
                                        sp.edit().putBoolean("isOpened", true).commit();
                                        //开启一个新的activity
                                        ((BaseActivity) MoreFragment.this.getActivity()).goToActivity(GestureEditActivity.class, null);
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        UiUtils.toast("取消设置手势密码", false);
                                        sp.edit().putBoolean("isOpened", false).commit();
                                        toggleMore.setChecked(false);
                                    }
                                })
                                .show();
                    } else {
                        UiUtils.toast("开启手势密码", false);
                        sp.edit().putBoolean("isOpened", true).commit();
                    }

                } else {
                    UiUtils.toast("关闭了手势密码", false);
                    editor.putBoolean("isOpened", false).commit();
                }
            }
        });
    }

    private void userRegister() {
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) MoreFragment.this.getActivity()).goToActivity(UserRegisterActivity.class, null);
            }
        });
    }

    @Override
    protected void initTitle() {
        ivTitle.setVisibility(View.GONE);
        tvTitle.setText("更多");
        ivSetting.setVisibility(View.GONE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.more_fragment;
    }
}
