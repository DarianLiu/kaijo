package com.geek.kaijo.Utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

/**
 * android 常用方法
 * Created by Administrator on 2015/4/15.
 */
public class AndroidUtil {

    /**
     * 设置字体大小
     *
     * @param textView
     * @param str1
     * @param size1
     * @param str2
     * @param size2
     */
    public static void setTextSize(@NonNull TextView textView, @NonNull String str1, int size1, @NonNull String str2, int size2) {
        SpannableStringBuilder style = new SpannableStringBuilder(str1 + str2);
        style.setSpan(new AbsoluteSizeSpan(size1, true), 0, str1.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        style.setSpan(new AbsoluteSizeSpan(size2, true), str1.length(), str1.length() + str2.length(),
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        textView.setText(style);
    }

    /**
     * 设置文本风格
     *
     * @param str1
     * @param size1
     * @param color1
     * @param str2
     * @param size2
     * @param color2
     * @return
     */
    @NonNull
    public static SpannableStringBuilder setTextStyle(@NonNull String str1, int size1, int color1, @NonNull String str2, int size2, int color2) {
        SpannableStringBuilder style = new SpannableStringBuilder(str1 + str2);
        style.setSpan(new AbsoluteSizeSpan(size1, true), 0, str1.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        style.setSpan(new AbsoluteSizeSpan(size2, true), str1.length(), str1.length() + str2.length(),
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        style.setSpan(new ForegroundColorSpan(color1), 0, str1.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        style.setSpan(new ForegroundColorSpan(color2), str1.length(), str1.length() + str2.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return style;
    }

    /**
     * 设置textview 颜色和文字大小 注意：保持后面的数组大小一样
     *
     * @param textView
     * @param strs
     * @param colors
     * @param textSizes
     */
    public static void setTextSizeColor(@NonNull TextView textView, @NonNull String[] strs, @Nullable int[] colors, @Nullable int[] textSizes) {
        StringBuilder builder = new StringBuilder();
        for (String str : strs) {
            builder.append(str);
        }
        SpannableStringBuilder style = new SpannableStringBuilder(builder.toString());
        int count = strs.length;
        int startIndex = 0;
        int endIndex = 0;
        for (int i = 0; i < count; i++) {
            endIndex += strs[i].length();
            if (colors != null) {
                style.setSpan(new ForegroundColorSpan(colors[i]), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            }
            if (textSizes != null) {
                style.setSpan(new AbsoluteSizeSpan(textSizes[i], true), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            }
            startIndex += strs[i].length();
        }
        textView.setText(style);
    }

}