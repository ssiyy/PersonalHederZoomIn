package com.example.siy.myapplication.support;

import android.view.View;

import com.example.siy.myapplication.R;

/**
 * 技能详情的空fragment
 * <p>
 * Created by Siy on 2018/12/30.
 *
 * @author Siy
 */
public class ServiceInfoEmptyFragment extends BaseLazyFragment {

    public static ServiceInfoEmptyFragment newInstance() {
        return new ServiceInfoEmptyFragment();
    }

    @Override
    protected void initViewsAndEvents(View view) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.me_activity_game_label_empty;
    }
}
