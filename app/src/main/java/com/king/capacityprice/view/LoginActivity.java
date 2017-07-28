package com.king.capacityprice.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.czy.permission.Callback.PermissionListener;
import com.czy.permission.PermissionSteward;
import com.king.capacityprice.R;
import com.king.capacityprice.base.BaseActivity;
import com.king.capacityprice.databinding.ActivityLoginBinding;
import com.king.capacityprice.http.MyJSON;
import com.king.capacityprice.http.RequestCenter;
import com.king.capacityprice.http.ZCResponse;
import com.king.capacityprice.model.request.UserInfo;
import com.king.capacityprice.model.serializable.SUserInfo;
import com.king.capacityprice.utils.CommonValues;
import com.king.capacityprice.utils.dialogUtils.DialogManager;
import com.king.capacityprice.viewmodel.UserInfoViewModel;
import com.tencent.bugly.beta.Beta;

import java.util.List;


/**
 * Created by king on 2016/9/18.
 * 登录界面
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private ActivityLoginBinding mDataBinding;
    private UserInfo mUserInfo;
    private UserInfoViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mUserInfo = new UserInfo();
        mDataBinding.setUserInfo(mUserInfo);
        mViewModel = new UserInfoViewModel(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clear:
                mDataBinding.etPhone.setText("");
                break;
            case R.id.btn_login:
                DialogManager.getInstance().showProgressDialog(this, "正在登录");
                mViewModel.doLogin(mUserInfo, this);
                break;
            case R.id.btn_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.tv_forgetPwd:
                startActivity(new Intent(this, FindPwActivity.class));
//                Beta.checkUpgrade();
                break;
        }
    }

    @Override
    public boolean onSuccess(ZCResponse response, String requestUrl, String method) {
        DialogManager.getInstance().dissMissDialog();
        if (requestUrl.equals(RequestCenter.USER_ACTION) && method.equals(RequestCenter.LOGIN_METHOD)) {
            String msg = response.getMessage();
            if (!TextUtils.equals(msg, CommonValues.isY)) {
                Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                UserInfo userInfo = MyJSON.parseObject(response.getMainData().getString("userInfo"), UserInfo.class);
                mViewModel.onResultCallback(userInfo);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        }
        return super.onSuccess(response, requestUrl, method);
    }

    @Override
    public boolean onFail(Throwable error, String requestUrl, String method) {
        mViewModel.onFailCallback(error);
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onPageStart(this, "LoginActivity");
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPageEnd(this, "LoginActivity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
