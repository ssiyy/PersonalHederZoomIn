package com.example.siy.myapplication.loopingviewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.lang.reflect.Field;

/**
 * Created by lanhongming on 2017/1/6 0006.
 * <p>
 * Modify By Siy
 * 通用viewpager
 *
 * @author lanhongming
 */
public class CommonViewPager extends ViewPager {

    /**
     * 是否可以滑动,默认可以滑动
     */
    private boolean noScroll = false;

    public CommonViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommonViewPager(Context context) {
        super(context);
    }

    /**
     * 设置是否可以滑动
     *
     * @param canScroll true 可以滑动，false 不可以滑动
     */
    public void setCanScroll(boolean canScroll) {
        this.noScroll = !canScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        return !noScroll && super.onTouchEvent(arg0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return !noScroll && super.onInterceptTouchEvent(arg0);
    }


    /**
     * 初始化当前ViewPager的位置，之所以写这个方法，是为了防止当设置初始item不是1的情况下还是调用
     * 第一个Fragment的setUserVisibleHint(true)
     *
     * @param item    初始位置
     * @param adapter 适配器
     */
    public void initCurrentItem(int item, PagerAdapter adapter) {
        try {
            Class c = ViewPager.class;
            Field f = c.getDeclaredField("mCurItem");
            f.setAccessible(true);
            f.setInt(this, item);
            f.setAccessible(false);
            setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
