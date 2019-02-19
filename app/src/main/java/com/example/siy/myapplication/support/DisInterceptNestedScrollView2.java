package com.example.siy.myapplication.support;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 禁止ScrollView的横向滑动
 * <p>
 * Created on 2018年12月31日17:12:53
 *
 * @author Siy
 */
public class DisInterceptNestedScrollView2 extends NestedScrollView {

    private float mPosY, mCurPosY, mPosX, mCurPosX;

    public DisInterceptNestedScrollView2(Context context) {
        super(context);
    }

    public DisInterceptNestedScrollView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DisInterceptNestedScrollView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPosY = ev.getY();
                mPosX = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                mCurPosY = ev.getY();
                mCurPosX = ev.getX();
                if (Math.abs(mCurPosY - mPosY) > Math.abs(mCurPosX - mPosX)) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
        }

        return super.dispatchTouchEvent(ev);
    }

}
