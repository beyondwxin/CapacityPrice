package com.king.capacityprice.view;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.baidu.mobstat.StatService;
import com.king.capacityprice.R;
import com.king.capacityprice.base.BaseActivity;
import com.king.capacityprice.databinding.ActivityPersonalBinding;
import com.king.capacityprice.eventbus.HeadEvent;
import com.king.capacityprice.http.RequestCenter;
import com.king.capacityprice.model.serializable.SUserInfo;
import com.king.capacityprice.utils.ActionSheet;
import com.king.capacityprice.utils.BitmapTools;
import com.king.capacityprice.utils.CommonValues;
import com.king.capacityprice.utils.SharedPreferencesUtil;
import com.king.capacityprice.viewmodel.PersonalInfoModelView;
import com.king.capacityprice.zxing.QrUtils;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by king on 2016/11/18.
 * 个人资料界面
 */
public class PersonalActivity extends BaseActivity implements View.OnClickListener {
    private ActivityPersonalBinding mBinding;
    private PersonalInfoModelView modelView;
    private InputMethodManager imm;//软键盘管理
    private ActionSheet.Builder mBuilder;
    private String[] chooseItem = new String[]{"从图库中选取", "拍摄照片"};
    private Bitmap head;
    private static String path = "/sdcard/myHead/";// sd路径
    /**
     * 执行图库
     */
    private static final int GALLERY_REQUEST_CODE = 0x3;

    /**
     * 执行相机
     */
    private static final int CAMERA_REQUEST_CODE = 0x1;

    /**
     * 图片的操作
     */
    private static final int IMG_OPTION = 0x4;

    /**
     * 图片裁剪
     */
    private static final int PHOTO_REQUEST_CUT = 0x2;
    private static final int CODE_FOR_READ_PERMISSION = 1;
    private static final int CODE_FOR_WRITE_PERMISSION = 2;
    private static final int CODE_FOR_CAMERA_PERMISSION = 3;
    private Uri tempUri;
    private Uri saveUri;

    private String realPath;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int msgId = msg.what;
            switch (msgId) {
                case IMG_OPTION:
                    String result = msg.getData().getString("result");
                    saveUri = Uri.parse(result);
                    startPhotoZoom(saveUri);
                    break;
                case PHOTO_REQUEST_CUT:
                    head = msg.getData().getParcelable("result");
                    try {
//                        realPath = BitmapTools.getRealFilePath(PersonalActivity.this, saveUri);
//                        SharedPreferencesUtil.saveValue(PersonalActivity.this, CommonValues.URI_HEAD, realPath);
                        BitmapTools.setPicToView(path, head);//把得到的Bitmap转换为File用于上传
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mBinding.ivIcon.setImageBitmap(head);
                    EventBus.getDefault().post(new HeadEvent(realPath));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_personal);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        getTopbar().setTitle(R.string.tv_personInfo);

        modelView = PersonalInfoModelView.getmInstance();

        initData();

        QrUtils.createQRImage(CommonValues.APKDOWNLOADURL, this, mBinding.ivQr);

    }

