package com.king.capacityprice.base;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.baidu.mobstat.SendStrategyEnum;
import com.baidu.mobstat.StatService;
import com.king.capacityprice.R;
import com.king.capacityprice.http.HttpCallBack;
import com.king.capacityprice.http.ZCResponse;
import com.king.capacityprice.view.ActionBarView;
import com.king.capacityprice.view.LoginActivity;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by king on 2016/9/18.
 */
public class BaseActivity extends AutoLayoutActivity implements HttpCallBack {
    protected ActionBarView topbarView;
    //应用是否销毁标志
    protected boolean isDestroy;
    //防止重复点击设置的标志，涉及到点击打开其他Activity时，将该标志设置为false，在onResume事件中设置为true
    private boolean clickable = true;

    //定义手势检测器实例
    GestureDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isDestroy = false;
        // 添加Activity到堆栈
        AppManager.getAppManager().addActivity(this);

        //创建手势检测器
        detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if ((e1.getRawY() - e2.getRawY()) > 150) {   //上滑
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });

        //配置百度统计
        stat();
    }


    public void stat() {
        // 打开调试开关，正式版本请关闭，以免影响性能
        StatService.setDebugOn(true);
        // 打开异常收集开关，默认收集java层异常，如果有嵌入SDK提供的so库，则可以收集native crash异常
        StatService.setOn(this, StatService.EXCEPTION_LOG);

        // 如果没有页面和事件埋点，此代码必须设置，否则无法完成接入
        // 设置发送策略，建议使用 APP_START
        // 发送策略，取值 取值 APP_START、SET_TIME_INTERVAL、ONCE_A_DAY
        // 备注，SET_TIME_INTERVAL与ONCE_A_DAY，如果APP退出或者进程死亡，则不会发送
        // 建议此代码不要在Application中设置，否则可能因为进程重启等造成启动次数高，具体见web端sdk帮助中心
        StatService.start(this);
    }


    /**
     * 跳转到登录
     */
    protected void goToLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    /**
     * 加载页面
     *
     * @param url
     * @param title
     */
    public void gotoWebView(String url, String title) {
        Intent intent = new Intent(this, BasicWebActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        startActivity(intent);
    }

    public ActionBarView getTopbar() {
        if (topbarView == null) {
            View view = findViewById(R.id.topbar_view);
            if (view != null) {
                topbarView = new ActionBarView(view);
            }
        }
        return topbarView;
    }

//    public UserDao getUserDao() {
//        return ((BaseApplication) this.getApplicationContext()).getDaoSession().getUserDao();
//    }
//
//    public AcademyDao getAcademyDao() {
//        return ((BaseApplication) this.getApplicationContext()).getDaoSession().getAcademyDao();
//    }

//    public SQLiteDatabase getDb() {
//        return ((BaseApplication) this.getApplicationContext()).getDb();
//    }

    /**
     * 当前是否可以点击
     *
     * @return
     */
    protected boolean isClickable() {
        return clickable;
    }

    /**
     * 锁定点击
     */
    protected void lockClick() {
        clickable = false;
    }


    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        if (isClickable()) {
            lockClick();
            super.startActivityForResult(intent, requestCode, options);
        }
    }

    @Override
    public boolean onSuccess(ZCResponse response, String requestUrl, String method) {
        return false;
    }

    @Override
    public boolean onFail(Throwable error, String requestUrl, String method) {
        return false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroy = true;
        // 结束Activity&从堆栈中移除
        AppManager.getAppManager().finishActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //每次返回界面时，将点击标志设置为可点击
        clickable = true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        return super.dispatchTouchEvent(event);


    }
}
