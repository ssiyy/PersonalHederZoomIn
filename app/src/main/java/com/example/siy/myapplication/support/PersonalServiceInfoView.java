package com.example.siy.myapplication.support;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.example.siy.myapplication.R;

/**
 * 技能详情的自定义视图，主要用来处理滑动冲突
 * <p>
 * Created by Siy on 2019/01/02.
 *
 * @author Siy
 */
public class PersonalServiceInfoView extends FrameLayout {

    /**
     * 按下去的坐标
     */
    private float lastX, lastY;

    /**
     * 移动范围，判断是否是点击事件
     */
    private float limit;

    /**
     * 显示的空View
     */
    private View mEmptyView;

    /**
     * 用户印象的点击按钮
     */
    private View mPersonalInfoView;

    /**
     * 技能认证图片
     */
    private View mServiceInfoView;

    /**
     * 点击用户印象点击事件
     */
    public OnClickListener onPersonalFurgtureClick;

    /**
     * 点击技能图片
     */
    public OnClickListener onServiceInfoClick;

    public PersonalServiceInfoView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public PersonalServiceInfoView(@NonNull  Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PersonalServiceInfoView(@NonNull  Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        if (inflater == null) {
            return;
        }
        limit = DeviceUtil.dipToPx(context, 5);
        inflater.inflate(R.layout.me_activity_game_label, this, true);
        mEmptyView = findViewById(R.id.empty_tv_id);
        mPersonalInfoView = findViewById(R.id.tv_to_more_label_rl);
        mServiceInfoView = findViewById(R.id.service_img);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        try {//加此异常捕捉函数，主要是在部分机型ViewPatch和多点触控结合使用时会出现崩溃问题
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                lastX = event.getRawX();
                lastY = event.getRawY();
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (Math.abs(lastX - event.getRawX()) < limit && Math.abs(lastY - event.getRawY()) < limit) {
                    if (inRangeOfView(mPersonalInfoView, event)) {
                        //点击用户印象
                        if (mEmptyView.getVisibility() == View.GONE) {
                            onPersonalFurgture(mPersonalInfoView);
                        }
                    } else if (inRangeOfView(mServiceInfoView, event)) {
                        //点击技能图片
                        onServiceInfoView(mServiceInfoView);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 单击用户印象
     *
     * @param view
     */
    private void onPersonalFurgture(View view) {
        if (onPersonalFurgtureClick != null) {
            onPersonalFurgtureClick.onClick(view);
        }
    }

    /**
     * 点击技能详情图片
     *
     * @param view
     */
    private void onServiceInfoView(View view) {
        if (onServiceInfoClick != null) {
            onServiceInfoClick.onClick(view);
        }
    }

    /**
     * 设置单击用户印象
     *
     * @param onPersonalFurgtureClick
     */
    public void setOnPersonalFurgtureClick(OnClickListener onPersonalFurgtureClick) {
        this.onPersonalFurgtureClick = onPersonalFurgtureClick;
    }

    /**
     * 设置点击技能图片事件
     *
     * @param onServiceInfoClick
     */
    public void setOnServiceInfoClick(OnClickListener onServiceInfoClick) {
        this.onServiceInfoClick = onServiceInfoClick;
    }

    /**
     * 判断是否点击下载按钮
     *
     * @param view
     * @param ev
     * @return
     */
    private boolean inRangeOfView(View view, MotionEvent ev) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        if (ev.getRawX() < x || ev.getRawX() > (x + view.getWidth()) || ev.getRawY() < y || ev.getRawY() > (y + view.getHeight())) {
            return false;
        }
        return true;
    }
}
