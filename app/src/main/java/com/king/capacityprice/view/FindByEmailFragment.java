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

import com.king.capacityprice.R;
import com.king.capacityprice.databinding.FragmentFindbyemailBinding;
import com.king.capacityprice.http.HttpCallBack;
import com.king.capacityprice.http.RequestCenter;
import com.king.capacityprice.http.ZCResponse;
import com.king.capacityprice.model.request.UserFindPwByEmail;
import com.king.capacityprice.utils.CommonValues;
import com.king.capacityprice.utils.RegexpUtils;
import com.king.capacityprice.utils.ToastUtil;
import com.king.capacityprice.utils.dialogUtils.DialogManager;
import com.king.capacityprice.viewmodel.UserFindPwByEmailViewModel;

/**
 * Created by king on 2017/3/21.
 * 找回密码--邮箱
 */
public class FindByEmailFragment extends Fragment implements View.OnClickListener, HttpCallBack {
    private FragmentFindbyemailBinding mBinding;
    private UserFindPwByEmail mModel;
    private UserFindPwByEmailViewModel mViewModel;

    private String email;//邮箱
    private String result;//激活码

    public static FindByEmailFragment newInstance() {
        FindByEmailFragment fragment = new FindByEmailFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_findbyemail, container, false);
        mBinding = DataBindingUtil.bind(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mModel = new UserFindPwByEmail();
        mViewModel = new UserFindPwByEmailViewModel(this.getActivity());
        mBinding.setUserFindPwByEmail(mModel);

        setListener();
    }

    private void setListener() {
        mBinding.clear.setOnClickListener(this);
        mBinding.btnSubmit.setOnClickListener(this);
        mBinding.tvGetcode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                this.getActivity().finish();
                break;
            case R.id.clear:
                mBinding.etEmail.setText("");
                break;
            case R.id.tv_getcode:
                if (!RegexpUtils.isEmail(mModel.getEmail().trim())) {
                    ToastUtil.getInstance().toastInCenter(this.getActivity(), getString(R.string.tv_emailError));
                    return;
                }
                DialogManager.getInstance().showProgressDialog(this.getActivity(), "正在获取...");
                mViewModel.getAVCode(mModel, this);
                break;
            case R.id.btn_submit:
                //下一步
                if (!mModel.isStatus()) {
                    mViewModel.check(result, email, mModel);
                } else {
                    DialogManager.getInstance().showProgressDialog(this.getActivity(), "正在修改，请稍后");
                    mViewModel.doModify(mModel, this);
                }
                break;
        }
    }

    @Override
    public boolean onSuccess(ZCResponse response, String requestUrl, String method) {
        DialogManager.getInstance().dissMissDialog();
        result = response.getMainData().getString("userInfo");
        email = response.getMainData().getString("email");
        //获取激活码
        if (requestUrl.equals(RequestCenter.USER_ACTION) && method.equals(RequestCenter.GETAVCODE_METHOD)) {
            if (TextUtils.equals(response.getMessage(), CommonValues.isY)) {
                mModel.setAvCode(result);
            } else {
                ToastUtil.getInstance().toastInCenter(this.getActivity(), result);
            }
        }

        //确认修改
        if (requestUrl.equals(RequestCenter.USER_ACTION) && method.equals(RequestCenter.UPDATEPASSWORD_METHOD)) {
            ToastUtil.getInstance().toastInCenter(this.getActivity(), "修改成功");
            mViewModel.onResultCallback(mModel);
            startActivity(new Intent(this.getActivity(), LoginActivity.class));
            getActivity().finish();
        }
        return false;
    }

    @Override
    public boolean onFail(Throwable error, String requestUrl, String method) {
        mViewModel.onFailCallback(error);
        return false;
    }
}
