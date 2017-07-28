package com.king.capacityprice.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.king.capacityprice.R;
import com.king.capacityprice.base.BaseActivity;
import com.king.capacityprice.base.SMSManager;
import com.king.capacityprice.databinding.ActivityRegisterBinding;
import com.king.capacityprice.http.RequestCenter;
import com.king.capacityprice.http.ZCResponse;
import com.king.capacityprice.model.request.UserRegister;
import com.king.capacityprice.utils.CommonValues;
import com.king.capacityprice.utils.RegexpUtils;
import com.king.capacityprice.utils.StringUtil;
import com.king.capacityprice.utils.ToastUtil;
import com.king.capacityprice.utils.dialogUtils.DialogManager;
import com.king.capacityprice.utils.listener.TimeListener;
import com.king.capacityprice.viewmodel.UserRegisterViewModel;


/**
 * Created by king on 2017/3/21.
 * 注册界面
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener, TimeListener {
    private ActivityRegisterBinding mBinding;
    private UserRegister mUserRegister;
    private UserRegisterViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        getTopbar().setTitle(getString(R.string.text_register));
        mUserRegister = new UserRegister();
        mBinding.setUserRegister(mUserRegister);
        mViewModel = new UserRegisterViewModel(this);

        SpannableStringBuilder builder = StringUtil.lightStr(mBinding.tvArgee.getText().toString(), "使用条款", "隐私政策", getResources().getColor(R.color.bg_yzm));
        mBinding.tvArgee.setText(builder);

        SMSManager.getInstance().registerTimeListener(this);
        setListener();
    }

    private void setListener() {
        mBinding.tvGetcode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.clear:
                mBinding.etPhone.setText("");
                break;
            case R.id.tv_getcode:
                //获取验证码，校验手机号
                Toast.makeText(this, "正在发送", Toast.LENGTH_SHORT).show();
                mUserRegister.setClick(false);
                mViewModel.verifyMun(mUserRegister);
                break;
            case R.id.btn_register:
                if (!RegexpUtils.isEmail(mUserRegister.getEmail().trim())) {
                    ToastUtil.getInstance().toastInCenter(this, getString(R.string.tv_emailError));
                    return;
                }
                if (!mBinding.cbShow.isChecked()) {
                    ToastUtil.getInstance().toastInCenter(this, getString(R.string.tv_checked));
                    return;
                }
                //校验验证码
                mViewModel.verifyCode(mUserRegister, mViewModel, this);
                break;
            case R.id.tv_login:
                //有账号，去登录
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
    }

    @Override
    public boolean onSuccess(ZCResponse response, String requestUrl, String method) {
        DialogManager.getInstance().dissMissDialog();
        if (requestUrl.equals(RequestCenter.USER_ACTION) && method.equals(RequestCenter.REGISTER_METHOD)) {
            String msg = response.getMessage();
            String reason = response.getMainData().getString("userInfo");
            if (!TextUtils.equals(msg, CommonValues.isY)) {
                //用户名或邮箱重复
                Toast.makeText(this, reason, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                mViewModel.onResultCallback(mUserRegister);
                startActivity(new Intent(this, LoginActivity.class));
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
    public void onLastTimeNotify(int lastSecond) {
        if (lastSecond > 0) {
            mUserRegister.setGetAuthCode("已发送(" + lastSecond + "s)");
            mUserRegister.setClick(false);
        } else {
            mUserRegister.setGetAuthCode(getResources().getString(R.string.tv_getCode));
            mUserRegister.setClick(true);
        }
    }

    @Override
    public void onAbleNotify(boolean valuable) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onPageStart(this, "RegisterActivity");
        SMSManager.getInstance().stopTimer();
        mUserRegister.setGetAuthCode(getResources().getString(R.string.tv_getCode));
        mUserRegister.setClick(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPageStart(this, "RegisterActivity");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SMSManager.getInstance().unRegisterTimeListener(this);
    }
}
