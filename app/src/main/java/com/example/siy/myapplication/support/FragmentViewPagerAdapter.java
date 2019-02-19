package com.example.siy.myapplication.support;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * VeiwPager的页面是Fragment的适配器，这个是配置支持对Fragment的增加和删除,如果对页面增加删除需要调用{@link #notifyDataSetChangedWithBaseId()}
 * 这个来通知适配器
 * <p>
 * Created by Siy on 2018/08/06.
 *
 * @author Siy
 */
public class FragmentViewPagerAdapter extends FragmentPagerAdapter {
    private long baseId = 0;

    /**
     * 新的fragment
     */
    private List<Fragment> mFragments;

    /**
     * 过期的fragment
     */
    private List<Fragment> mOldFragment;

    /**
     * 設置新的數據
     *
     * @param fragments
     */
    public void setNewDatas(List<Fragment> fragments) {
        if (fragments == null) {
            mFragments = new ArrayList<>();
        } else {
            if (EmptyUtil.isCollectionNotEmpty(mFragments)) {
                mOldFragment.addAll(mFragments);
            }
            mFragments = fragments;
        }
        baseId = baseId + mFragments.size();
    }

    /**
     * 增加數據
     *
     * @param fragments
     */
    public void addDatas(List<Fragment> fragments) {
        mOldFragment.addAll(mFragments);
        mFragments.addAll(fragments);
        baseId = baseId + mFragments.size();
    }

    public FragmentViewPagerAdapter(FragmentManager fm) {
        super(fm);
        mOldFragment = new ArrayList<>();
    }

    public FragmentViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        this(fm);
        setNewDatas(fragments);
    }

    @Override
    public Fragment getItem(int position) {
        if (EmptyUtil.isCollectionEmpty(mFragments) || position < 0 || position >= mFragments.size()) {
            return null;
        }
        return mFragments.get(position);
    }

    /**
     * 获取fragment class所在的位置
     *
     * @param clazz
     * @return
     */
    public int getPostion(Class<? extends Fragment> clazz) {
        if (EmptyUtil.isCollectionNotEmpty(mFragments)) {
            for (int i = 0; i < mFragments.size(); i++) {
                if (mFragments.get(i).getClass() == clazz) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 获取fragment class所在的位置
     *
     * @param fragment
     * @return
     */
    public int getPostion(Fragment fragment) {
        if (EmptyUtil.isCollectionNotEmpty(mFragments)) {
            for (int i = 0; i < mFragments.size(); i++) {
                if (mFragments.get(i) == fragment) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if (isOldFragment(object)) {
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

    /**
     * 如果是老页面就从新实例化
     *
     * @param object
     * @return
     */
    private boolean isOldFragment(Object object) {
        if (EmptyUtil.isCollectionNotEmpty(mOldFragment)) {
            for (Fragment f : mOldFragment) {
                if (f == object) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public long getItemId(int position) {
        return baseId + position;
    }

    /**
     * 根据baseId刷新数据，在改变fragment数据时用
     */
    public void notifyDataSetChangedWithBaseId() {
        this.baseId = this.baseId + 1;
        notifyDataSetChanged();
    }
}
