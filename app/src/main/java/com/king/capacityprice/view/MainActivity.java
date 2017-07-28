package com.king.capacityprice.view;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.RouteOverLay;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.route.RouteSearch;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.baidu.mobstat.StatService;
import com.fm.openinstall.OpenInstall;
import com.fm.openinstall.listener.AppWakeUpListener;
import com.fm.openinstall.model.AppData;
import com.fm.openinstall.model.Error;
import com.king.capacityprice.R;
import com.king.capacityprice.base.BaseActivity;
import com.king.capacityprice.databinding.ActivityMainBinding;
import com.king.capacityprice.http.MyJSON;
import com.king.capacityprice.http.RequestCenter;
import com.king.capacityprice.http.ZCResponse;
import com.king.capacityprice.model.request.UserInfo;
import com.king.capacityprice.utils.CommonValues;
import com.king.capacityprice.utils.DateUtils;
import com.king.capacityprice.utils.LogUtil;
import com.king.capacityprice.utils.SharedPreferencesUtil;
import com.king.capacityprice.utils.amap.AMapUtil;
import com.king.capacityprice.utils.dialogUtils.DialogManager;
import com.king.capacityprice.utils.wheelview.DateChooseWheelViewDialog;
import com.king.capacityprice.view.pojo.HighWay;
import com.king.capacityprice.view.pojo.SeaFreightRate;
import com.king.capacityprice.view.pojo.SeaFreightRate2;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends BaseActivity implements LocationSource, AMapLocationListener, AMapNaviListener,
        AMap.OnMapClickListener, View.OnClickListener, GeocodeSearch.OnGeocodeSearchListener,
        AMap.OnMarkerClickListener, AMap.OnInfoWindowClickListener, TextWatcher, Inputtips.InputtipsListener, TextView.OnEditorActionListener, RadioGroup.OnCheckedChangeListener, AppWakeUpListener {
    private ActivityMainBinding mBinding;
    private AMap aMap;
    private MapView mapView;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);

    //线路搜索--默认为驾车
    private final int ROUTE_TYPE_DRIVE = 2;
    private LatLonPoint mStartPoint;
    private LatLonPoint mEndPoint;
    private ProgressDialog progDialog = null;// 搜索时进度条
    private GeocodeSearch geocoderSearch;

    private String city;
    private String cityCode;
    private AutoCompleteTextView mStart;//起点
    private AutoCompleteTextView mEnd;//终点
    private ImageView mSearch;

    private ListView mList;
    private LinearLayout mLayoutPrice;//价格View
    private View mPopView;
    private List<HashMap<String, String>> listString = new ArrayList<>();

    private SimpleAdapter mAdapter;
    private boolean flag;

    private GeocodeAddress address;
    private ImageView mPrive20;
    private ImageView mPrive40;
    private ImageView mPrive35;
    private TextView mHaiType;

    private RadioButton mRb20;
    private RadioButton mRb40;
    private RadioButton mRb35;
    private TextView m20;
    private TextView m40;
    private TextView m35;
    private LinearLayout mNoPrice;
    private LinearLayout mPrice;


    private HighWay highWay;
    private SeaFreightRate seaFreightRate;
    private SeaFreightRate2 seaFreightRate2;
    private Map<String, String> timeMap = new HashMap<>();//价格天数
    private Map<String, Object> price3Map = new HashMap<>();
    private Map<String, Object> price5Map = new HashMap<>();
    private Map<String, Object> price7Map = new HashMap<>();


    /**
     * 起点坐标集合[由于需要确定方向，建议设置多个起点]
     */
    private List<NaviLatLng> startList = new ArrayList<NaviLatLng>();
    /**
     * 途径点坐标集合
     */
    private List<NaviLatLng> wayList = new ArrayList<NaviLatLng>();
    /**
     * 终点坐标集合［建议就一个终点］
     */
    private List<NaviLatLng> endList = new ArrayList<NaviLatLng>();
    private int navigationType = 0;
    private AMapNavi mAMapNavi;

    /**
     * 保存当前算好的路线
     */
    private SparseArray<RouteOverLay> routeOverlays = new SparseArray<RouteOverLay>();
    private List<AMapNaviPath> ways = new ArrayList<>();
    private boolean calculateSuccess;
    private int routeIndex = 0;
    private int zindex = 0;
    private String mDistanceLimt;//线路距离

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        OpenInstall.getWakeUp(getIntent(), this);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                1);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
        setListener();
    }

    /**
     * 初始化
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.getUiSettings().setMyLocationButtonEnabled(true); // 是否显示定位按钮
            aMap.setMyLocationEnabled(true);//显示定位层并且可以触发定位,默认是flase
//            //初始化client
            locationOption = getDefaultOption();
            setupLocationStyle();
            locationClient = new AMapLocationClient(this.getApplicationContext());
            locationClient.setLocationOption(locationOption);
            startLocation();
            mAMapNavi = AMapNavi.getInstance(getApplicationContext());
            mAMapNavi.addAMapNaviListener(this);

        }
        mStart = (AutoCompleteTextView) findViewById(R.id.et_start);
        mEnd = (AutoCompleteTextView) findViewById(R.id.et_end);
        mSearch = (ImageView) findViewById(R.id.iv_search);
        mList = (ListView) findViewById(R.id.list);

        registerListener();
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
    }

    private void setListener() {
        mStart.addTextChangedListener(this);
        mEnd.addTextChangedListener(this);
        mSearch.setOnClickListener(this);

        mBinding.chooseTime.setOnClickListener(this);

        mBinding.etStart.setOnEditorActionListener(this);
        mBinding.etEnd.setOnEditorActionListener(this);
    }

    //注册监听
    private void registerListener() {
        aMap.setOnMapClickListener(this);
        aMap.setOnMarkerClickListener(this);
        aMap.setOnInfoWindowClickListener(this);
    }


    /**
     * 定位成功后回调函数
     *
     * @param amapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                aMap.moveCamera(CameraUpdateFactory.zoomTo(25));
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    /**
     * 开始定位
     */
    private void startLocation() {
        // 启动定位
        aMap.moveCamera(CameraUpdateFactory.zoomTo(25));
        locationClient.startLocation();
    }

    /**
     * 停止定位
     */
    private void stopLocation() {
        // 停止定位
        locationClient.stopLocation();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_head:
                startActivity(new Intent(this, PersonalActivity.class));
                break;
            case R.id.iv_change:
                flag = !flag;
                String strStart = mStart.getText().toString();
                String strEnd = mEnd.getText().toString();
                mStart.setText(strEnd);
                mEnd.setText(strStart);
                LatLonPoint point = mStartPoint;
                mStartPoint = mEndPoint;
                mEndPoint = point;
                toSearch();
                break;
            case R.id.chooseTime:
                DateChooseWheelViewDialog endDateChooseDialog = new DateChooseWheelViewDialog(MainActivity.this,
                        new DateChooseWheelViewDialog.DateChooseInterface() {
                            @Override
                            public void getDateTime(String time, boolean longTimeChecked) {
                                mBinding.chooseTime.setText(time);
                            }
                        });
                endDateChooseDialog.setTimePickerGone(true);
                endDateChooseDialog.setDateDialogTitle("结束时间");
                endDateChooseDialog.showDateChooseDialog();
                break;
            case R.id.iv_search:
                toSearch();
                break;
            case R.id.btn_searchPrice:
                mPopView.findViewById(R.id.btn_searchPrice).setVisibility(View.GONE);
                DialogManager.getInstance().showProgressDialog(this, "正在查询，请稍后");
                UserInfo userInfo = (UserInfo) SharedPreferencesUtil.readObject(this, CommonValues.USERINFO);
                RequestCenter.selectAll(userInfo.getId() + "", mBinding.chooseTime.getText() + "", mBinding.etStart.getText().toString(), mBinding.etEnd.getText().toString(), mStartPoint.getLongitude() + "", mStartPoint.getLatitude() + "", mEndPoint.getLongitude() + "", mEndPoint.getLatitude() + "", mDistanceLimt + "", this);
                break;
        }
    }

    /**
     * 开始搜索
     */
    public void toSearch() {
        mBinding.list.setVisibility(View.GONE);
        if (TextUtils.isEmpty(mBinding.etStart.getText().toString().trim()) || TextUtils.isEmpty(mBinding.etEnd.getText().toString().trim())) {
            Toast.makeText(this, "请输入起点或终点", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(mBinding.chooseTime.getText())) {
            Toast.makeText(this, "请输入查询时间", Toast.LENGTH_SHORT).show();
            return;
        }
        //关闭软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }

        aMap.clear();
        stopLocation();
        searchRouteResult(ROUTE_TYPE_DRIVE, RouteSearch.DrivingDefault);
    }


    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult(int routeType, int mode) {
        startList.clear();
        endList.clear();
        if (mStartPoint == null) {
            Toast.makeText(this, "定位中，稍后再试...", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mEndPoint == null) {
            Toast.makeText(this, "终点未设置", Toast.LENGTH_SHORT).show();
        }
        showProgressDialog();

        startList.add(new NaviLatLng(mStartPoint.getLatitude(), mStartPoint.getLongitude()));

        endList.add(new NaviLatLng(mEndPoint.getLatitude(), mEndPoint.getLongitude()));
        planRoute();
//        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
//                mEndPoint, mStartPoint);
//        if (routeType == ROUTE_TYPE_DRIVE) {// 驾车路径规划
//            RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, mode, null,
//                    null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
//            mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
//        }
    }


    /**
     * 清除当前地图上算好的路线
     */
    private void clearRoute() {
        for (int i = 0; i < routeOverlays.size(); i++) {
            RouteOverLay routeOverlay = routeOverlays.valueAt(i);
            routeOverlay.removeFromMap();
        }
        routeOverlays.clear();
        ways.clear();
    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {
        /**
         * 清空上次计算的路径列表。
         */
        routeOverlays.clear();
        ways.clear();
        AMapNaviPath path = mAMapNavi.getNaviPath();
        /**
         * 单路径不需要进行路径选择，直接传入－1即可
         */
        drawRoutes(-1, path);
        dissmissProgressDialog();
        showPop();
    }

//    @Override
//    public void onCalculateMultipleRoutesSuccess(int[] ints) {
//        //清空上次计算的路径列表。
//        routeOverlays.clear();
//        ways.clear();
//        HashMap<Integer, AMapNaviPath> paths = mAMapNavi.getNaviPaths();
////        for (int i = 0; i < ints.length; i++) {
////            AMapNaviPath path = paths.get(ints[i]);
////            if (path != null) {
////                drawRoutes(ints[i], path);
////                ways.add(path);
////            }
////        }
//        drawRoutes(ints[0], paths.get(ints[0]));
//        changeRoute();
//        dissmissProgressDialog();
//        showPop();
//    }

    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }

    @Override
    public void onPlayRing(int i) {

    }


    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onInitNaviSuccess() {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }


    @Override
    public void onCalculateRouteFailure(int i) {
        calculateSuccess = false;
    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void hideLaneInfo() {

    }

    /**
     * 路线规划
     */
    private void planRoute() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (startList.size() > 0 && endList.size() > 0) {
                    if (navigationType == 0) {//驾车
                        int strategy = 0;
                        try {
                            /**
                             * 方法:
                             *   int strategy=mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, multipleroute);
                             * 参数:
                             * @congestion 躲避拥堵
                             * @avoidhightspeed 不走高速
                             * @cost 避免收费
                             * @hightspeed 高速优先
                             * @multipleroute 多路径
                             *
                             * 说明:
                             *      以上参数都是boolean类型，其中multipleroute参数表示是否多条路线，如果为true则此策略会算出多条路线。
                             * 注意:
                             *      不走高速与高速优先不能同时为true
                             *      高速优先与避免收费不能同时为true
                             */
                            strategy = mAMapNavi.strategyConvert(true, false, false, true, true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mAMapNavi.calculateDriveRoute(startList, endList, wayList, strategy);
                    } else if (navigationType == 1) {//步行
                        mAMapNavi.calculateWalkRoute(startList.get(0), endList.get(0));
                    } else {//骑行
                        mAMapNavi.calculateRideRoute(startList.get(0), endList.get(0));
                    }
                }
            }
        });

    }

    /**
     * 选择路线
     */
    public void changeRoute() {
        if (!calculateSuccess) {
            Toast.makeText(this, "请先算路", Toast.LENGTH_SHORT).show();
            return;
        }
        /**
         * 计算出来的路径只有一条
         */
        if (routeOverlays.size() == 1) {
            //必须告诉AMapNavi 你最后选择的哪条路
            mAMapNavi.selectRouteId(routeOverlays.keyAt(0));
            return;
        }

        if (routeIndex >= routeOverlays.size())
            routeIndex = 0;
        int routeID = routeOverlays.keyAt(routeIndex);
        //突出选择的那条路
        for (int i = 0; i < routeOverlays.size(); i++) {
            int key = routeOverlays.keyAt(i);
            routeOverlays.get(key).setTransparency(0.4f);
        }
        routeOverlays.get(routeID).setTransparency(1);
        /**把用户选择的那条路的权值弄高，使路线高亮显示的同时，重合路段不会变的透明**/
        routeOverlays.get(routeID).setZindex(zindex++);

        //必须告诉AMapNavi 你最后选择的哪条路
        mAMapNavi.selectRouteId(routeID);
        routeIndex++;

    }

    /**
     * 绘制路线
     *
     * @param routeId
     * @param path
     */
    private void drawRoutes(int routeId, AMapNaviPath path) {
        calculateSuccess = true;
        aMap.moveCamera(CameraUpdateFactory.changeTilt(0));
        RouteOverLay routeOverLay = new RouteOverLay(aMap, path, this);
        routeOverLay.setTrafficLine(false);
        Log.e("距离", routeOverLay.getAMapNaviPath().getAllLength() + "");
        mDistanceLimt = routeOverLay.getAMapNaviPath().getAllLength() + "";
        routeOverLay.addToMap();
        routeOverlays.put(routeId, routeOverLay);
    }


    @Override
    public boolean onSuccess(ZCResponse response, String requestUrl, String method) {
        DialogManager.getInstance().dissMissDialog();
        if (requestUrl.equals(RequestCenter.USER_ACTION) && method.equals(RequestCenter.SELECTALL_METHOD)) {
            String msg = response.getMessage();
            if (!TextUtils.equals(msg, CommonValues.isY)) {
                Toast.makeText(this, "查询失败", Toast.LENGTH_SHORT).show();
            } else {
                highWay = MyJSON.parseObject(response.getMainData().getString("mapHighWay"), HighWay.class);//公路
                seaFreightRate = MyJSON.parseObject(response.getMainData().getString("mapFreightRate"), SeaFreightRate.class);//海运
                seaFreightRate2 = MyJSON.parseObject(response.getMainData().getString("mapFreightRate2"), SeaFreightRate2.class);//铁运
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if ((highWay.getTimeday() == null) && (seaFreightRate.getTimeday() == null) && (seaFreightRate2.getTimeday() == null)) {
                            //0 0 0
                            mLayoutPrice.setVisibility(View.VISIBLE);
                            mNoPrice.setVisibility(View.VISIBLE);
                            mPrice.setVisibility(View.GONE);
                        } else if ((highWay.getTimeday() == null) && (seaFreightRate.getTimeday() != null) && (seaFreightRate2.getTimeday() != null)) {
                            //0 1 1
                            initVisibility();
                            mRb35.setVisibility(View.GONE);
                            sort2(highWay, seaFreightRate, seaFreightRate2);
                            mRb20.setVisibility(View.VISIBLE);
                            mRb40.setVisibility(View.VISIBLE);
                            mRb35.setVisibility(View.GONE);
                            mRb20.setText(timeMap.get("a") + "天");
                            mRb40.setText(timeMap.get("b") + "天");
                            initPriceMap3();
                        } else if ((highWay.getTimeday() == null) && (seaFreightRate.getTimeday() != null) && (seaFreightRate2.getTimeday() == null)) {
                            //0 1 0
                            initVisibility();
                            mRb20.setVisibility(View.VISIBLE);
                            mRb40.setVisibility(View.GONE);
                            mRb35.setVisibility(View.GONE);
                            mRb20.setText(seaFreightRate.getTimeday() + "天");
                            onlyOne();
                            initPriceMap3();
                        } else if ((highWay.getTimeday() != null) && (seaFreightRate.getTimeday() == null) && (seaFreightRate2.getTimeday() == null)) {
                            //1 0 0
                            initVisibility();
                            mRb20.setVisibility(View.VISIBLE);
                            mRb40.setVisibility(View.GONE);
                            mRb35.setVisibility(View.GONE);
                            mRb20.setText(highWay.getTimeday() + "天");
                            onlyOne();
                            initPriceMap3();
                        } else if ((highWay.getTimeday() != null) && (seaFreightRate.getTimeday() == null) && (seaFreightRate2.getTimeday() != null)) {
                            //1 0 1
                            initVisibility();
                            mRb20.setVisibility(View.VISIBLE);
                            mRb40.setVisibility(View.VISIBLE);
                            mRb35.setVisibility(View.GONE);
                            sort2(highWay, seaFreightRate, seaFreightRate2);
                            mRb20.setText(timeMap.get("a") + "天");
                            mRb40.setText(timeMap.get("b") + "天");
                            initPriceMap3();
                        } else if ((highWay.getTimeday() != null) && (seaFreightRate.getTimeday() != null) && (seaFreightRate2.getTimeday() == null)) {
                            //1 1 0
                            initVisibility();
                            mRb20.setVisibility(View.VISIBLE);
                            mRb40.setVisibility(View.VISIBLE);
                            mRb35.setVisibility(View.GONE);
                            sort2(highWay, seaFreightRate, seaFreightRate2);
                            mRb20.setText(timeMap.get("a") + "天");
                            mRb40.setText(timeMap.get("b") + "天");
                            initPriceMap3();
                        } else if ((highWay.getTimeday() == null) && (seaFreightRate.getTimeday() == null) && (seaFreightRate2.getTimeday() != null)) {
                            //0 0  1
                            initVisibility();
                            mRb20.setVisibility(View.VISIBLE);
                            mRb40.setVisibility(View.GONE);
                            mRb35.setVisibility(View.GONE);
                            mRb20.setText(seaFreightRate2.getTimeday() + "天");
                            onlyOne();
                            initPriceMap3();
                        } else {
                            //1 1 1
                            initVisibility();
                            mRb20.setVisibility(View.VISIBLE);
                            mRb40.setVisibility(View.VISIBLE);
                            mRb35.setVisibility(View.VISIBLE);
                            sort3(highWay, seaFreightRate, seaFreightRate2);
                            mRb20.setText(timeMap.get("a") + "天");
                            mRb40.setText(timeMap.get("b") + "天");
                            mRb35.setText(timeMap.get("c") + "天");
                            initPriceMap3();
                        }


                    }
                });
            }
        }
        return super.onSuccess(response, requestUrl, method);
    }

    void initVisibility() {
        mLayoutPrice.setVisibility(View.VISIBLE);
        mNoPrice.setVisibility(View.GONE);
        mPrice.setVisibility(View.VISIBLE);
    }

    void onlyOne() {
        if (highWay.getTimeday() != null) {
            price3Map.put("3", highWay);
        } else if (seaFreightRate.getTimeday() != null) {
            price3Map.put("3", seaFreightRate);
        } else {
            price3Map.put("3", seaFreightRate2);
        }
    }

    void initPriceMap3() {
        Object object = price3Map.get("3");
        if (object instanceof HighWay) {
            mHaiType.setText("20英尺35T箱");
            if (TextUtils.equals(highWay.getF20(), getResources().getString(R.string.tv_null)) || TextUtils.isEmpty(highWay.getF20())) {
                m20.setText(getResources().getString(R.string.tv_null));
                mPrive20.setVisibility(View.GONE);
            } else {
                m20.setText(highWay.getF20());
                mPrive20.setVisibility(View.VISIBLE);
            }
            if (TextUtils.equals(highWay.getF40(), getResources().getString(R.string.tv_null)) || TextUtils.isEmpty(highWay.getF40())) {
                m40.setText(getResources().getString(R.string.tv_null));
                mPrive40.setVisibility(View.GONE);
            } else {
                m40.setText(highWay.getF40());
                mPrive40.setVisibility(View.VISIBLE);
            }

            if (TextUtils.equals(highWay.getF20_TWO(), getResources().getString(R.string.tv_null)) || TextUtils.isEmpty(highWay.getF20_TWO())) {
                m35.setText(getResources().getString(R.string.tv_null));
                mPrive35.setVisibility(View.GONE);
            } else {
                m35.setText(highWay.getF20_TWO());
                mPrive35.setVisibility(View.VISIBLE);
            }
        } else if (object instanceof SeaFreightRate) {
            mHaiType.setText("40HQ");
            if (TextUtils.equals(seaFreightRate.getF20(), getResources().getString(R.string.tv_null)) || TextUtils.isEmpty(seaFreightRate.getF20())) {
                m20.setText(getResources().getString(R.string.tv_null));
                mPrive20.setVisibility(View.GONE);
            } else {
                m20.setText(seaFreightRate.getF20());
                mPrive20.setVisibility(View.VISIBLE);
            }

            if (TextUtils.equals(seaFreightRate.getF40(), getResources().getString(R.string.tv_null)) || TextUtils.isEmpty(seaFreightRate.getF40())) {
                m40.setText(getResources().getString(R.string.tv_null));
                mPrive40.setVisibility(View.GONE);
            } else {
                m40.setText(seaFreightRate.getF40());
                mPrive40.setVisibility(View.VISIBLE);
            }

            if (TextUtils.equals(seaFreightRate.getF20_TWO(), getResources().getString(R.string.tv_null)) || TextUtils.isEmpty(seaFreightRate.getF20_TWO())) {
                m35.setText(getResources().getString(R.string.tv_null));
                mPrive35.setVisibility(View.GONE);
            } else {
                m35.setText(seaFreightRate.getF20_TWO());
                mPrive35.setVisibility(View.VISIBLE);
            }
        } else {
            mHaiType.setText("20英尺35T箱");
            if (TextUtils.equals(seaFreightRate2.getF20(), getResources().getString(R.string.tv_null)) || TextUtils.isEmpty(seaFreightRate2.getF20())) {
                m20.setText(getResources().getString(R.string.tv_null));
                mPrive20.setVisibility(View.GONE);
            } else {
                m20.setText(seaFreightRate2.getF20());
                mPrive20.setVisibility(View.VISIBLE);
            }

            if (TextUtils.equals(seaFreightRate2.getF40(), getResources().getString(R.string.tv_null)) || TextUtils.isEmpty(seaFreightRate2.getF40())) {
                m40.setText(getResources().getString(R.string.tv_null));
                mPrive40.setVisibility(View.GONE);
            } else {
                m40.setText(seaFreightRate2.getF40());
                mPrive40.setVisibility(View.VISIBLE);
            }

            if (TextUtils.equals(seaFreightRate2.getF20_TWO(), getResources().getString(R.string.tv_null)) || TextUtils.isEmpty(seaFreightRate2.getF20_TWO())) {
                m35.setText(getResources().getString(R.string.tv_null));
                mPrive35.setVisibility(View.GONE);
            } else {
                m35.setText(seaFreightRate2.getF20_TWO());
                mPrive35.setVisibility(View.VISIBLE);
            }

        }
    }

    void sort2(HighWay highWay, SeaFreightRate seaFreightRate, SeaFreightRate2 seaFreightRate2) {

        if (highWay.getTimeday() == null) {
            int timeSea = Integer.parseInt(seaFreightRate.getTimeday());
            int timeSea2 = Integer.parseInt(seaFreightRate2.getTimeday());

            if (timeSea > timeSea2) {
                timeMap.put("a", timeSea2 + "");
                timeMap.put("b", timeSea + "");
                price3Map.put("3", seaFreightRate2);
                price5Map.put("5", seaFreightRate);
            } else {
                timeMap.put("a", timeSea + "");
                timeMap.put("b", timeSea2 + "");
                price3Map.put("3", seaFreightRate);
                price5Map.put("5", seaFreightRate2);
            }
        }


        if (seaFreightRate.getTimeday() == null) {
            int timeWay = Integer.parseInt(highWay.getTimeday());
            int timeSea2 = Integer.parseInt(seaFreightRate2.getTimeday());

            if (timeWay > timeSea2) {
                timeMap.put("a", timeSea2 + "");
                timeMap.put("b", timeWay + "");
                price3Map.put("3", seaFreightRate2);
                price5Map.put("5", highWay);
            } else {
                timeMap.put("a", timeWay + "");
                timeMap.put("b", timeSea2 + "");
                price3Map.put("3", highWay);
                price5Map.put("5", seaFreightRate2);
            }
        }


        if (seaFreightRate2.getTimeday() == null) {
            int timeWay = Integer.parseInt(highWay.getTimeday());
            int timeSea = Integer.parseInt(seaFreightRate.getTimeday());

            if (timeWay > timeSea) {
                timeMap.put("a", timeSea + "");
                timeMap.put("b", timeWay + "");
                price3Map.put("3", seaFreightRate);
                price5Map.put("5", highWay);
            } else {
                timeMap.put("a", timeWay + "");
                timeMap.put("b", timeSea + "");
                price3Map.put("3", highWay);
                price5Map.put("5", seaFreightRate);
            }
        }

    }

    void sort3(HighWay highWay, SeaFreightRate seaFreightRate, SeaFreightRate2 seaFreightRate2) {
        int timeWay = Integer.parseInt(highWay.getTimeday());
        int timeSea = Integer.parseInt(seaFreightRate.getTimeday());
        int timeSea2 = Integer.parseInt(seaFreightRate2.getTimeday());

        Map<String, Object> map = new HashMap<>();

        map.put(timeWay + "", highWay);
        map.put(timeSea + "", seaFreightRate);
        map.put(timeSea2 + "", seaFreightRate2);
        int temp = 0;
        if (timeWay > timeSea) {
            temp = timeWay;
            timeWay = timeSea;
            timeSea = temp;
        }
        if (timeWay > timeSea2) {
            temp = timeWay;
            timeWay = timeSea2;
            timeSea2 = temp;
        }
        if (timeSea > timeSea2) {
            temp = timeSea;
            timeSea = timeSea2;
            timeSea2 = temp;
        }
        //从小到大
        timeMap.put("a", timeWay + "");
        timeMap.put("b", timeSea + "");
        timeMap.put("c", timeSea2 + "");
        price3Map.put("3", map.get(timeWay + ""));
        price5Map.put("5", map.get(timeSea + ""));
        price7Map.put("7", map.get(timeSea2 + ""));
    }

    /**
     * 显示进度框
     */

    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(false);
        progDialog.setMessage("请稍后...");
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }


    public void showPop() {
        mPopView = MainActivity.this.getLayoutInflater().inflate(R.layout.pop_show, null);
        final PopupWindow window = new PopupWindow(mPopView, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button btnSearch = (Button) mPopView.findViewById(R.id.btn_searchPrice);
        TextView showStart = (TextView) mPopView.findViewById(R.id.tv_showStart);
        TextView showEnd = (TextView) mPopView.findViewById(R.id.tv_showEnd);
        showStart.setText(mBinding.etStart.getText());
        showEnd.setText(mBinding.etEnd.getText());
        btnSearch.setOnClickListener(this);
        window.setAnimationStyle(R.style.pop_anim);
        window.setBackgroundDrawable(new ColorDrawable(0xb0000000));
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.showAtLocation(findViewById(R.id.activity_main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
        mPopView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                int height = mPopView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        window.dismiss();
                    }
                }
                return true;
            }
        });

        mLayoutPrice = (LinearLayout) mPopView.findViewById(R.id.layout_price);
        mPrive20 = (ImageView) mLayoutPrice.findViewById(R.id.iv_price20);
        mPrive40 = (ImageView) mLayoutPrice.findViewById(R.id.iv_price40);
        mPrive35 = (ImageView) mLayoutPrice.findViewById(R.id.iv_price35);

        mHaiType = (TextView) mLayoutPrice.findViewById(R.id.tv_type);

        m20 = (TextView) mLayoutPrice.findViewById(R.id.tv_20);
        m40 = (TextView) mLayoutPrice.findViewById(R.id.tv_40);
        m35 = (TextView) mLayoutPrice.findViewById(R.id.tv_35);

        mNoPrice = (LinearLayout) mLayoutPrice.findViewById(R.id.ll_noPrice);
        mPrice = (LinearLayout) mLayoutPrice.findViewById(R.id.ll_price);
        RadioGroup rb = (RadioGroup) mLayoutPrice.findViewById(R.id.rg_choose);

        mRb20 = (RadioButton) rb.findViewById(R.id.rb_3);
        mRb40 = (RadioButton) rb.findViewById(R.id.rb_5);
        mRb35 = (RadioButton) rb.findViewById(R.id.rb_7);

        rb.setOnCheckedChangeListener(this);

        dissmissProgressDialog();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

        switch (checkedId) {
            case R.id.rb_3:
                initPriceMap3();
                break;
            case R.id.rb_5:
                Object object2 = price5Map.get("5");
                if (object2 instanceof HighWay) {
                    mHaiType.setText("20英尺35T箱");
                    if (TextUtils.equals(highWay.getF20(), getResources().getString(R.string.tv_null)) || TextUtils.isEmpty(highWay.getF20())) {
                        m20.setText(getResources().getString(R.string.tv_null));
                        mPrive20.setVisibility(View.GONE);
                    } else {
                        m20.setText(highWay.getF20());
                        mPrive20.setVisibility(View.VISIBLE);
                    }
                    if (TextUtils.equals(highWay.getF40(), getResources().getString(R.string.tv_null)) || TextUtils.isEmpty(highWay.getF40())) {
                        m40.setText(getResources().getString(R.string.tv_null));
                        mPrive40.setVisibility(View.GONE);
                    } else {
                        m40.setText(highWay.getF40());
                        mPrive40.setVisibility(View.VISIBLE);
                    }

                    if (TextUtils.equals(highWay.getF20_TWO(), getResources().getString(R.string.tv_null)) || TextUtils.isEmpty(highWay.getF20_TWO())) {
                        m35.setText(getResources().getString(R.string.tv_null));
                        mPrive35.setVisibility(View.GONE);
                    } else {
                        m35.setText(highWay.getF20_TWO());
                        mPrive35.setVisibility(View.VISIBLE);
                    }
                } else if (object2 instanceof SeaFreightRate) {
                    mHaiType.setText("40HQ");
                    if (TextUtils.equals(seaFreightRate.getF20(), getResources().getString(R.string.tv_null)) || TextUtils.isEmpty(seaFreightRate.getF20())) {
                        m20.setText(getResources().getString(R.string.tv_null));
                        mPrive20.setVisibility(View.GONE);
                    } else {
                        m20.setText(seaFreightRate.getF20());
                        mPrive20.setVisibility(View.VISIBLE);
                    }

                    if (TextUtils.equals(seaFreightRate.getF40(), getResources().getString(R.string.tv_null)) || TextUtils.isEmpty(seaFreightRate.getF40())) {
                        m40.setText(getResources().getString(R.string.tv_null));
                        mPrive40.setVisibility(View.GONE);
                    } else {
                        m40.setText(seaFreightRate.getF40());
                        mPrive40.setVisibility(View.VISIBLE);
                    }

                    if (TextUtils.equals(seaFreightRate.getF20_TWO(), getResources().getString(R.string.tv_null)) || TextUtils.isEmpty(seaFreightRate.getF20_TWO())) {
                        m35.setText(getResources().getString(R.string.tv_null));
                        mPrive35.setVisibility(View.GONE);
                    } else {
                        m35.setText(seaFreightRate.getF20_TWO());
                        mPrive35.setVisibility(View.VISIBLE);
                    }
                } else {
                    mHaiType.setText("20英尺35T箱");
                    if (TextUtils.equals(seaFreightRate2.getF20(), getResources().getString(R.string.tv_null)) || TextUtils.isEmpty(seaFreightRate2.getF20())) {
                        m20.setText(getResources().getString(R.string.tv_null));
                        mPrive20.setVisibility(View.GONE);
                    } else {
                        m20.setText(seaFreightRate2.getF20());
                        mPrive20.setVisibility(View.VISIBLE);
                    }

                    if (TextUtils.equals(seaFreightRate2.getF40(), getResources().getString(R.string.tv_null)) || TextUtils.isEmpty(seaFreightRate2.getF40())) {
                        m40.setText(getResources().getString(R.string.tv_null));
                        mPrive40.setVisibility(View.GONE);
                    } else {
                        m40.setText(seaFreightRate2.getF40());
                        mPrive40.setVisibility(View.VISIBLE);
                    }

                    if (TextUtils.equals(seaFreightRate2.getF20_TWO(), getResources().getString(R.string.tv_null)) || TextUtils.isEmpty(seaFreightRate2.getF20_TWO())) {
                        m35.setText(getResources().getString(R.string.tv_null));
                        mPrive35.setVisibility(View.GONE);
                    } else {
                        m35.setText(seaFreightRate2.getF20_TWO());
                        mPrive35.setVisibility(View.VISIBLE);
                    }

                }
                break;
            case R.id.rb_7:
                Object object3 = price7Map.get("7");
                if (object3 instanceof HighWay) {
                    mHaiType.setText("20英尺35T箱");
                    if (TextUtils.equals(highWay.getF20(), getResources().getString(R.string.tv_null)) || TextUtils.isEmpty(highWay.getF20())) {
                        m20.setText(getResources().getString(R.string.tv_null));
                        mPrive20.setVisibility(View.GONE);
                    } else {
                        m20.setText(highWay.getF20());
                        mPrive20.setVisibility(View.VISIBLE);
                    }
                    if (TextUtils.equals(highWay.getF40(), getResources().getString(R.string.tv_null)) || TextUtils.isEmpty(highWay.getF40())) {
                        m40.setText(getResources().getString(R.string.tv_null));
                        mPrive40.setVisibility(View.GONE);
                    } else {
                        m40.setText(highWay.getF40());
                        mPrive40.setVisibility(View.VISIBLE);
                    }

                    if (TextUtils.equals(highWay.getF20_TWO(), getResources().getString(R.string.tv_null)) || TextUtils.isEmpty(highWay.getF20_TWO())) {
                        m35.setText(getResources().getString(R.string.tv_null));
                        mPrive35.setVisibility(View.GONE);
                    } else {
                        m35.setText(highWay.getF20_TWO());
                        mPrive35.setVisibility(View.VISIBLE);
                    }
                } else if (object3 instanceof SeaFreightRate) {
                    mHaiType.setText("40HQ");
                    if (TextUtils.equals(seaFreightRate.getF20(), getResources().getString(R.string.tv_null)) || TextUtils.isEmpty(seaFreightRate.getF20())) {
                        m20.setText(getResources().getString(R.string.tv_null));
                        mPrive20.setVisibility(View.GONE);
                    } else {
                        m20.setText(seaFreightRate.getF20());
                        mPrive20.setVisibility(View.VISIBLE);
                    }

                    if (TextUtils.equals(seaFreightRate.getF40(), getResources().getString(R.string.tv_null)) || TextUtils.isEmpty(seaFreightRate.getF40())) {
                        m40.setText(getResources().getString(R.string.tv_null));
                        mPrive40.setVisibility(View.GONE);
                    } else {
                        m40.setText(seaFreightRate.getF40());
                        mPrive40.setVisibility(View.VISIBLE);
                    }

                    if (TextUtils.equals(seaFreightRate.getF20_TWO(), getResources().getString(R.string.tv_null)) || TextUtils.isEmpty(seaFreightRate.getF20_TWO())) {
                        m35.setText(getResources().getString(R.string.tv_null));
                        mPrive35.setVisibility(View.GONE);
                    } else {
                        m35.setText(seaFreightRate.getF20_TWO());
                        mPrive35.setVisibility(View.VISIBLE);
                    }
                } else {
                    mHaiType.setText("20英尺35T箱");
                    if (TextUtils.equals(seaFreightRate2.getF20(), getResources().getString(R.string.tv_null)) || TextUtils.isEmpty(seaFreightRate2.getF20())) {
                        m20.setText(getResources().getString(R.string.tv_null));
                        mPrive20.setVisibility(View.GONE);
                    } else {
                        m20.setText(seaFreightRate2.getF20());
                        mPrive20.setVisibility(View.VISIBLE);
                    }

                    if (TextUtils.equals(seaFreightRate2.getF40(), getResources().getString(R.string.tv_null)) || TextUtils.isEmpty(seaFreightRate2.getF40())) {
                        m40.setText(getResources().getString(R.string.tv_null));
                        mPrive40.setVisibility(View.GONE);
                    } else {
                        m40.setText(seaFreightRate2.getF40());
                        mPrive40.setVisibility(View.VISIBLE);
                    }

                    if (TextUtils.equals(seaFreightRate2.getF20_TWO(), getResources().getString(R.string.tv_null)) || TextUtils.isEmpty(seaFreightRate2.getF20_TWO())) {
                        m35.setText(getResources().getString(R.string.tv_null));
                        mPrive35.setVisibility(View.GONE);
                    } else {
                        m35.setText(seaFreightRate2.getF20_TWO());
                        mPrive35.setVisibility(View.VISIBLE);
                    }

                }
                break;
        }
    }

    /**
     * 默认的定位参数
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(true);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(true);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(true);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }


    /**
     * 设置自定义定位蓝点
     */
    private void setupLocationStyle() {
        // 自定义系统定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
                fromResource(R.drawable.purple_pin));
        // 将自定义的 myLocationStyle 对象添加到地图上
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        aMap.setMyLocationStyle(myLocationStyle);
    }


    @Override
    public void onInfoWindowClick(Marker arg0) {

    }

    @Override
    public boolean onMarkerClick(Marker arg0) {
        return false;
    }

    @Override
    public void onMapClick(LatLng arg0) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(final CharSequence s, int start, int before, int count) {
        mList.setVisibility(View.VISIBLE);
        String newText = s.toString().trim();
        InputtipsQuery inputQuery = new InputtipsQuery(newText, "");
//        inputQuery.setCityLimit(true);
        Inputtips inputTips = new Inputtips(MainActivity.this, inputQuery);
        inputTips.setInputtipsListener(MainActivity.this);
        inputTips.requestInputtipsAsyn();
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(s)) {
            mList.setVisibility(View.GONE);
        }
    }

    @Override
    public void onGetInputtips(final List<Tip> tipList, final int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            listString.clear();
            for (int i = 0; i < tipList.size(); i++) {
                HashMap<String, String> map = new HashMap<>();
                map.put("name", tipList.get(i).getName());
                map.put("address", tipList.get(i).getDistrict());
                map.put("adcode", tipList.get(i).getAdcode());
                listString.add(map);
            }
            mAdapter = new SimpleAdapter(getApplicationContext(), listString, R.layout.item_searchresult,
                    new String[]{"name", "address"}, new int[]{R.id.poi_field_id, R.id.poi_value_id});

            mList.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Map<String, Object> mMap = (Map<String, Object>) mAdapter.getItem(position);
                    if (mStart.getId() == findViewById(R.id.activity_main).findFocus().getId()) {
                        flag = true;
                        mStart.setText(mMap.get("name").toString());
                        Editable text = mStart.getText();
                        Selection.setSelection(text, text.length());
                        getLatLon(mStart.getText().toString(), mMap.get("adcode").toString());
                    } else {
                        flag = false;
                        mEnd.setText(mMap.get("name").toString());
                        Editable text = mEnd.getText();
                        Selection.setSelection(text, text.length());
                        getLatLon(mEnd.getText().toString(), mMap.get("adcode").toString());
                    }
                    mList.setVisibility(View.GONE);
                    listString.clear();
                }
            });
        } else {
            Toast.makeText(this, rCode, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            toSearch();
            return true;
        }
        return false;
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        StatService.onPageStart(this, "MainActivity");
        mapView.onResume();
    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPageStart(this, "MainActivity");
        mapView.onPause();
        stopLocation();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    /**
     * 响应地理编码
     */
    public void getLatLon(final String name, final String adCode) {
        showProgressDialog();
        GeocodeQuery query = new GeocodeQuery(name, adCode);// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
        geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

    }

    /**
     * 地理编码查询回调
     */
    @Override
    public void onGeocodeSearched(GeocodeResult result, int rCode) {
        dissmissProgressDialog();
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getGeocodeAddressList() != null
                    && result.getGeocodeAddressList().size() > 0) {
                address = result.getGeocodeAddressList().get(0);
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        AMapUtil.convertToLatLng(address.getLatLonPoint()), 15));
                if (flag) {
                    mStartPoint = address.getLatLonPoint();
                    LogUtil.e("起点", "起点:经度--" + mStartPoint.getLongitude() + "纬度--" + mStartPoint.getLatitude());
                } else {
                    mEndPoint = address.getLatLonPoint();
                    LogUtil.e("终点", "终点:经度--" + mEndPoint.getLongitude() + "纬度--" + mEndPoint.getLatitude());
                }
                if (!TextUtils.isEmpty(mStart.getText().toString()) && !TextUtils.isEmpty(mEnd.getText().toString()) && !TextUtils.isEmpty(mBinding.chooseTime.getText())) {
                    LogUtil.e("地点", "起点:经度--" + mStartPoint.getLongitude() + "纬度--" + mStartPoint.getLatitude() + "...终点:经度--" + mEndPoint.getLongitude() + "纬度--" + mEndPoint.getLatitude());
                    toSearch();
                }
                String addressName = "经纬度值:" + address.getLatLonPoint() + "\n位置描述:"
                        + address.getFormatAddress();
                Toast.makeText(this, address.getFormatAddress(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "无结果", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, rCode, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        //此处要调用，否则App在后台运行时，会无法截获
        OpenInstall.getWakeUp(intent, this);
    }

    @Override
    public void onWakeUpFinish(AppData appData, Error error) {
        if (error == null) {
            Log.d("MainActivity", "wakeup = " + appData.toString());
        } else {
            Log.d("MainActivity", "error : " + error.toString());
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient == null) {
            aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    AMapUtil.convertToLatLng(address.getLatLonPoint()), 25));
            mLocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mLocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mLocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
        locationOption = null;
    }

    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //继承了Activity的onTouchEvent方法，直接监听点击事件
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //当手指按下的时候
            x1 = event.getX();
            y1 = event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            //当手指离开的时候
            x2 = event.getX();
            y2 = event.getY();
            if (y1 - y2 > 5) {
                Toast.makeText(MainActivity.this, "向上滑", Toast.LENGTH_SHORT).show();
            } else if (y2 - y1 > 5) {
                Toast.makeText(MainActivity.this, "向下滑", Toast.LENGTH_SHORT).show();
            } else if (x1 - x2 > 5) {
                Toast.makeText(MainActivity.this, "向左滑", Toast.LENGTH_SHORT).show();
            } else if (x2 - x1 > 5) {
                Toast.makeText(MainActivity.this, "向右滑", Toast.LENGTH_SHORT).show();
            }
            //关闭软键盘
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
            }
        }
        return super.onTouchEvent(event);
    }
}
