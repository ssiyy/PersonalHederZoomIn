package com.example.siy.myapplication.support;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;



/**
 * 用來控制LinearLayout的绘制顺序
 *
 * Created by Siy on 2019/01/08.
 *
 * @author Siy
 */
public class OrderLinearlayout extends LinearLayout {
    public OrderLinearlayout(Context context) {
        super(context);
        setChildrenDrawingOrderEnabled(true);
    }

    public OrderLinearlayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setChildrenDrawingOrderEnabled(true);
    }

    public OrderLinearlayout(Context context, @Nullable  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setChildrenDrawingOrderEnabled(true);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        return childCount-1-i;
    }
}
