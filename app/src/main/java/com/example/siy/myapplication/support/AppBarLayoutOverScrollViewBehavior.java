package com.example.siy.myapplication.support;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.example.siy.myapplication.appbarlayout_support_25_3_1.AppBarLayout;


/**
 * 用来控制个人主页图片的缩放
 * <p>
 * Created by Siy on 2018/12/19.
 *
 * @author Siy
 */
public class AppBarLayoutOverScrollViewBehavior extends AppBarLayout.Behavior {

    /**
     * 图片上面的个人信息的
     */
    private static final String TAG_PERSONAL_INFO_MSG = "personal_info_msg";

    /**
     * 可以縮放的view
     */
    private static final String TAG_OVER_SCROLL = "overScroll";

    /**
     * 缩放布局后面需要下移的view
     */
    private static final String TAG_MIDDLE = "middle";

    /**
     * 个人信息布局
     */
    private ViewGroup mPersonalInfoLayout;

    /**
     * 个人信息布局的高
     */
    private int mPersonalInfoHeight;

    /**
     * 个人签名布局
     */
    private ViewGroup middleLayout;

    /**
     * 个性签名布局的高
     */
    private int mMiddleHeight;

    /**
     * 需要缩放的视图
     */
    private View mTargetView;

    /**
     * AppBarLayout 的高
     */
    private int mParentHeight;

    /**
     * 需要缩放视图的高
     */
    private int mTargetViewHeight;

    /**
     * 是否可以执行动画
     */
    private boolean isAnimate;

    /**
     * 是否正在自动回弹中
     */
    private boolean isRecovering = false;

    private float mTotalDy;

    private float mLastScale;

    private int mLastBottom;

    /**
     * 达到这个下拉临界值就开始刷新动画
     */
    private final float MAX_REFRESH_LIMIT = 0.3f;

    private static final float TARGET_HEIGHT = 1500;

    private OnProgressChangeListener onProgressChangeListener;

    public AppBarLayoutOverScrollViewBehavior() {
        super();
    }

    public AppBarLayoutOverScrollViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void initView(View view, AppBarLayout abl) {
        if (mPersonalInfoLayout == null) {
            mPersonalInfoLayout = view.findViewWithTag(TAG_PERSONAL_INFO_MSG);
        }

        if (middleLayout == null) {
            middleLayout = view.findViewWithTag(TAG_MIDDLE);
        }
        // 需要在调用过super.onLayoutChild()方法之后获取
        if (mTargetView == null) {
            mTargetView = view.findViewWithTag(TAG_OVER_SCROLL);
        }

        if (mTargetView != null) {
            initial(abl);
        }

    }

    private void initial(AppBarLayout abl) {
        abl.setClipChildren(false);
        mParentHeight = abl.getHeight();
        mTargetViewHeight = mTargetView.getHeight();
        mMiddleHeight = middleLayout.getHeight();
        mPersonalInfoHeight = mPersonalInfoLayout.getHeight();
    }


