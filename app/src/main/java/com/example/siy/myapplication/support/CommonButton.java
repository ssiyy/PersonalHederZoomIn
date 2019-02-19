package com.example.siy.myapplication.support;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * author: liquan
 * created on: 2018/12/10 0010
 * 通用按钮，为了使用padding来改变文字的位置...
 */
public class CommonButton extends AppCompatTextView implements View.OnTouchListener{

    private int mPaddingBottom;

    public CommonButton(Context context) {
        super(context);
        init();
    }

    public CommonButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CommonButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    private void init() {
        setOnTouchListener(this);
        mPaddingBottom = getPaddingBottom();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.setPadding(0, 0 + 0, 0, 0);
                break;
            case MotionEvent.ACTION_MOVE:
                Rect r = new Rect();
                view.getLocalVisibleRect(r);
                if (!r.contains((int) event.getX(), (int) event.getY()) &&
                        !r.contains((int) event.getX(), (int) event.getY())) {
                    this.setPadding(0, 0 + 0, 0, mPaddingBottom);
                }
                break;
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                this.setPadding(0, 0 + 0, 0, mPaddingBottom);
                break;
        }
        return false;
    }
}
