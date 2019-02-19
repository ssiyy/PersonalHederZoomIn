package com.example.siy.myapplication;

import android.arch.lifecycle.Lifecycle;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.siy.myapplication.appbarlayout_support_25_3_1.AppBarLayout;
import com.example.siy.myapplication.loopingviewpager.CommonViewPager;
import com.example.siy.myapplication.support.EmptyUtil;
import com.example.siy.myapplication.support.FragmentViewPagerAdapter;
import com.example.siy.myapplication.support.ImageIndicator;
import com.example.siy.myapplication.support.ScrollableIndicator;
import com.example.siy.myapplication.support.ServiceBean;
import com.example.siy.myapplication.support.ServiceInfoEmptyFragment;
import com.example.siy.myapplication.support.WalletNumberTextView;

import java.util.ArrayList;
import java.util.List;
public class MainActivity extends AppCompatActivity {

    /**
     * 守护榜前三名展示布局
     */
    private ViewGroup mGuardListVg;

    /**
     * 游戏选择器下面的细线
     */
    private View mGameIndicatiorline;

    /**
     * 游戏指示器
     */
    private ScrollableIndicator<ServiceBean> mGameIndicator;

    /**
     * 送出的礼物的数量
     */
    private WalletNumberTextView mGiftGiveNum;

    /**
     * 收到礼物的数量
     */
    private WalletNumberTextView mCollectGiftNum;

    /**
     * 底部按钮条
     */
    private ViewGroup mBottomBarView;

    /**
     * 聊一聊
     */
    private View mTalkWith;

    /**
     * 立即邀请
     */
    private View mIntive;

    /**
     * 加载错误视图
     */
    private ViewGroup mErrLayout;

    /**
     * 加载成功视图
     */
    private ViewGroup mContentLayout;

    /**
     * 右边按钮
     */
    private ImageView mRightIv;

    /**
     * 标题
     */
    private TextView mTitle;

    /**
     * 用户姓名
     */
    private TextView mUserName;

    /**
     * 鸡牌号
     */
    private TextView mChickenId;

    /**
     * 性别
     */
    private ImageView mSex;

    /**
     * 我的等级
     */
    private TextView mLevel;

    /**
     * 我的地址
     */
    private TextView mAddres;

    /**
     * 我的粉丝
     */
    private TextView mFans;

    /**
     * 我的订单信息
     */
    private TextView mOrderMsg;

    /**
     * 我的关系
     */
    private TextView mRelation;

    /**
     * 个性签名所在的布局
     */
    private ViewGroup mSignatureVg;

    /**
     * 个性签名
     */
    private TextView mSignature;

    /**
     * 游戏技能详细信息展示的ViewPager
     */
    private ViewPager mGameViewPager;

    /**
     * 暴娘相册展示
     */
    private CommonViewPager mPersoalFigures;

    /**
     * 相册展示的指示器
     */
    private ImageIndicator mImageIndicator;

    /**
     * 返回按钮图片
     */
    private ImageView mBackView;

