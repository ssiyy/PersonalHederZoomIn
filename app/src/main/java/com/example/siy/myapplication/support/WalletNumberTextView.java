package com.example.siy.myapplication.support;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;

import java.text.DecimalFormat;

/**
 * Created by Siy on 2019/01/15.
 *
 * @author Siy
 */
public class WalletNumberTextView extends AppCompatTextView {

    /**
     * 设置金额显示格式，总是保留2位小数(如果有小数)
     * <p>
     * <pre>
     * 设置各种币显示格式
     *
     * 0 一个数字
     * # 一个数字，不包括 0
     * . 小数的分隔符的占位符
     * , 分组分隔符的占位符
     * ; 分隔格式。
     * - 缺省负数前缀。
     * % 乘以 100 和作为百分比显示
     * </pre>
     */
    public static final String FORMAT_NUM_DECIMAL_IF_HAVE = "##.#";

    /**
     * 默认阈值
     */
    private int thresholdValue = 10000;

    /**
     * 阈值默认后缀
     */
    private String thresholdValueSuffix = "w";

    /**
     * 后缀
     */
    private String mSuffix = "";

    private DecimalFormat mDecimalFormat;

    public WalletNumberTextView(Context context) {
        super(context);
        init(context);
    }

    public WalletNumberTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WalletNumberTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mDecimalFormat = new DecimalFormat(FORMAT_NUM_DECIMAL_IF_HAVE);
        setTypeface(TxtUtils.getTypeface(context, "bebas_neue_old.ttf"));
    }

    /**
     * 设置超过阈值之后显示的后缀
     *
     * @param thresholdValueSuffix
     */
    public void setThresholdValueSuffix(String thresholdValueSuffix) {
        this.thresholdValueSuffix = thresholdValueSuffix;
    }

    /**
     * 设置阈值
     *
     * @param thresholdValue
     */
    public void setThresholdValue(int thresholdValue) {
        this.thresholdValue = thresholdValue;
    }

    /**
     * 设置后缀
     *
     * @param suffix
     */
    public void setSuffix(String suffix) {
        this.mSuffix = suffix;
    }

    private CharSequence getAddSuffix(double number) {
        String s;
        String suffix;
        if (number >= thresholdValue) {
            double f = number / thresholdValue;
            String str = mDecimalFormat.format(f);
            suffix = thresholdValueSuffix + mSuffix;
            s = str + suffix;
        } else {
            String str = mDecimalFormat.format(number);
            suffix = mSuffix;
            s = str + suffix;
        }

        int startIndex = s.indexOf(suffix);
        int endIndex = startIndex + suffix.length();
        SpannableString spannableString = new SpannableString(s);
        spannableString.setSpan(new RelativeSizeSpan(0.5f), startIndex, endIndex, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);

        return spannableString;
    }

    /**
     * 设置数值
     *
     * @param number
     */
    public void setNumber(double number) {
        setText(getAddSuffix(number));
    }

    /**
     * 设置数值
     *
     * @param positive true 代表正 false 代表负
     * @param number
     */
    public void setNumber(boolean positive, double number) {
        String s;
        String suffix;
        if (number >= thresholdValue) {
            double f = number / thresholdValue;
            String str = mDecimalFormat.format(f);
            suffix = thresholdValueSuffix + mSuffix;
            s = str + suffix;
        } else {
            String str = mDecimalFormat.format(number);
            suffix = mSuffix;
            s = str + suffix;
        }

        s = (positive ? "+" : "-") + s;
        int startIndex = s.indexOf(suffix);
        int endIndex = startIndex + suffix.length();
        SpannableString spannableString = new SpannableString(s);
        spannableString.setSpan(new RelativeSizeSpan(0.5f), startIndex, endIndex, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        setText(spannableString);
    }
}