    private void initData() {
//        Picasso.with(this).load("file:///" + SharedPreferencesUtil.getStringValue(this, CommonValues.URI_HEAD, "")).placeholder(R.drawable.img_grayblank).into(mBinding.ivIcon);
        mBinding.ivIcon.setImageBitmap(BitmapTools.getBitmap("/sdcard/myHead/head.png"));
        //网络加载 获取个人资料
//        modelView.getPersonalInfo(SUserInfo.getUserInfoInstance().getPhone(), this);
        mBinding.tvPhone.setText(SUserInfo.getUserInfoInstance().getMpNum());
        mBinding.tvEmail.setText(SUserInfo.getUserInfoInstance().getEmail());
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.iv_icon:
                Intent intent = new Intent(this, SpaceImageDetailActivity.class);
                intent.putExtra("images", head);//非必须
                int[] location = new int[2];
                mBinding.ivIcon.getLocationOnScreen(location);
                intent.putExtra("locationX", location[0]);//必须
                intent.putExtra("locationY", location[1]);//必须

                intent.putExtra("width", mBinding.ivIcon.getWidth());//必须
                intent.putExtra("height", mBinding.ivIcon.getHeight());//必须
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;
            case R.id.ll_my:
                //更换头像
                mBuilder = ActionSheet.createBuilder(this, getSupportFragmentManager()).setOtherButtonTitles(chooseItem).setCancelButtonTitle(R.string.tv_cancel).setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                        switch (index) {
                            case 0:
                                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                                //图库
                                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
                                break;
                            case 1:
                                ActivityCompat.requestPermissions(PersonalActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        CODE_FOR_WRITE_PERMISSION);
                                ActivityCompat.requestPermissions(PersonalActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                        CODE_FOR_READ_PERMISSION);
                                ActivityCompat.requestPermissions(PersonalActivity.this, new String[]{Manifest.permission.CAMERA},
                                        CODE_FOR_CAMERA_PERMISSION);
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                SimpleDateFormat timeStampFormat = new SimpleDateFormat(
                                        "yyyy_MM_dd_HH_mm_ss");
                                String filename = timeStampFormat.format(new Date());
                                ContentValues values = new ContentValues();
                                values.put(MediaStore.Audio.Media.TITLE, filename);

                                tempUri = getContentResolver().insert(
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                                startActivityForResult(intent, CAMERA_REQUEST_CODE);
                                break;
                        }
                    }
                });
                mBuilder.show();
                break;
            case R.id.btn_exit:
                SharedPreferencesUtil.saveValue(this, CommonValues.USERINFO, "");
                finish();
                goToLoginActivity();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //响应成功
        if (MainActivity.RESULT_OK != resultCode && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1)
            return;
        switch (requestCode) {
            case GALLERY_REQUEST_CODE:
                //相册
                Message galleryMessage = Message.obtain();
                galleryMessage.what = IMG_OPTION;
                Bundle galleryBundle = new Bundle();
                if (data != null) {
                    galleryBundle.putString("result", data.getData().toString());
                    galleryMessage.setData(galleryBundle);
                    handler.sendMessageDelayed(galleryMessage, 10);
                }
                break;

            case CAMERA_REQUEST_CODE:
                //拍照
                Uri uri = null;
                if (data != null && data.getData() != null) {
                    uri = data.getData();
                }
                //注意:尝试修改
                if (uri == null) {
                    if (tempUri != null) {
                        uri = tempUri;
                    }
                }
                Message cameraMessage = Message.obtain();
                cameraMessage.what = IMG_OPTION;
                Bundle cameraBundle = new Bundle();
                cameraBundle.putString("result", uri.toString());
                cameraMessage.setData(cameraBundle);
                handler.sendMessageDelayed(cameraMessage, 10);
                break;

            case PHOTO_REQUEST_CUT:
                Bitmap image = null;
                if (data != null) {
                    //取得返回的Uri,基本上选择照片的时候返回的是以Uri形式，但是在拍照中有得机子呢Uri是空的，所以要特别注意
                    Uri mImageCaptureUri = data.getData();
                    //返回的Uri不为空时，那么图片信息数据都会在Uri中获得。如果为空，那么我们就进行下面的方式获取
                    if (mImageCaptureUri != null) {
                        try {
                            //这个方法是根据Uri获取Bitmap图片的静态方法
                            image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageCaptureUri);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Bundle extras = data.getExtras();
                        if (extras != null) {
                            //这里是有些拍照后的图片是直接存放到Bundle中的所以我们可以从这里面获取Bitmap图片
                            image = extras.getParcelable("data");
                        }
                    }
                    Message cutMessage = Message.obtain();
                    cutMessage.what = PHOTO_REQUEST_CUT;
                    Bundle cutBundleData = new Bundle();
                    cutBundleData.putParcelable("result", image);
                    cutMessage.setData(cutBundleData);
                    handler.sendMessageDelayed(cutMessage, 10);
                    break;
                }
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 图片处理
     *
     * @param uri
     */
    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //设置是否裁剪
        intent.putExtra("crop", "true");
        //设置宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        //设置裁剪图片的宽高：影响效果
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra("return-data", true);
        intent.putExtra("output", realPath);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onPageStart(this, "PersonalActivity");
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPageEnd(this, "PersonalActivity");
    }
}