    /**
     * 注册一个监听title渐变的事件
     */
    private void registerTitleGradientListener() {
        final View mTitleBg = findViewById(R.id.id_titlebg);
        ((AppBarLayout) findViewById(R.id.appbarLayout)).addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                float alpha = (float) Math.abs(i) / (float) appBarLayout.getTotalScrollRange();
                mTitleBg.setAlpha(alpha);
                mBackView.setColorFilter(alpha == 0 ? Color.WHITE : Color.parseColor("#FF282828"));
                mRightIv.setColorFilter(alpha == 0 ? Color.WHITE : Color.parseColor("#FF282828"));
                if (alpha == 0) {
                    mBackView.setAlpha(1f);
                    mRightIv.setAlpha(1f);
                } else {
                    mBackView.setAlpha(alpha);
                    mRightIv.setAlpha(alpha);

                }
                mTitle.setAlpha(alpha);
            }
        });
    }


    private void initView() {
        mGuardListVg = findViewById(R.id.to_guard_list);
        mCollectGiftNum = findViewById(R.id.gift_collect_num);
        mGiftGiveNum = findViewById(R.id.gift_give_num);
        mBottomBarView = findViewById(R.id.bottom_button_bar);
        mTalkWith = findViewById(R.id.talk_with);
        mIntive = findViewById(R.id.invitate_with);
        mErrLayout = findViewById(R.id.err_layout);
        mContentLayout = findViewById(R.id.content);
        mBackView = findViewById(R.id.back_iv);
        mRightIv = findViewById(R.id.iv_edit);
        mTitle = findViewById(R.id.title);
        mUserName = findViewById(R.id.personal_name);
        mChickenId = findViewById(R.id.personal_chicken_id);
        mSex = findViewById(R.id.sex);
        mLevel = findViewById(R.id.level);
        mAddres = findViewById(R.id.personal_addr);
        mFans = findViewById(R.id.fans);
        mOrderMsg = findViewById(R.id.ordernum);
        mRelation = findViewById(R.id.personal_relation);
        mSignatureVg = findViewById(R.id.personal_ll);
        mSignature = findViewById(R.id.personal_signature);
        mGameIndicatiorline = findViewById(R.id.game_indicator_line);
        mGameIndicator = findViewById(R.id.game_indicator);
        mGameViewPager = findViewById(R.id.game_viewpager);
        mGameIndicator.attachViewPager(mGameViewPager);
        mPersoalFigures = findViewById(R.id.personal_figure);
        mImageIndicator = findViewById(R.id.personal_figure_indicator);

        mSignature.setMaxLines(3);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        registerTitleGradientListener();
        showServices();
        showServiceInfo();
        showPersonalFigures();
    }

    /**
     * 显示个人形象图片
     */
    private void showPersonalFigures() {

        final List<Integer> mPersoalImgFigures = new ArrayList<>();
        mPersoalImgFigures.add(R.drawable.abc);
        mPersoalImgFigures.add(R.drawable.def);

        if (EmptyUtil.isCollectionEmpty(mPersoalImgFigures)) {
            return;
        }

        if (mPersoalImgFigures.size() == 1) {
            mImageIndicator.setVisibility(View.GONE);
            mPersoalFigures.setCanScroll(false);
        } else {
            mImageIndicator.init(mPersoalImgFigures.size(), 0);
            mPersoalFigures.setCanScroll(true);
        }


        mPersoalFigures.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return mPersoalImgFigures.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, final int position) {
                View view = getLayoutInflater().inflate(R.layout.common_banner_item, null, false);
                ImageView view1 = view.findViewById(R.id.imageview);
                view1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                view.setBackgroundResource(mPersoalImgFigures.get(position));
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }
        });
    }


    /**
     * 显示技能列表
     */
    private void showServices() {
        List<ServiceBean> list = new ArrayList<>(5);
        ServiceBean sb1 = new ServiceBean();
        sb1.setGameCode("1");
        sb1.setGameName("王者荣耀");
        list.add(sb1);

        ServiceBean sb2 = new ServiceBean();
        sb2.setGameCode("2");
        sb2.setGameName("王者荣耀");
        list.add(sb2);

        ServiceBean sb3 = new ServiceBean();
        sb3.setGameCode("3");
        sb3.setGameName("王者荣耀");
        list.add(sb3);


        ServiceBean sb4 = new ServiceBean();
        sb4.setGameCode("4");
        sb4.setGameName("王者荣耀");
        list.add(sb4);


        ServiceBean sb5 = new ServiceBean();
        sb5.setGameCode("5");
        sb5.setGameName("王者荣耀");
        list.add(sb5);

        if (EmptyUtil.isCollectionNotEmpty(list)) {
            mGameIndicatiorline.setVisibility(View.VISIBLE);
            mGameIndicator.setItems(list);
        } else {
            mGameIndicatiorline.setVisibility(View.GONE);
        }
    }

    /**
     * 显示技能详情
     */
    private void showServiceInfo() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(ServiceInfoEmptyFragment.newInstance());
        mGameViewPager.setAdapter(new FragmentViewPagerAdapter(getSupportFragmentManager(), fragments));
    }

}
