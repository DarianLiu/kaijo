package com.geek.kaijo.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.geek.kaijo.R;

/**
 * 自定义加载中dialog
 *
 * @author Jason
 */
public class LoadingProgressDialog extends Dialog {

    private ObjectAnimator anim;

    private LoadingProgressDialog(@NonNull Builder builder, int themeResId) {
        super(builder.context, themeResId);
        LayoutInflater inflater = LayoutInflater.from(builder.context);
        View view = inflater.inflate(R.layout.dialog_loading, null);
        ImageView progressCircleImg = view.findViewById(R.id.iv_loading);
        anim = ObjectAnimator.ofFloat(progressCircleImg, "rotation", 0f, 360f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatMode(ValueAnimator.RESTART);
        anim.setRepeatCount(-1);

        TextView progressTextTxt = view.findViewById(R.id.tv_loading);
        progressTextTxt.setText(builder.message);
        setContentView(view);
        setCancelable(builder.isCancelable);
        setCanceledOnTouchOutside(builder.isCancelOutside);
    }

    public static class Builder {
        private Context context;
        private String message;
        private boolean isCancelable;
        private boolean isCancelOutside;

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * 设置提示信息
         *
         * @param message 提示信息
         */
        @NonNull
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * 设置是否可以按返回键取消
         *
         * @param isCancelable 返回键取消
         */
        @NonNull
        public Builder setCancelable(boolean isCancelable) {
            this.isCancelable = isCancelable;
            return this;
        }

        /**
         * 设置是否可以取消
         *
         * @param isCancelOutside 取消
         */
        @NonNull
        public Builder setCancelOutside(boolean isCancelOutside) {
            this.isCancelOutside = isCancelOutside;
            return this;
        }

        @NonNull
        public LoadingProgressDialog create() {
            return new LoadingProgressDialog(this, R.style.LoadingProgressDialog);
        }


    }

    public void show() {
        super.show();
        if (!anim.isStarted() && !anim.isRunning()) {
            anim.start();
        }
    }

    public void dismiss() {
        super.dismiss();
        if (anim.isStarted() || anim.isRunning())
            anim.cancel();
    }
}