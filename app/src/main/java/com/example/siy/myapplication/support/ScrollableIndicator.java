package com.example.siy.myapplication.support;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;


import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.List;

/**
 * Created by Siy on 2018/12/20.
 *
 * @author Siy
 */
public class ScrollableIndicator<T> extends MagicIndicator {

    private CommonNavigatorAdapter mAdater;

    private List<T> mItems;

    private ViewPager mViewPager;

    /**
     * 每个item的点击事件
     */
    private OnItemClickListener mItemClickListener;

    public ScrollableIndicator(Context context) {
        super(context);
        init();
    }

    public ScrollableIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 绑定ViewPager
     *
     * @param viewPager
     */
    public void attachViewPager(ViewPager viewPager) {
        ViewPagerHelper.bind(this, viewPager);
        mViewPager = viewPager;
    }

    public void setItems(List<T> items) {
        this.mItems = items;
        mAdater.notifyDataSetChanged();
    }

    private void init() {
        setBackgroundColor(Color.TRANSPARENT);
        CommonNavigator navigator = new CommonNavigator(getContext());
        navigator.setAdapter(mAdater = new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mItems != null ? mItems.size() : 0;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int i) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context) {
                    @Override
                    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
                        super.onLeave(index, totalCount, leavePercent, leftToRight);
                        setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    }

                    @Override
                    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
                        super.onEnter(index, totalCount, enterPercent, leftToRight);
                        setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    }
                };
                simplePagerTitleView.setNormalColor(Color.parseColor("#FF8F8F8F"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#FF282828"));
                simplePagerTitleView.setGravity(Gravity.LEFT);
                simplePagerTitleView.setTypeface(Typeface.DEFAULT_BOLD);
                simplePagerTitleView.setText(mItems.get(i).toString());
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mViewPager != null) {
                            mViewPager.setCurrentItem(i);
                        }

                        if (mItemClickListener != null) {
                            mItemClickListener.onItemClick(i);
                        }

                    }
                });

                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                linePagerIndicator.setColors(Color.parseColor("#FFFF6B47"));
                return linePagerIndicator;
            }
        });
        setNavigator(navigator);
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    /**
     * tab item的点击事件
     */
    interface OnItemClickListener {
        /**
         * 点击item时回调
         *
         * @param position
         */
        void onItemClick(int position);
    }
}
