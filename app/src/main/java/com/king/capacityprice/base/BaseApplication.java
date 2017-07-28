package com.king.capacityprice.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.fm.openinstall.OpenInstall;
import com.king.capacityprice.utils.ChannelUtil;
import com.king.capacityprice.utils.LruCacheUtils;
import com.king.capacityprice.utils.NetUtil;
import com.king.capacityprice.utils.blockcheck.BlockError;
import com.king.capacityprice.utils.blockcheck.BlockLooper;
import com.zhy.autolayout.config.AutoLayoutConifg;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by king on 2016/9/18.
 */
public class BaseApplication extends Application {
//    private final static String TAG = BaseApplication.class.getSimpleName();
//    private static BaseApplication mZBaseApplication;
//
//    public static int mNetWorkState;
//    private List<Activity> list = new ArrayList<Activity>();
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        AutoLayoutConifg.getInstance().useDeviceSize().init(this);
//        OpenInstall.init(this);
//        //打开调试，便于看到Log
//        OpenInstall.setDebug(true);
//        mZBaseApplication = this;
//
//        setupDatabase();
//
//        LruCacheUtils.getLruCacheSize();
//        ChannelUtil.getChannel(this);//美团渠道打包
//
//        //初始化BlockLooper
////        BlockLooper.initialize(new BlockLooper.Builder(this)
////                .setIgnoreDebugger(true)
////                .setReportAllThreadInfo(true)
////                .setSaveLog(true)
////                .setOnBlockListener(new BlockLooper.OnBlockListener() {
////                    @Override
////                    public void onBlock(BlockError blockError) {
////                        blockError.printStackTrace();
////                    }
////                })
////                .build());
////        BlockLooper.getBlockLooper().start();//启动检测
//    }
//
//
//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//    }
//
//    public static BaseApplication getInstance() {
//        return mZBaseApplication;
//    }
//
//    public void addActivity(Activity activity) {
//        list.add(activity);
//    }
//
//
//    private void setupDatabase() {
//        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
//        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
//        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
//        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
////        helper = new DaoMaster.DevOpenHelper(this, CommonValues.DB_NAME, null);
////        db = helper.getWritableDatabase();
////        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
////        daoMaster = new DaoMaster(db);
////        daoSession = daoMaster.newSession();
//    }
//
//    public void initData() {
//        mNetWorkState = NetUtil.getNetworkState(this);
//    }
//
//    /**
//     * 检查网络状态
//     *
//     * @return
//     */
//    public boolean isNetworkAvailable() {
//        ConnectivityManager connectivity = (ConnectivityManager) BaseApplication.getInstance()
//                .getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (connectivity == null) {
//            return false;
//        } else {
//            NetworkInfo[] info = connectivity.getAllNetworkInfo();
//            if (info != null) {
//                for (int i = 0; i < info.length; i++) {
//                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
//                        NetworkInfo netWorkInfo = info[i];
//                        if (netWorkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
//                            return true;
//                        } else if (netWorkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
//                            return true;
//                        }
//                    }
//                }
//            }
//        }
//        return false;
//    }

}
