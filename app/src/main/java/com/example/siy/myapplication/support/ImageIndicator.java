package com.example.siy.myapplication.support;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.siy.myapplication.MyApplication;
import com.example.siy.myapplication.R;


/**
 * Created by ChenLei on 2017/12/28 0028.
 */

public class ImageIndicator extends View {

    private static final String TAG = ImageIndicator.class.getSimpleName();


    private Config mConfig = new Config();

    public ImageIndicator(Context context) {
        this(context, null);
    }

    public ImageIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }

    private Paint mPaint = new Paint();

    {
        mPaint.setAntiAlias(true);
    }

    public void setNums(int nums) {
        mConfig.num = nums;
        invalidate();
    }


    private void initAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ImageIndicatorStyle);
            mConfig.selectedColor = typedArray.getColor(R.styleable.ImageIndicatorStyle_selected_color, mConfig.selectedColor);
            mConfig.unSelectedColor = typedArray.getColor(R.styleable.ImageIndicatorStyle_unselected_color, mConfig.unSelectedColor);
            mConfig.interval = typedArray.getDimension(R.styleable.ImageIndicatorStyle_interval, mConfig.interval);
            mConfig.width = typedArray.getDimension(R.styleable.ImageIndicatorStyle_width, mConfig.width);
            mConfig.selected = typedArray.getInt(R.styleable.ImageIndicatorStyle_selected, mConfig.selected);
            mConfig.num = typedArray.getInt(R.styleable.ImageIndicatorStyle_num, mConfig.num);
            typedArray.recycle();
        }
    }


    public void setConfig(Config config) {
        mConfig = config;
        requestLayout();
    }

    public void setColor(int selectedColor, int unSelectedColor) {
        mConfig.selectedColor = selectedColor;
        mConfig.unSelectedColor = unSelectedColor;
        invalidate();
    }

    public void init(int num, int index) {
        mConfig.num = num;
        mConfig.selected = index;
        requestLayout();
    }

    public void setIndicatorSize(int size) {
        mConfig.width = size;
        requestLayout();
    }

    public void setInterval(int interval) {
        mConfig.interval = interval;
        requestLayout();
    }


    public void setIndex(int index) {
        if (index == mConfig.selected) {
            return;
        }
        if (index > mConfig.num - 1) {
            return;
        }
        mConfig.selected = index;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    /*    int widthMode = MeasureSpec.getMode(widthMeasureSpec);   //获取宽的模式
        int heightMode = MeasureSpec.getMode(heightMeasureSpec); //获取高的模式
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);   //获取宽的尺寸
        int heightSize = MeasureSpec.getSize(heightMeasureSpec); //获取高的尺寸
        LogUtil.d(TAG, "宽的模式:" + widthMode);
        LogUtil.d(TAG, "高的模式:" + heightMode);
        LogUtil.d(TAG, "宽的尺寸:" + widthSize);
        LogUtil.d(TAG, "高的尺寸:" + heightSize);*/
        int width;
        int height;
      /*  if (widthMode == MeasureSpec.EXACTLY) {
            //如果match_parent或者具体的值，直接赋值
            width = widthSize;
        } else {
            //如果是wrap_content，我们要得到控件需要多大的尺寸
            float textWidth = mBound.width();   //文本的宽度
            //控件的宽度就是文本的宽度加上两边的内边距。内边距就是padding值，在构造方法执行完就被赋值
            width = (int) (getPaddingLeft() + textWidth + getPaddingRight());
        }
        //高度跟宽度处理方式一样
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            float textHeight = mBound.height();
            height = (int) (getPaddingTop() + textHeight + getPaddingBottom());
        }*/
        //保存测量宽度和测量高度
        width = (int) Math.ceil((mConfig.num - 1) * (mConfig.width + mConfig.interval) + mConfig.width);
        height = (int) Math.ceil(mConfig.width);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float radius = mConfig.width /2;
        float posX;
        for (int i = 0; i < mConfig.num; i++) {
            if (i == mConfig.selected) {
                mPaint.setColor(mConfig.selectedColor);
            } else {
                mPaint.setColor(mConfig.unSelectedColor);
            }
            posX =i * (mConfig.width + mConfig.interval) +radius;
            canvas.drawCircle(posX, radius, mConfig.width / 2, mPaint);
        }
    }

    public static class Config {
        public int unSelectedColor = Color.parseColor("#66FFFFFF");
        public int selectedColor = Color.WHITE;
        public float width = DeviceUtil.dipToPx(MyApplication.getInstance(), 3);
        public float interval = DeviceUtil.dipToPx(MyApplication.getInstance(), 6);
        public int selected = -1;
        public int num = 0;
    }
}
