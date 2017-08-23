package com.zxn.myp2pinvest.common;

/**
 * Created by zxn on 2017-08-10.
 * 配置网络请求相关的地址
 */

public class AppNetConfig {
    public static final String IPADDRESS="192.168.1.101";

    public static final String BASE_URL="http://"+IPADDRESS+":8080/P2PInvest/";

    public static final String PORDUCT=BASE_URL+"product";//访问全部理财产品的

    public static final String LOGIN=BASE_URL+"login";//用户登录的

    public static final String INDEX=BASE_URL+"index";//访问homeFragment

    public static final String USER_REGISTER=BASE_URL+"UserRegister";//用户注册的

    public static final String FEEDBACK=BASE_URL+"FeedBack";//用户反馈的

    public static final String UPDATE=BASE_URL+"update.json";//更新应用
}
