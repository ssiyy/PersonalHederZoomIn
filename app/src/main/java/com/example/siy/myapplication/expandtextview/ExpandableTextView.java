package com.example.siy.myapplication.expandtextview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;

/**
 * 带有展开的TextView
 * <p>
 * Created by Siy on 2019/01/09.
 *
 * @author Siy
 */
public class ExpandableTextView extends AppCompatTextView {

    /**
     * 的右侧文字Drawable
     */
    private TextDrawable mTextDrawable;

    /**
     * 点击了展开按钮
     */
    private boolean clickExpanded = false;

    /**
     * 允许输入的最大行数
     */
    private volatile int mMaxline = -1;

    public ExpandableTextView(Context context) {
        this(context, null);

    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public ExpandableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mTextDrawable = new TextDrawable(context);
        String str = "「展开」";
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#9891FB")), 1, str.length() - 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        mTextDrawable.setText(spannableString);
        mTextDrawable.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        mTextDrawable.setTextColor(Color.parseColor("#FF282828"));
        mTextDrawable.setTextAlign(Layout.Alignment.ALIGN_CENTER);
        mTextDrawable.setBounds(0, 0, mTextDrawable.getIntrinsicWidth(), mTextDrawable.getIntrinsicHeight());
    }

    @Override
    public void setMaxLines(int maxLines) {
        mMaxline = maxLines;
    }


    private void setTextDrawableVisible(boolean visible) {
        Drawable rightDrawble = visible ? mTextDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], rightDrawble, getCompoundDrawables()[3]);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ExpandableTextView.super.setMaxLines(mMaxline < 0 ? Integer.MAX_VALUE : mMaxline);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int lineCouunt = getLineCount();
        if (lineCouunt > 1) {
            setGravity(Gravity.LEFT);
            if (clickExpanded) {
                //点击过展开按钮就不再显示展开按钮了,直接设置最大行数
                ExpandableTextView.super.setMaxLines(mMaxline < 0 ? Integer.MAX_VALUE : mMaxline);
                setTextDrawableVisible(false);
            } else {
                //没有点击过展开按钮
                ExpandableTextView.super.setMaxLines(1);
                setTextDrawableVisible(true);
            }
        } else {
            setGravity(Gravity.CENTER);
            setTextDrawableVisible(false);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCompoundDrawables()[2] != null) {
                float x = event.getX();
                float padRight = getPaddingRight();
                float iconWidth = getCompoundDrawables()[2].getIntrinsicWidth();
                boolean touchAble = (x > (getWidth() - padRight - iconWidth)) && (x < getWidth() - padRight);

                if (touchAble) {
                    clickExpanded = true;
                    requestLayout();

                }
            }
        }

        return super.onTouchEvent(event);
    }
}
