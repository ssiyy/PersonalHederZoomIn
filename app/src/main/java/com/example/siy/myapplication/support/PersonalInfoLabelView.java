package com.example.siy.myapplication.support;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.util.Pair;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.siy.myapplication.R;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 任务标签视图
 * <p>
 * Created by Siy on 2018/12/20.
 *
 * @author Siy
 */
public class PersonalInfoLabelView extends FrameLayout {

    /**
     * 空消息提示
     */
    private View mEmptyViewTv;

    /**
     * 用户印象
     */
    private View mToMoreLabel;

    /**
     * 用户标签总布局
     */
    private ViewGroup mLabelInfoVg;

    /**
     * 添加标签的绝对布局
     */
    private AbsoluteLayout mLableAL;

    /**
     * 底部bar
     */
    private View mBottomBar;

    /**
     * 人物形象展示
     */
    private ImageView mMeInfoIcon;

    /**
     * 近50场胜率
     */
    private TextView mWinReate;

    /**
     * 近50场胜率的布局
     */
    private ViewGroup mWinReateVg;

    /**
     * 评价数
     */
    private TextView mCommon;

    /**
     * 综合评分
     */
    private TextView mScore;

    /**
     * 标签可能存在的位置
     */
    private List<List<Point>> lablePotionts;

    /**
     * 标签可能存在的颜色
     */
    private List<Integer> lableColors;

    /**
     * 底部标签bar的颜色,根据分数区间分为[1,2),[2,3),[3,4),[4,4.5),[4.5,5]
     */
    private SparseArray<GradientDrawable> mLabelBottomBarColors;

    /**
     * 任务标签形象展示，根据分数区间分为[1,2),[2,3),[3,4),[4,4.5),[4.5,5]
     * <p>
     * Pair-fist 男   Pair-second 女
     */
    private SparseArray<Pair<Integer, Integer>> mLabelPersoanlIcon;

    public PersonalInfoLabelView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public PersonalInfoLabelView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PersonalInfoLabelView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        if (inflater == null) {
            return;
        }

        initLabelPoints();
        initLabelColors();
        initLabelBottomBar();
        initLabelPersoanlIcon();

        View view = inflater.inflate(R.layout.me_info_label_layout, this, true);
        mEmptyViewTv = view.findViewById(R.id.empty_tv_id);
        mToMoreLabel = view.findViewById(R.id.tv_to_more_label);
        mLabelInfoVg = view.findViewById(R.id.label_layout);
        mLableAL = view.findViewById(R.id.al_lables);
        mBottomBar = view.findViewById(R.id.me_info_below_bar);
        mMeInfoIcon = view.findViewById(R.id.me_info_icon);
        mWinReateVg = view.findViewById(R.id.win_rate_ll);
        mWinReate = view.findViewById(R.id.win_rate);
        mCommon = view.findViewById(R.id.commmon_num);
        mScore = view.findViewById(R.id.me_info_evlution);

