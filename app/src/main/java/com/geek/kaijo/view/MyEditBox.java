package com.geek.kaijo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geek.kaijo.R;

public class MyEditBox extends LinearLayout {
    //属性 :文本，颜色 大小
    private String attributeText;
    private int attributeTextColor;
    private float attributeTextSize;
    private Drawable attributeDrawableRight;

    //编辑框：hint文本（颜色） ,颜色，大小，背景
    private String attributeValueText;
    private int attributeValueTextColor;
    private String attributeValueTextHint;
    private int attributeValueTextHintColor;
    private float attributeValueTextSize;
    private Drawable attributeValueBg;
    private int attributeValueMaxLines;
    private int scrollBarType;
    private int attributeValueHeight;
    private int attributeValueHintGravity;


    private TextView attributeView;
    private EditText attributeValueView;

    private LayoutParams attributeParams, attributeValueParams;
    private boolean horizonta;

    public MyEditBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyEditbox);

        attributeText = typedArray.getString(R.styleable.MyEditbox_attributeText);
        attributeTextColor = typedArray.getColor(R.styleable.MyEditbox_attributeTextColor, 0);
        attributeTextSize = typedArray.getDimension(R.styleable.MyEditbox_attributeTextSize, 16);
        attributeDrawableRight = typedArray.getDrawable(R.styleable.MyEditbox_attributeDrawableRight);


        attributeValueText = typedArray.getString(R.styleable.MyEditbox_attributeValueText);
        attributeValueTextColor = typedArray.getColor(R.styleable.MyEditbox_attributeValueTextColor, 0);
        attributeValueTextHint = typedArray.getString(R.styleable.MyEditbox_attributeValueTextHint);
        attributeValueTextHintColor = typedArray.getColor(R.styleable.MyEditbox_attributeValueTextHintColor, 0);
        attributeValueTextSize = typedArray.getDimension(R.styleable.MyEditbox_attributeValueTextSize, 16);
        attributeValueBg = typedArray.getDrawable(R.styleable.MyEditbox_attributeValueBg);
        attributeValueMaxLines = typedArray.getInteger(R.styleable.MyEditbox_attributeValueMaxLines, Integer.MAX_VALUE);
        scrollBarType = typedArray.getInteger(R.styleable.MyEditbox_scrollBarType, 1);
        attributeValueHeight = (int) typedArray.getDimension(R.styleable.MyEditbox_attributeValueHeight, ViewGroup.LayoutParams.WRAP_CONTENT);
        attributeValueHintGravity = typedArray.getInteger(R.styleable.MyEditbox_attributeValueHintGravity, 0);

        typedArray.recycle();

        attributeView = new TextView(context);
        attributeValueView = new EditText(context);

        attributeView.setText(attributeText);
        attributeView.setTextColor(attributeTextColor);
        attributeView.setTextSize(attributeTextSize);
        attributeView.setTypeface(Typeface.DEFAULT_BOLD);
        if (attributeDrawableRight != null) {
            attributeDrawableRight.setBounds(0, 0, attributeDrawableRight.getMinimumWidth(), attributeDrawableRight.getMinimumHeight());
            attributeView.setCompoundDrawables(null, null, attributeDrawableRight, null);
        }
        attributeView.setCompoundDrawablePadding(8);

        attributeView.setPadding(0, 0, 0, 5);

        attributeValueView.setText(attributeValueText);
        attributeValueView.setTextColor(attributeValueTextColor);
        attributeValueView.setTextSize(attributeValueTextSize);
        attributeValueView.setHint(attributeValueTextHint);
        attributeValueView.setHintTextColor(attributeValueTextHintColor);
        attributeValueView.setBackgroundDrawable(attributeValueBg);
        attributeValueView.setMaxLines(attributeValueMaxLines);
        attributeValueView.setPadding(15, 12, 15, 12);
        attributeValueView.setGravity(attributeValueHintGravity);
        switch (scrollBarType) {
            case 1:
                attributeValueView.setHorizontallyScrolling(true);
                break;
            case 2:
                attributeValueView.setVerticalScrollBarEnabled(true);
                break;
        }

        attributeParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(attributeView, attributeParams);

        attributeValueParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, attributeValueHeight);
        addView(attributeValueView, attributeValueParams);

        setOrientation(VERTICAL);
    }

    /**
     * 设置编辑框值
     */
    public void setAttributeValueText(String text) {
        attributeValueView.setText(text);
    }

    /**
     * 获取编辑框值
     */
    public void getAttributeValueText() {
        attributeValueView.getText();
    }

}

