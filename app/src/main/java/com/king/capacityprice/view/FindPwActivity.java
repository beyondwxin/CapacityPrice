package com.king.capacityprice.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

import com.baidu.mobstat.StatService;
import com.king.capacityprice.R;
import com.king.capacityprice.base.BaseActivity;
import com.king.capacityprice.databinding.ActivityFindPwBinding;
import com.king.capacityprice.view.adapter.MyFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by king on 2017/3/21.
 * 找回密码界面
 */
public class FindPwActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {
    private ActivityFindPwBinding mBinding;
    List<Fragment> fragmentList;
    FindByPhoneFragment mPhoneFragment;
    FindByEmailFragment mEMailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_find_pw);
        getTopbar().setTitle(getString(R.string.tv_toptitle));

        initViews();
        setListener();
    }

    private void initViews() {
        mPhoneFragment = FindByPhoneFragment.newInstance();
        mEMailFragment = FindByEmailFragment.newInstance();
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(mPhoneFragment);
        fragmentList.add(mEMailFragment);
        mBinding.viewpager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
        mBinding.viewpager.setCurrentItem(0);
    }

    private void setListener() {
        mBinding.rgChoose.setOnCheckedChangeListener(this);
        mBinding.viewpager.setOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.rb_findpwbyphone:
                changeView(0);
                break;
            case R.id.rb_findpwbyemail:
                changeView(1);
                break;
        }
    }

    //手动设置ViewPager要显示的视图
    private void changeView(int desTab) {
        mBinding.viewpager.setCurrentItem(desTab, true);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                mBinding.rgChoose.check(R.id.rb_findpwbyphone);
                break;
            case 1:
                mBinding.rgChoose.check(R.id.rb_findpwbyemail);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onPageStart(this, "FindPwActivity");
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPageEnd(this, "FindPwActivity");
    }

}