    /**
     * AppBarLayout布局时调用
     *
     * @param parent          父布局CoordinatorLayout
     * @param abl             使用此Behavior的AppBarLayout
     * @param layoutDirection 布局方向
     * @return 返回true表示子View重新布局，返回false表示请求默认布局
     */
    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, AppBarLayout abl, int layoutDirection) {
        boolean handled = super.onLayoutChild(parent, abl, layoutDirection);
        initView(parent, abl);
        return handled;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout parent, AppBarLayout child, View directTargetChild, View target, int nestedScrollAxes) {
        isAnimate = true;
        if (target instanceof DisInterceptNestedScrollView) {
            //这个布局就是middleLayout
            return true;
        }
        return super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed) {
        if (!isRecovering) {
            if (mTargetView != null && ((dy < 0 && child.getBottom() >= mParentHeight) || (dy > 0 && child.getBottom() > mParentHeight))) {
                scale(child, target, dy);
                return;
            }
        }
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);

    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX, float velocityY) {
        if (velocityY > 100) {
            //当y速度>100,就秒弹回
            isAnimate = false;
        }
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout abl, View target) {
        recovery(abl);
        super.onStopNestedScroll(coordinatorLayout, abl, target);
    }

    private void scale(AppBarLayout abl, View target, int dy) {
        mTotalDy += -dy;
        mTotalDy = Math.min(mTotalDy, TARGET_HEIGHT);
        mLastScale = Math.max(1f, 1f + mTotalDy / TARGET_HEIGHT);
        ViewCompat.setScaleX(mTargetView, mLastScale);
        ViewCompat.setScaleY(mTargetView, mLastScale);
        mLastBottom = mParentHeight + (int) (mTargetViewHeight / 2 * (mLastScale - 1));
        abl.setBottom(mLastBottom);
        target.setScrollY(0);

        middleLayout.setTop(mLastBottom - mMiddleHeight);
        middleLayout.setBottom(mLastBottom);

        mPersonalInfoLayout.setBottom(mLastBottom - mMiddleHeight);
        mPersonalInfoLayout.setTop(mLastBottom - mMiddleHeight - mPersonalInfoHeight);

        if (onProgressChangeListener != null) {
            //计算0~1的进度
            float progress = Math.min((mLastScale - 1) / MAX_REFRESH_LIMIT, 1);
            onProgressChangeListener.onProgressChange(progress, false);
        }
    }

    private void recovery(final AppBarLayout abl) {
        if (isRecovering) {
            return;
        }
        if (mTotalDy > 0) {
            isRecovering = true;
            mTotalDy = 0;
            if (isAnimate) {
                ValueAnimator anim = ValueAnimator.ofFloat(mLastScale, 1f).setDuration(200);
                anim.addUpdateListener(
                        new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                float value = (float) animation.getAnimatedValue();
                                ViewCompat.setScaleX(mTargetView, value);
                                ViewCompat.setScaleY(mTargetView, value);
                                abl.setBottom((int) (mLastBottom - (mLastBottom - mParentHeight) * animation.getAnimatedFraction()));
                                middleLayout.setTop((int) (mLastBottom - (mLastBottom - mParentHeight) * animation.getAnimatedFraction() - mMiddleHeight));
                                mPersonalInfoLayout.setTop((int) (mLastBottom - (mLastBottom - mParentHeight) * animation.getAnimatedFraction() - mMiddleHeight) - mPersonalInfoHeight);

                                if (onProgressChangeListener != null) {
                                    //计算0~1的进度
                                    float progress = Math.min((value - 1) / MAX_REFRESH_LIMIT, 1);
                                    onProgressChangeListener.onProgressChange(progress, true);
                                }
                            }
                        }
                );
                anim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isRecovering = false;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
                anim.start();
            } else {
                ViewCompat.setScaleX(mTargetView, 1f);
                ViewCompat.setScaleY(mTargetView, 1f);
                abl.setBottom(mParentHeight);
                middleLayout.setTop(mParentHeight - mMiddleHeight);
                mPersonalInfoLayout.setTop(mParentHeight - mMiddleHeight - mPersonalInfoHeight);

                isRecovering = false;

                if (onProgressChangeListener != null) {
                    onProgressChangeListener.onProgressChange(0, true);
                }
            }
        }
    }

    /**
     * 回弹进度监听
     */
    public interface OnProgressChangeListener {
        /**
         * 范围 0~1
         *
         * @param progress
         * @param isRelease 是否是释放状态
         */
        void onProgressChange(float progress, boolean isRelease);
    }

    /**
     * 设置缩放进度监听
     *
     * @param onProgressChangeListener
     */
    private void setOnProgressChangeListener(OnProgressChangeListener onProgressChangeListener) {
        this.onProgressChangeListener = onProgressChangeListener;
    }
}