package com.example.siy.myapplication.support;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 懒加载Fragment,用来配合ViewPager使用
 * <p>
 * Created by Siy on 2018/11/06.
 *
 * @author Siy
 * @see <a href="https://blog.csdn.net/mr_immortalz/article/details/51015196 ">Fragment实现懒加载，让应用更优化</a>
 */
public abstract class BaseLazyFragment extends Fragment {

    protected Context mContext;

    /**
     * 记录上一次可见的Fragment
     */
    private Fragment lastChildrenVisibleFragment;

    /**
     * 是否是第一次显示
     */
    private boolean isFirstVisible = true;

    /**
     * 是否是第一次隐藏
     */
    private boolean isFirstInvisible = true;

    /**
     * 是否是第一次onResume
     */
    private boolean isFirstResume = true;

    /**
     * 是否是第一准备
     */
    private boolean isPrepared;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPrepare();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getContentView(inflater, container);
        if (view != null) {
            return view;
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewsAndEvents(view);
    }

    private synchronized void initPrepare() {
        if (isPrepared) {
            onFirstUserVisible();
        } else {
            isPrepared = true;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        changChildrenVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isFirstVisible) {
                isFirstVisible = false;
                initPrepare();
            } else {
                onUserVisible();
            }
        } else {
            if (isFirstInvisible) {
                isFirstInvisible = false;
                onFirstUserInvisible();
            } else {
                onUserInvisible();
            }
        }
    }

    /**
     * 更改嵌套子Fragment的用户可见状态
     *
     * @param isVisibleToUser
     */
    private void changChildrenVisibleHint(boolean isVisibleToUser) {
        if (!isAdded()) {
            return;
        }
        if (isVisibleToUser) {
            //如果是变的可见
            if (lastChildrenVisibleFragment != null && lastChildrenVisibleFragment.isAdded()) {
                lastChildrenVisibleFragment.setUserVisibleHint(true);
            }
        } else {
            //如果是变的不可见
            FragmentManager fm = getChildFragmentManager();
            List<Fragment> fragments = fm.getFragments();
            if (EmptyUtil.isCollectionNotEmpty(fragments)) {
                for (Fragment fragment : fragments) {
                    if (fragment.getUserVisibleHint()) {
                        lastChildrenVisibleFragment = fragment;
                        fragment.setUserVisibleHint(false);
                    }
                }
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (isFirstResume) {
            isFirstResume = false;
            return;
        }

        if (getUserVisibleHint()) {
            onUserVisible();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getUserVisibleHint()) {
            onUserInvisible();
        }
    }

    /**
     * 可以用来绑定视图
     *
     * @param view
     */
    protected abstract void initViewsAndEvents(View view);

    /**
     * 获取布局视图,如果不想传入布局的资源id，可以重写{@link #getContentView(LayoutInflater, ViewGroup)}
     *
     * @return
     */
    protected abstract int getContentViewLayoutId();

    /**
     * 获取Fragment的布局视图
     *
     * @param inflater
     * @param container
     * @return
     */
    protected View getContentView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        if (getContentViewLayoutId() != 0) {
            return inflater.inflate(getContentViewLayoutId(), container, false);
        }
        return null;
    }

    /**
     * Fragment第一次不可见时回调，可以在这里 加载数据 / 开启动画 / 广播.....
     */
    protected void onFirstUserVisible() {
    }

    /**
     * Fragment可见时(>1)回调，可以在这里开启动画 / 广播.....
     */
    protected void onUserVisible() {
    }

    /**
     * Fragment不见时(>1)回调，可以在这里暂停动画 / 暂停广播.....
     */
    protected void onUserInvisible() {
    }

    /**
     * Fragment第一次不可见时回到,一般不用
     */
    protected void onFirstUserInvisible() {
    }

}
