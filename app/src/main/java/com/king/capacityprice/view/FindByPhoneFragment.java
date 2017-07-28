package com.king.capacityprice.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.king.capacityprice.R;
import com.king.capacityprice.base.SMSManager;
import com.king.capacityprice.databinding.FragmentFindbyphoneBinding;
import com.king.capacityprice.http.HttpCallBack;
import com.king.capacityprice.http.RequestCenter;
import com.king.capacityprice.http.ZCResponse;
import com.king.capacityprice.model.request.UserFindPwByPhone;
import com.king.capacityprice.utils.CommonValues;
import com.king.capacityprice.utils.dialogUtils.DialogManager;
import com.king.capacityprice.utils.listener.TimeListener;
import com.king.capacityprice.viewmodel.UserFindPwByPhoneViewModel;

/**
 * Created by king on 2017/3/21.
 * 找回密码--手机号
 */
public class FindByPhoneFragment extends Fragment implements View.OnClickListener, TimeListener, HttpCallBack {
    private FragmentFindbyphoneBinding mBinding;
    private UserFindPwByPhone mModel;
    private UserFindPwByPhoneViewModel mViewModel;
    private String msg;

    public static FindByPhoneFragment newInstance() {
        FindByPhoneFragment fragment = new FindByPhoneFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_findbyphone, container, false);
        mBinding = DataBindingUtil.bind(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mModel = new UserFindPwByPhone();
        mViewModel = new UserFindPwByPhoneViewModel(this.getActivity());
        mBinding.setUserFindPwByPhone(mModel);

        SMSManager.getInstance().registerTimeListener(this);

        setListener();

    }

    private void setListener() {
        mBinding.clear.setOnClickListener(this);
        mBinding.btnSubmit.setOnClickListener(this);
        mBinding.btnConfirm.setOnClickListener(this);
        mBinding.tvGetcode.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                this.getActivity().finish();
                break;
            case R.id.clear:
                mBinding.etPhone.setText("");
                break;
            case R.id.tv_getcode:
                //获取验证码，校验手机号
                Toast.makeText(this.getActivity(), "正在发送", Toast.LENGTH_SHORT).show();
                mModel.setClick(false);
                mViewModel.verifyMun(mModel);
                break;
            case R.id.btn_submit:
                //验证手机号
                DialogManager.getInstance().showProgressDialog(this.getActivity(), "请稍后");
                mViewModel.verifyPhone(mModel, this);
                break;
            case R.id.btn_confirm:
                //验证手机号
                mViewModel.verifyPhone(mModel, this);
                mViewModel.checkPw(mModel);
                if (TextUtils.equals(msg, CommonValues.isY)) {
                    DialogManager.getInstance().showProgressDialog(this.getActivity(), "正在修改，请稍后");
                    mViewModel.doModify(mModel, this);
                }
                break;
        }
    }

    @Override
    public boolean onSuccess(ZCResponse response, String requestUrl, String method) {
        DialogManager.getInstance().dissMissDialog();
        msg = response.getMessage();
        String reason = response.getMainData().getString("userInfo");
        //保存操作，校验手机号
        if (requestUrl.equals(RequestCenter.USER_ACTION) && method.equals(RequestCenter.GORGETPASSWORD_METHOD)) {
            if (!TextUtils.equals(msg, CommonValues.isY)) {
                Toast.makeText(this.getActivity(), reason, Toast.LENGTH_SHORT).show();
            } else {
                if (!mModel.isStatus()) {
                    mViewModel.verifyCode(mModel);
                }
            }
        }
        //确认修改
        if (requestUrl.equals(RequestCenter.USER_ACTION) && method.equals(RequestCenter.UPDATEPASSWORD_METHOD)) {
            //保存操作，校验手机号
            if (!TextUtils.equals(msg, CommonValues.isY)) {
                Toast.makeText(this.getActivity(), reason, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this.getActivity(), "修改成功", Toast.LENGTH_SHORT).show();
                mViewModel.onResultCallback(mModel);
                startActivity(new Intent(this.getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        }
        return false;
    }

    @Override
    public boolean onFail(Throwable error, String requestUrl, String method) {
        mViewModel.onFailCallback(error);
        return false;
    }

    @Override
    public void onLastTimeNotify(int lastSecond) {
        if (lastSecond > 0) {
            mModel.setGetAuthCode("已发送(" + lastSecond + "s)");
            mModel.setClick(false);
        } else {
            mModel.setGetAuthCode(getResources().getString(R.string.tv_getCode));
            mModel.setClick(true);
        }
    }

    @Override
    public void onAbleNotify(boolean valuable) {

    }

    @Override
    public void onResume() {
        super.onResume();
        SMSManager.getInstance().stopTimer();
        mModel.setGetAuthCode(getResources().getString(R.string.tv_getCode));
        mModel.setClick(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SMSManager.getInstance().unRegisterTimeListener(this);
    }
}
