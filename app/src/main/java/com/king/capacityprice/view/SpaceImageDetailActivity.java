package com.king.capacityprice.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.king.capacityprice.R;
import com.king.capacityprice.base.BaseActivity;
import com.king.capacityprice.utils.BitmapTools;
import com.king.capacityprice.utils.CommonValues;
import com.king.capacityprice.utils.SharedPreferencesUtil;
import com.king.capacityprice.utils.widget.view.SmoothImageView;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * 头像放大全屏
 */
public class SpaceImageDetailActivity extends Activity {
    private SmoothImageView imageView;
    private int mLocationX;
    private int mLocationY;
    private int mWidth;
    private int mHeight;
    private static String path = "/sdcard/myHead/head.png";// sd路径

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLocationX = getIntent().getIntExtra("locationX", 0);
        mLocationY = getIntent().getIntExtra("locationY", 0);
        mWidth = getIntent().getIntExtra("width", 0);
        mHeight = getIntent().getIntExtra("height", 0);

        imageView = new SmoothImageView(this);
        imageView.setOriginalInfo(mWidth, mHeight, mLocationX, mLocationY);
        imageView.transformIn();
        imageView.setImageResource(R.drawable.img_grayblank);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        setContentView(imageView);

        imageView.setImageBitmap(BitmapTools.getBitmap(path));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
    }

    @Override
    public void onBackPressed() {
        close();
    }

    /**
     * 关闭(释放资源)
     */
    private void close() {
        imageView.transformOut();
        imageView.setOnTransformListener(new SmoothImageView.TransformListener() {
            @Override
            public void onTransformComplete(int mode) {
                if (mode == 2) {
                    finish();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            overridePendingTransition(0, 0);
        }
    }

}

