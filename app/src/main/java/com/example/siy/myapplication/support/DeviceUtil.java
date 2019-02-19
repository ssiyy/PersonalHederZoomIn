package com.example.siy.myapplication.support;


import android.content.Context;

public class DeviceUtil {


    /**
     * 将dip值转成px
     *
     * @param context
     * @param dip
     * @return
     */
    public static float dipToPx(Context context, float dip) {
        return dip * getDeviceDensity(context) + 0.5f;
    }
    /**
     * 获取设备屏幕密度,像素的比例
     *
     * @param context
     * @return
     */
    public static float getDeviceDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

}
