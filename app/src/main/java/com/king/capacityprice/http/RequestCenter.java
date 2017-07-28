package com.king.capacityprice.http;


import com.king.capacityprice.model.request.UserFindPwByEmail;
import com.king.capacityprice.model.request.UserFindPwByPhone;
import com.king.capacityprice.model.request.UserInfo;
import com.king.capacityprice.model.request.UserRegister;
import com.king.capacityprice.model.serializable.SUserInfo;
import com.king.capacityprice.utils.DateUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by king on 16/9/20.
 */
public class RequestCenter {
    public static final String USER_ACTION = "user";//用户相关
    public static final String LOGIN_METHOD = "test.do";//用户登录
    public static final String REGISTER_METHOD = "registerApp.mvc";//用户注册
    public static final String GETAVCODE_METHOD = "sendmailApp.mvc";//忘记密码-邮箱获取激活码
    public static final String GORGETPASSWORD_METHOD = "checkphoneApp.mvc";//忘记密码-验证手机号
    public static final String UPDATEPASSWORD_METHOD = "updatepasswordApp.mvc";//忘记密码-确认修改
    public static final String CHANGE_PASSWORD_METHOD = "resetPassword";//修改密码
    public static final String SELECTALL_METHOD = "selectAllApp.mvc";//查询运价
    public static final String PERSONAL_INFO = "change/avator";//获取个人资料
    public static final String EXIT_APP = "exitApp";//退出登录

    /**
     * 登录
     *
     * @param userInfo
     * @param callBack
     */
    public static void login(UserInfo userInfo, HttpCallBack callBack) {
        ZCRequest request = new ZCRequest();
        request.setMethod(LOGIN_METHOD);
        request.setAction(USER_ACTION);
        request.putParams("mpNum", userInfo.getMpNum());
        request.putParams("password", userInfo.getPassword());
        OkHttpUtil.getInstance().okHttpPost(request, null, callBack);
    }

    /**
     * 注册
     *
     * @param userRegister
     * @param callBack
     */
    public static void requestRegister(UserRegister userRegister, HttpCallBack callBack) {
        ZCRequest request = new ZCRequest();
        request.setMethod(REGISTER_METHOD);
        request.setAction(USER_ACTION);
        request.putParams("mp_num", userRegister.getPhone());
        request.putParams("mp_verification_code", userRegister.getYzm());
        request.putParams("email", userRegister.getEmail());
        request.putParams("password", userRegister.getPassword());
        OkHttpUtil.getInstance().okHttpPost(request, null, callBack);
    }

    /**
     * 忘记密码--通过手机号保存操作校验手机号
     *
     * @param findPwByPhone
     */
    public static void forgetPwByPhone(UserFindPwByPhone findPwByPhone, HttpCallBack callBack) {
        ZCRequest request = new ZCRequest();
        request.setMethod(GORGETPASSWORD_METHOD);
        request.setAction(USER_ACTION);
        request.putParams("mp_num", findPwByPhone.getPhone());
        OkHttpUtil.getInstance().okHttpPost(request, null, callBack);
    }

    /**
     * 忘记密码--获取邮箱激活码
     *
     * @param findPwByEmail
     */
    public static void forgetPwByEmailGetAVCode(UserFindPwByEmail findPwByEmail, HttpCallBack callBack) {
        ZCRequest request = new ZCRequest();
        request.setMethod(GETAVCODE_METHOD);
        request.setAction(USER_ACTION);
        request.putParams("mail", findPwByEmail.getEmail());
        OkHttpUtil.getInstance().okHttpPost(request, null, callBack);
    }

    /**
     * 忘记密码--通过手机号确认修改
     *
     * @param findPwByPhone
     */
    public static void modifyByPhone(UserFindPwByPhone findPwByPhone, HttpCallBack callBack) {
        ZCRequest request = new ZCRequest();
        request.setMethod(UPDATEPASSWORD_METHOD);
        request.setAction(USER_ACTION);
        request.putParams("phone", findPwByPhone.getPhone());
        request.putParams("password", findPwByPhone.getPassword());
        OkHttpUtil.getInstance().okHttpPost(request, null, callBack);
    }

    /**
     * 忘记密码--通过邮箱确认修改
     *
     * @param findPwByEmail
     */
    public static void modifyByEmail(UserFindPwByEmail findPwByEmail, HttpCallBack callBack) {
        ZCRequest request = new ZCRequest();
        request.setMethod(UPDATEPASSWORD_METHOD);
        request.setAction(USER_ACTION);
        request.putParams("email", findPwByEmail.getEmail());
        request.putParams("password", findPwByEmail.getPassword());
        OkHttpUtil.getInstance().okHttpPost(request, null, callBack);
    }


    /**
     * 查询运价
     */
    public static void selectAll(String userId, String time, String startAddr, String endAddr, String sLongitude, String sLatitude, String eLongitude, String eLatitude, String distance, HttpCallBack callBack) {
        ZCRequest request = new ZCRequest();
        request.setMethod(SELECTALL_METHOD);
        request.setAction(USER_ACTION);
        request.putParams("user_id", userId);
        request.putParams("start_address", startAddr);
        request.putParams("end_address", endAddr);
        request.putParams("statrjd", sLongitude);
        request.putParams("statrwd", sLatitude);
        request.putParams("endjd", eLongitude);
        request.putParams("endwd", eLatitude);
        request.putParams("distancelimt", distance);
        request.putParams("inquery_time", time);
        OkHttpUtil.getInstance().okHttpPost(request, null, callBack);
    }

    /**
     * 退出
     *
     * @param userInfo
     * @param callBack
     */
    public static void exit(UserInfo userInfo, HttpCallBack callBack) {
        ZCRequest request = new ZCRequest();
        request.setMethod(EXIT_APP);
        request.setAction(USER_ACTION);
        request.putParams("phone", userInfo.getMpNum());
        OkHttpUtil.getInstance().okHttpPost(request, null, callBack);
    }
//
//    /**
//     * 获取个人资料
//     *
//     * @param head     头像
//     * @param callBack
//     */
//    public static void getPersonalInfo(Map<String, File> head, HttpCallBack callBack) {
//        Map<String, String> params = new HashMap<>();
//        params.put("phone", SUserInfo.getUserInfoInstance().getMpNum());
//        OkHttpUtil.getInstance().okHttpPost(PERSONAL_INFO, params, head, null, callBack);
//    }
}
