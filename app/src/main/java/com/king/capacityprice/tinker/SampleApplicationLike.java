/*
 * Tencent is pleased to support the open source community by making Tinker available.
 *
 * Copyright (C) 2016 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the BSD 3-Clause License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.king.capacityprice.tinker;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.widget.Toast;

import com.fm.openinstall.OpenInstall;
import com.king.capacityprice.R;
import com.king.capacityprice.utils.ChannelUtil;
import com.king.capacityprice.utils.LruCacheUtils;
import com.king.capacityprice.utils.NetUtil;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.interfaces.BetaPatchListener;
import com.tencent.bugly.beta.upgrade.UpgradeStateListener;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.zhy.autolayout.config.AutoLayoutConifg;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * because you can not use any other class in your application, we need to
 * As Application, all its direct reference class should be in the main dex.
 * <p>
 * We use tinker-android-anno to make sure all your classes can be patched.
 * <p>
 * application: if it is start with '.', we will add SampleApplicationLifeCycle's package name
 * <p>
 * flags:
 * TINKER_ENABLE_ALL: support dex, lib and resource
 * TINKER_DEX_MASK: just support dex
 * TINKER_NATIVE_LIBRARY_MASK: just support lib
 * TINKER_RESOURCE_MASK: just support resource
 * <p>
 * loaderClass: define the tinker loader class, we can just use the default TinkerLoader
 * <p>
 * loadVerifyFlag: whether check files' md5 on the load time, defualt it is false.
 * <p>
 * Created by zhangshaowen on 16/3/17.
 */
public class SampleApplicationLike extends DefaultApplicationLike {
    public static final String TAG = "Tinker.SampleApplicationLike";
    private static SampleApplicationLike mZBaseApplication;
    private Context context;
    public static int mNetWorkState;
    private List<Activity> list = new ArrayList<Activity>();

    public SampleApplicationLike(Application application, int tinkerFlags,
                                 boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime,
                                 long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime,
                applicationStartMillisTime, tinkerResultIntent);
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        context = base;
        MultiDex.install(base);
        // TODO: 安装tinker
        Beta.installTinker(this);
    }


    @Override
    public void onCreate() {
        super.onCreate();

        AutoLayoutConifg.getInstance().useDeviceSize().init(context);
        // 设置是否开启热更新能力，默认为true
        Beta.enableHotfix = true;
        // 设置是否自动下载补丁，默认为true
        Beta.canAutoDownloadPatch = true;
        // 设置是否自动合成补丁，默认为true
        Beta.canAutoPatch = true;
        Beta.autoCheckUpgrade = false;
        //更新图标
        Beta.largeIconId = R.mipmap.ic_logo;
        Beta.smallIconId = R.mipmap.ic_logo;
        // 设置是否提示用户重启，默认为false
        Beta.canNotifyUserRestart = true;
        // 补丁回调接口
//        Beta.betaPatchListener = new BetaPatchListener() {
//            @Override
//            public void onPatchReceived(String patchFile) {
//                Toast.makeText(getApplication(), "补丁下载地址" + patchFile, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onDownloadReceived(long savedLength, long totalLength) {
//                Toast.makeText(getApplication(),
//                        String.format(Locale.getDefault(), "%s %d%%",
//                                Beta.strNotificationDownloading,
//                                (int) (totalLength == 0 ? 0 : savedLength * 100 / totalLength)),
//                        Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onDownloadSuccess(String msg) {
//                Toast.makeText(getApplication(), "补丁下载成功", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onDownloadFailure(String msg) {
//                Toast.makeText(getApplication(), "补丁下载失败", Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onApplySuccess(String msg) {
//                Toast.makeText(getApplication(), "补丁应用成功", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onApplyFailure(String msg) {
//                Toast.makeText(getApplication(), "补丁应用失败", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onPatchRollback() {
//
//            }
//        };

        //监听更新安装进度
        Beta.upgradeStateListener = new UpgradeStateListener() {
            @Override
            public void onUpgradeSuccess(boolean isManual) {
                Toast.makeText(getApplication(), "UPGRADE_SUCCESS", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUpgradeFailed(boolean isManual) {
                Toast.makeText(getApplication(), "UPGRADE_FAILED", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUpgrading(boolean isManual) {
                Toast.makeText(getApplication(), "UPGRADE_CHECKING", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDownloadCompleted(boolean b) {
                Toast.makeText(getApplication(), "DownloadCompleted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUpgradeNoVersion(boolean isManual) {
                Toast.makeText(getApplication(), "UPGRADE_NO_VERSION", Toast.LENGTH_SHORT).show();
            }
        };


        // 设置开发设备，默认为false，上传补丁如果下发范围指定为“开发设备”，需要调用此接口来标识开发设备
        Bugly.setIsDevelopmentDevice(getApplication(), true);
        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId
        Bugly.init(getApplication(), "8b14fdfa1b", true);


        OpenInstall.init(context);
        //打开调试，便于看到Log
        OpenInstall.setDebug(true);
        mZBaseApplication = this;

        LruCacheUtils.getLruCacheSize();
        ChannelUtil.getChannel(context);//美团渠道打包

    }

    public static SampleApplicationLike getInstance() {
        return mZBaseApplication;
    }

    public void addActivity(Activity activity) {
        list.add(activity);
    }


    public void initData() {
        mNetWorkState = NetUtil.getNetworkState(context);
    }

    /**
     * 检查网络状态
     *
     * @param context
     * @return
     */
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) getApplication()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i] != null && info[i].getState() == NetworkInfo.State.CONNECTED) {
                        NetworkInfo netWorkInfo = info[i];
                        if (netWorkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                            return true;
                        } else if (netWorkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 退出app
     *
     * @param context
     */
    public void exit(Context context) {
        for (Activity activity : list) {
            activity.finish();
        }
        System.exit(0);
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallback(
            Application.ActivityLifecycleCallbacks callbacks) {
        getApplication().registerActivityLifecycleCallbacks(callbacks);
    }

}
