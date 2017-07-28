package com.king.capacityprice.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.baidu.mobstat.StatService;
import com.king.capacityprice.R;
import com.king.capacityprice.base.BaseActivity;
import com.king.capacityprice.utils.CommonValues;
import com.king.capacityprice.utils.SharedPreferencesUtil;
import com.king.capacityprice.view.adapter.GuideViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 欢迎页
 * Created by king on 2016/11/28.
 */
public class GuideActivity extends BaseActivity implements OnClickListener {

    private ViewPager vp;
    private GuideViewPagerAdapter adapter;
    private List<View> views;
    private ImageView mEnter;

    // 引导页图片资源
    private static final int[] pics = {R.layout.guid_view1,
            R.layout.guid_view2, R.layout.guid_view3};

    // 底部小点图片
    private ImageView[] dots;

    // 记录当前选中位置
    private int currentIndex = 0;
    public GestureDetector mGestureDetector;
    private int flaggingWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_guide);


        slipToMain();
        // 获取分辨率
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        flaggingWidth = dm.widthPixels / 3;

        views = new ArrayList<View>();

        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        dots = new ImageView[pics.length];
        // 初始化引导页视图列表
        for (int i = 0; i < pics.length; i++) {

            View view = LayoutInflater.from(this).inflate(pics[i], null);
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(true);//都设为灰色
            dots[i].setOnClickListener(this);
            dots[i].setTag(i);// 设置位置tag，方便取出与当前位置对应

            if (i == pics.length - 1) {
                mEnter = (ImageView) findViewById(R.id.btn_enter);
                mEnter.setTag("enter");
                mEnter.setOnClickListener(this);
            }
            views.add(view);
        }

        vp = (ViewPager) findViewById(R.id.vp_guide);
        // 初始化adapter
        adapter = new GuideViewPagerAdapter(views);
        vp.setAdapter(adapter);
        vp.setOnPageChangeListener(new PageChangeListener());

        currentIndex = 0;
        dots[currentIndex].setEnabled(false);//设置为白色，即选中状态

    }

    /**
     * 设置当前view
     *
     * @param position
     */
    private void setCurView(int position) {
        if (position < 0 || position >= pics.length) {
            return;
        }
        vp.setCurrentItem(position);
    }

    /**
     * 设置当前指示点
     *
     * @param position
     */
    private void setCurDot(int position) {
        if (position < 0 || position > pics.length || currentIndex == position) {
            return;
        }
        dots[position].setEnabled(false);
        dots[currentIndex].setEnabled(true);
        currentIndex = position;
    }

    @Override
    public void onClick(View v) {
        //进入主页
        if (v.getTag().equals("enter")) {
            enterMainActivity();
            return;
        }
        int position = (Integer) v.getTag();
        setCurView(position);
        setCurDot(position);
    }


    private void enterMainActivity() {
        Intent intent = new Intent(this,
                LoginActivity.class);
        startActivity(intent);
        SharedPreferencesUtil.putBoolean(this, CommonValues.FIRST_OPEN, true);
        finish();
    }

    private class PageChangeListener implements ViewPager.OnPageChangeListener {
        // 当滑动状态改变时调用
        @Override
        public void onPageScrollStateChanged(int position) {
            // arg0 ==1的时辰默示正在滑动，arg0==2的时辰默示滑动完毕了，arg0==0的时辰默示什么都没做。

        }

        // 当前页面被滑动时调用
        @Override
        public void onPageScrolled(int position, float arg1, int arg2) {
            // arg0 :当前页面，及你点击滑动的页面
            // arg1:当前页面偏移的百分比
            // arg2:当前页面偏移的像素位置

        }

        // 当新的页面被选中时调用
        @Override
        public void onPageSelected(int position) {
            // 设置底部小点选中状态
            mEnter.setVisibility(View.GONE);
            if (position == pics.length - 1) {
                mEnter.setVisibility(View.VISIBLE);
            }
            setCurDot(position);
        }

    }

    private void slipToMain() {
        mGestureDetector = new GestureDetector(this,
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2,
                                           float velocityX, float velocityY) {
                        if (currentIndex == 2) {
                            if ((e1.getRawX() - e2.getRawX()) >= flaggingWidth) {

                                Intent intent = new Intent(
                                        GuideActivity.this,
                                        LoginActivity.class);
                                startActivity(intent);
                                SharedPreferencesUtil.putBoolean(GuideActivity.this, CommonValues.FIRST_OPEN, true);
                                finish();
                                return true;
                            }
                        }
                        return false;
                    }

                });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event)) {
            event.setAction(MotionEvent.ACTION_CANCEL);
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onPageStart(this, "GuideActivity");
    }


    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPageEnd(this, "GuideActivity");
        // 如果切换到后台，就设置下次不进入功能引导页
        SharedPreferencesUtil.putBoolean(this, CommonValues.FIRST_OPEN, true);
        finish();
    }
}