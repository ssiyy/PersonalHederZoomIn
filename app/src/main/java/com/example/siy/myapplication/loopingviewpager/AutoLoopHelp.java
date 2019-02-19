package com.example.siy.myapplication.loopingviewpager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;


import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

/**
 * 用来帮助自动轮播的
 *
 * Created by Siy on 2019/01/17.
 *
 * @author Siy
 */
public class AutoLoopHelp {

    private LoopViewPager mLoopViewPager;

    private Context mContext;

    /**
     * 用来保存初始的{@link android.support.v4.view.ViewPager#mScroller}
     */
    private Scroller cacheMScroller;

    /**
     * 自己设置切换速度
     */
    private int mCustomDuration = -1;

    private AutoLoopHandler mAutoLoopHandler;

    public AutoLoopHelp(@NonNull LoopViewPager viewPager) {
        this.mLoopViewPager = viewPager;
        this.mContext = viewPager.getContext();
        mAutoLoopHandler = new AutoLoopHandler(new WeakReference<AutoLoopHelp>(this));

        mLoopViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            /**
             * 用来实现轮播的暂停和恢复
             * @param state
             */
            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        mAutoLoopHandler.sendEmptyMessage(AutoLoopHandler.MSG_KEEP_SILENT);
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        Log.e("siy", "SCROLL_STATE_IDLE");
                        mAutoLoopHandler.sendEmptyMessage(AutoLoopHandler.MSG_BREAK_SILENT);
                        break;
                    default:
                        break;
                }
            }
        });
    }


    /**
     * 开始轮播
     *
     * @param sec 轮播时间间隔
     */
    public void startTurning(long sec) {
        if (mAutoLoopHandler.getLoopState()) {
            //是停止状态才开始轮播
            mAutoLoopHandler.setLoopTime(sec);
            mAutoLoopHandler.setLoopState(false);
            mAutoLoopHandler.sendEmptyMessage(AutoLoopHandler.MSG_BREAK_SILENT);
        }
    }

    /**
     * 停止轮播
     */
    public void stopTurning() {
        mAutoLoopHandler.setLoopState(true);
        mAutoLoopHandler.sendEmptyMessage(AutoLoopHandler.MSG_KEEP_SILENT);
    }

    /**
     * 主要是在轮播时修改切换速度
     *
     * @param duration
     */
    public void setDuration(int duration) {
        if (duration != mCustomDuration && duration > 0) {
            mCustomDuration = duration;
            FixedSpeedScroller scroller = new FixedSpeedScroller(mContext, new AccelerateInterpolator());
            scroller.setmDuration(duration);
            setDuration(scroller);
        }
    }

    /**
     * 还原之前的{@link ViewPager#mScroller}
     */
    public void reSetDuration() {
        if (cacheMScroller != null && mCustomDuration != -1) {
            mCustomDuration = -1;
            setDuration(cacheMScroller);
        }
    }

    private void setDuration(Scroller scroller) {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            //在设置之前保存一下原来的scroller
            if (cacheMScroller == null) {
                //只有当cacheMScroller是null的时候才保存
                cacheMScroller = (Scroller) field.get(mLoopViewPager);
            }
            field.set(mLoopViewPager, scroller);
            field.setAccessible(false);
        } catch (Exception e) {

        }
    }

    /**
     * 修改viewpager的滑动速度
     */
    private static class FixedSpeedScroller extends Scroller {

        /**
         * 滑动的时间
         */
        private int mDuration = 1500;

        public FixedSpeedScroller(Context context) {
            super(context);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        /**
         * 获取滑动时间间隔
         *
         * @return
         */
        public int getmDuration() {
            return mDuration;
        }

        /**
         * 设置滑动时间间隔
         *
         * @param duration
         */
        public void setmDuration(int duration) {
            this.mDuration = duration;
        }
    }

    /**
     * 控制ViewPager自动轮播的Handler
     */
    private static class AutoLoopHandler extends Handler {

        /**
         * 请求暂停轮播。
         */
        private static final int MSG_KEEP_SILENT = 1;

        /**
         * 请求恢复轮播。
         */
        private static final int MSG_BREAK_SILENT = 2;

        /**
         * 请求更新显示的View。
         */
        private static final int MSG_UPDATE_IMAGE = 3;

        /**
         * 停止轮播
         */
        private static final int MSG_STOP_HANDLER = 4;

        /**
         * 使用弱引用避免Handler泄露
         */
        private WeakReference<AutoLoopHelp> weakReference;

        /**
         * 轮播图片切换时间间隔
         */
        private static final int MSG_DURATION = 300;

        /**
         * 轮播间隔
         */
        private long mLoopTime = 2 * 1000;

        /**
         * Handler 状态
         */
        private boolean isStop = true;

        /**
         * 设置轮播间隔
         *
         * @param mLoopTime
         */
        private void setLoopTime(long mLoopTime) {
            this.mLoopTime = mLoopTime;
        }

        /**
         * 设置轮播状态
         *
         * @param isStop true 停止 false 开始轮播
         */
        private void setLoopState(boolean isStop) {
            this.isStop = isStop;
        }

        /**
         * 获取轮播状态
         *
         * @return true 是停止 false 是轮播中
         */
        public boolean getLoopState() {
            return isStop;
        }

        private AutoLoopHandler(WeakReference<AutoLoopHelp> help) {
            weakReference = help;
        }

        @Override
        public void handleMessage(Message msg) {
            AutoLoopHelp help = weakReference.get();
            if (help == null) {
                return;
            }

            if (hasMessages(MSG_UPDATE_IMAGE)) {
                removeMessages(MSG_UPDATE_IMAGE);
            }

            switch (msg.what) {
                case MSG_KEEP_SILENT:
                    //不再发送轮播消息
                    if (!isStop) {
                        //如果是silent状态就重置成原来的切换状态
                        help.reSetDuration();
                    }
                    break;
                case MSG_BREAK_SILENT:
                    if (!isStop) {
                        sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, mLoopTime);
                        //设置滑动时间间隔
                        help.setDuration(MSG_DURATION);
                    }
                    break;
                case MSG_STOP_HANDLER:
                    isStop = true;
                    break;
                case MSG_UPDATE_IMAGE:
                    if (!isStop) {
                        LoopViewPager viewPager = help.mLoopViewPager;
                        if (viewPager == null) {
                            return;
                        }
                        int curItem = viewPager.getCurrentItem();
                        PagerAdapter adapter = viewPager.getAdapter();
                        if (adapter != null) {
                            viewPager.setCurrentItem(curItem + 1);
                        }
                        sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, mLoopTime);
                    }
                    break;
                default:
            }
        }
    }
}
