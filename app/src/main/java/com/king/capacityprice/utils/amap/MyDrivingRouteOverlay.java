package com.king.capacityprice.utils.amap;

import android.content.Context;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DrivePath;
import com.king.capacityprice.R;
import com.king.capacityprice.overlay.DrivingRouteOverlay;

/**
 * Created by wuxin on 2016/7/1.
 * 自定义路线样式
 */
public class MyDrivingRouteOverlay extends DrivingRouteOverlay {
    public int color;//路线颜色
    public float lineWidth;//路线宽度

    @Override
    public float getRouteWidth() {
        return lineWidth;
    }

    @Override
    protected int getDriveColor() {
        return color;
    }

    public MyDrivingRouteOverlay(Context arg0, AMap arg1, DrivePath arg2, LatLonPoint arg3,
                                 LatLonPoint arg4) {
        super(arg0, arg1, arg2, arg3, arg4, null);
    }

    /*
    修改终点marker样式
    */
    @Override
    protected BitmapDescriptor getEndBitmapDescriptor() {
        BitmapDescriptor reBitmapDescriptor = new BitmapDescriptorFactory().fromResource(R.drawable.ic_mapstart);
        return reBitmapDescriptor;
    }

    /*修改起点marker样式*/
    @Override
    protected BitmapDescriptor getStartBitmapDescriptor() {
        BitmapDescriptor reBitmapDescriptor = new BitmapDescriptorFactory().fromResource(R.drawable.ic_mapend);

        return reBitmapDescriptor;
    }
    /*修改中间点marker样式*/

    @Override
    protected BitmapDescriptor getDriveBitmapDescriptor() {
        BitmapDescriptor reBitmapDescriptor = null;
        if (reBitmapDescriptor == null) {
            reBitmapDescriptor = new BitmapDescriptorFactory().fromResource(R.drawable.amap_car);
        }
        return reBitmapDescriptor;
    }

    /*一个工具方法，修改颜色和宽度*/
    public void setView(int color, float width) {
        this.color = color;
        lineWidth = width;
    }
}