        mScore.setTypeface(TxtUtils.getTypeface(context, "bebas_neue_old.ttf"));
        mCommon.setTypeface(TxtUtils.getTypeface(context, "bebas_neue_old.ttf"));
        mWinReate.setTypeface(TxtUtils.getTypeface(context, "bebas_neue_old.ttf"));
    }

    /**
     * 显示空视图
     */
    public void showEmptyView(boolean show) {
        mEmptyViewTv.setVisibility(show ? View.VISIBLE : View.GONE);
        mToMoreLabel.setVisibility(show ? View.GONE : View.VISIBLE);
        mLabelInfoVg.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    /**
     * 设置标签
     * 标签赞同数据越多，体积越大，分为3个区域：前20%，前20%~60%，前60%~100%
     *
     * @param tags
     */
    public void setLablels(List<Tag> tags) {
        if (EmptyUtil.isCollectionEmpty(tags)) {
            return;
        }

        mLableAL.removeAllViews();
        Observable.just(tags)
                .flatMap(new Function<List<Tag>, Observable<Pair<Float, Tag>>>() {
                    @Override
                    public Observable<Pair<Float, Tag>> apply(List<Tag> tags) throws Exception {
                        float totalCount = 0;
                        for (Tag tag : tags) {
                            int count = tag.getCount();
                            totalCount = totalCount + count;
                        }

                        List<Pair<Float, Tag>> list = new ArrayList<>();
                        for (Tag tag : tags) {
                            list.add(Pair.create(tag.getCount() / totalCount, tag));
                        }
                        return Observable.fromIterable(list);
                    }
                }).map(new Function<Pair<Float, Tag>, Pair<Float, Tag>>() {
            @Override
            public Pair<Float, Tag> apply(Pair<Float, Tag> floatTagPair) throws Exception {
                //标签赞同数据越多，体积越大，分为3个区域：前20%，前20%~60%，前60%~100%
                float percent = floatTagPair.first;
                float multiple;
                //,1.2(20%,60%],1.4(60%,100%]
                if (percent >= 0 && percent < 0.2) {
                    // 1[0,20%)
                    multiple = 1f;
                } else if (percent >= 0.2 && percent < 0.6) {
                    //1.2(20%,60%]
                    multiple = 1.2f;
                } else if (percent >= 0.6 && percent <= 1) {
                    multiple = 1.4f;
                } else {
                    multiple = 1f;
                }
                return Pair.create(multiple, floatTagPair.second);
            }
        }).toList().toObservable()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<Pair<Float, Tag>>>() {
                    @Override
                    public void onNext(List<Pair<Float, Tag>> bean) {
                        super.onNext(bean);
                        //获取10以内的6个随机数
                        int[] colorRandomNun = RandomUtils.randomNums(10, 6);
                        List<Integer> randomColors = new ArrayList<>();
                        for (int i : colorRandomNun) {
                            if (i < lableColors.size()) {
                                randomColors.add(lableColors.get(i));
                            }
                        }

                        //5套位置随机取一套
                        List<Point> randomPoints = lablePotionts.get(RandomUtils.randomNums(5, 1)[0]);

                        if (randomColors.size() == randomPoints.size()) {
                            for (int i = 0; i < bean.size(); i++) {
                                if (i < randomColors.size()) {
                                    Pair<Float, Tag> pair = bean.get(i);
                                    if (pair != null) {
                                        Float multiple = pair.first;
                                        Tag tag = pair.second;
                                        addLable(tag, randomPoints.get(i), randomColors.get(i), multiple);
                                    }
                                }
                            }
                        }
                    }
                });

    }

    /**
     * 人物形象和底色随着评分更改
     * 分为1~2分，2~3,3~4,4~4.5,4.5~5
     *
     * @param score  设置评分
     * @param isMale 是否是男生
     */
    public void setScore(final Double score, final boolean isMale) {
        Observable.just(score == null ? 0 : score)
                .map(new Function<Double, Integer>() {
                    @Override
                    public Integer apply(Double aFloat) throws Exception {
                        int level;
                        if (aFloat < 2) {
                            level = 1;
                        } else if (aFloat >= 2 && aFloat < 3) {
                            level = 2;
                        } else if (aFloat >= 3 && aFloat < 4) {
                            level = 3;
                        } else if (aFloat >= 4 && aFloat < 4.5) {
                            level = 4;
                        } else {
                            //aFloat>=4.5
                            level = 5;
                        }
                        return level;
                    }
                }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<Integer>() {
                    @Override
                    public void onNext(Integer bean) {
                        super.onNext(bean);
                        mBottomBar.setBackground(mLabelBottomBarColors.get(bean));
                        Pair<Integer, Integer> infoIconPair = mLabelPersoanlIcon.get(bean);
                        mMeInfoIcon.setImageResource(isMale ? infoIconPair.first : infoIconPair.second);
                        if (score == null) {
                            String str = "暂无";
                            SpannableStringBuilder ssb = new SpannableStringBuilder(str);
                            ssb.setSpan(new RelativeSizeSpan(0.7f), 0, str.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                            mScore.setText(ssb);
                        } else {
                            mScore.setText(String.valueOf(score));
                        }
                    }
                });
    }

    /**
     * 设置评价数
     *
     * @param commonStr
     */
    public void setCommont(String commonStr) {
        mCommon.setText(commonStr);
    }

    /**
     * 设置近50场胜率
     *
     * @param winrateStr null 或者""就隐藏
     */
    public void setWinRate(CharSequence winrateStr) {
        if (EmptyUtil.isStringEmpty(winrateStr)) {
            mWinReateVg.setVisibility(View.GONE);
        } else {
            mWinReateVg.setVisibility(View.VISIBLE);
            mWinReate.setText(winrateStr);
        }
    }

    /**
     * 初始化标签的位置
     */
    private void initLabelPoints() {
        if (lablePotionts == null) {
            lablePotionts = new ArrayList<>(5);
        }

        List<Point> point1s = new ArrayList<>(6);
        point1s.add(new Point(d2p(16), d2p(32)));
        point1s.add(new Point(d2p(0), d2p(81)));
        point1s.add(new Point(d2p(5), d2p(166)));
        point1s.add(new Point(d2p(156), d2p(40)));
        point1s.add(new Point(d2p(145), d2p(111)));
        point1s.add(new Point(d2p(121), d2p(154)));
        lablePotionts.add(point1s);

        List<Point> point2s = new ArrayList<>(6);
        point2s.add(new Point(d2p(11), d2p(74)));
        point2s.add(new Point(d2p(8), d2p(175)));
        point2s.add(new Point(d2p(156), d2p(40)));
        point2s.add(new Point(d2p(163), d2p(101)));
        point2s.add(new Point(d2p(129), d2p(131)));
        point2s.add(new Point(d2p(129), d2p(193)));
        lablePotionts.add(point2s);

        List<Point> point3s = new ArrayList<>(6);
        point3s.add(new Point(d2p(44), d2p(2)));
        point3s.add(new Point(d2p(2), d2p(89)));
        point3s.add(new Point(d2p(12), d2p(145)));
        point3s.add(new Point(d2p(148), d2p(71)));
        point3s.add(new Point(d2p(163), d2p(101)));
        point3s.add(new Point(d2p(129), d2p(157)));
        lablePotionts.add(point3s);

        List<Point> point4s = new ArrayList<>(6);
        point4s.add(new Point(d2p(44), d2p(33)));
        point4s.add(new Point(d2p(2), d2p(89)));
        point4s.add(new Point(d2p(12), d2p(145)));
        point4s.add(new Point(d2p(165), d2p(58)));
        point4s.add(new Point(d2p(145), d2p(103)));
        point4s.add(new Point(d2p(131), d2p(139)));
        lablePotionts.add(point4s);

        List<Point> point5s = new ArrayList<>(6);
        point5s.add(new Point(d2p(44), d2p(14)));
        point5s.add(new Point(d2p(41), d2p(110)));
        point5s.add(new Point(d2p(12), d2p(145)));
        point5s.add(new Point(d2p(148), d2p(21)));
        point5s.add(new Point(d2p(150), d2p(58)));
        point5s.add(new Point(d2p(131), d2p(139)));
        lablePotionts.add(point5s);
    }

    /**
     * 初始化标签的颜色
     */
    private void initLabelColors() {
        if (lableColors == null) {
            lableColors = new ArrayList<>(10);
        }

        lableColors.add(Color.parseColor("#FF7995FD"));
        lableColors.add(Color.parseColor("#FF79EAFD"));
        lableColors.add(Color.parseColor("#FF79FDD1"));
        lableColors.add(Color.parseColor("#FFC9FD79"));
        lableColors.add(Color.parseColor("#FFEBFD79"));
        lableColors.add(Color.parseColor("#FFFDAF79"));
        lableColors.add(Color.parseColor("#FFFD7979"));
        lableColors.add(Color.parseColor("#FF79C0FD"));
        lableColors.add(Color.parseColor("#FFDA79FD"));
        lableColors.add(Color.parseColor("#FF79FD83"));
    }

    /**
     * 初始化底部标签bar的颜色
     */
    private void initLabelBottomBar() {
        if (mLabelBottomBarColors == null) {
            mLabelBottomBarColors = new SparseArray<>(5);
        }
        for (int i = 1; i <= 5; i++) {
            mLabelBottomBarColors.put(i, createLevelBottomLabelBar(i));
        }
    }

    /**
     * 根据不同等级生成不同的bottomBar
     *
     * @param level
     * @return
     */
    private GradientDrawable createLevelBottomLabelBar(int level) {
        GradientDrawable gd = new GradientDrawable();
        gd.setOrientation(GradientDrawable.Orientation.RIGHT_LEFT);
        switch (level) {
            case 1:
                gd.setColors(new int[]{Color.parseColor("#FFCCCCCC"), Color.parseColor("#FFC8C8C8"), Color.parseColor("#FFB5B5B5")});
                return gd;
            case 2:
                gd.setColors(new int[]{Color.parseColor("#FFCCCCCC"), Color.parseColor("#FFC8C8C8"), Color.parseColor("#FFB5B5B5")});
                return gd;
            case 3:
                gd.setColors(new int[]{Color.parseColor("#FFCDA192"), Color.parseColor("#FFF1C9B1"), Color.parseColor("#FFD9B7A5")});
                return gd;
            case 4:
                gd.setColors(new int[]{Color.parseColor("#FFE8E8E8"), Color.parseColor("#FFECECEC"), Color.parseColor("#FFD9D9D9")});
                return gd;
            case 5:
                gd.setColors(new int[]{Color.parseColor("#FFE5C370"), Color.parseColor("#FFFAD79E"), Color.parseColor("#FFF3BF7D")});
                return gd;
            default:
                gd.setColors(new int[]{Color.parseColor("#FFCCCCCC"), Color.parseColor("#FFC8C8C8"), Color.parseColor("#FFB5B5B5")});
                return gd;
        }
    }

    /**
     * 初始化标签任务形象
     */
    private void initLabelPersoanlIcon() {
        if (mLabelPersoanlIcon == null) {
            mLabelPersoanlIcon = new SparseArray<>(5);
        }

        mLabelPersoanlIcon.put(1, Pair.create(R.drawable.personal_boy_label_1_2, R.drawable.personal_girl_label_1_2));
        mLabelPersoanlIcon.put(2, Pair.create(R.drawable.personal_boy_label_2_3, R.drawable.personal_girl_label_2_3));
        mLabelPersoanlIcon.put(3, Pair.create(R.drawable.personal_boy_label_3_4, R.drawable.personal_girl_label_3_4));
        mLabelPersoanlIcon.put(4, Pair.create(R.drawable.personal_boy_label_4_45, R.drawable.personal_girl_label_4_45));
        mLabelPersoanlIcon.put(5, Pair.create(R.drawable.personal_boy_label_45_5, R.drawable.personal_girl_label_45_5));
    }

    /**
     * 添加一个标签
     *
     * @param tag
     * @param point
     * @param color
     * @param multiple
     */
    private void addLable(Tag tag, Point point, int color, float multiple) {
        if (tag == null) {
            return;
        }
        AbsoluteLayout.LayoutParams aLayoutParams = new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, point.x, point.y);
        TextView tv = new TextView(getContext());
        tv.setPadding(d2p((int) (8 * multiple)), d2p((int) (4 * multiple)), d2p((int) (8 * multiple)), d2p((int) (4 * multiple)));
        tv.setText(tag.getTagName());
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12 * multiple);
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setBackground(getLabelBg(color));
        tv.setTextColor(Color.BLACK);
        mLableAL.addView(tv, aLayoutParams);
    }

    /**
     * 获取label的标签
     *
     * @param color
     * @return
     */
    private Drawable getLabelBg(int color) {
        GradientDrawable bgDrawable = new GradientDrawable();
        bgDrawable.setColor(color);
        bgDrawable.setStroke(d2p(2), Color.parseColor("#FF282828"));
        bgDrawable.setCornerRadius(d2p(2));
        return bgDrawable;
    }

    /**
     * dip转px
     */
    private int d2p(int dip) {
        return UIUtil.dip2px(getContext(), dip);
    }
}
