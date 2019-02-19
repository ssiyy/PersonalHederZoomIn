package com.example.siy.myapplication.support;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Siy on 2018/12/09.
 *
 * @author Siy
 */
public class TxtUtils {
    /**
     * 获取字体
     *
     * @param context
     * @param txtName
     * @return
     */
    public static Typeface getTypeface(Context context, String txtName) {
        return Typeface.createFromAsset(context.getAssets(), txtName);
    }
}
