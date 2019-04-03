package com.geek.kaijo.Utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.geek.kaijo.R;

/**
 * Created by $ShenHai on 2017/4/28 0028.
 */

public class NormalDialog extends Dialog {
    public NormalDialog(@NonNull Context context) {
        super(context);
    }

    public NormalDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected NormalDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder implements View.OnClickListener {

        private Context mContext;
        private NormalDialog mDialog;
        private View mDialogLayout;
        private View mNormalBtnLayout;
        private View mThreeBtnLayout;
        private TextView mTvMsg;
        private Button mBtnPositive;
        private Button mBtnNegative;
        private Button mBtnTop;
        private Button mBtnMiddle;
        private Button mBtnBottom;

        private String mPositiveButtonText;
        private String mNegativeButtonText;
        private String mTopButtonText;
        private String mMiddleButtonText;
        private String mBottomButtonText;
        private String mMsgText;

        private OnClickListener mPositiveButtonClickListener;
        private OnClickListener mNegativeButtonClickListener;
        private OnClickListener mTopButtonClickListener;
        private OnClickListener mMiddleButtonClickListener;
        private OnClickListener mBottomButtonClickListener;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder setPositiveButton(int resourceId, OnClickListener listener) {
            mPositiveButtonText = (String) mContext.getText(resourceId);
            mPositiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText, OnClickListener listener) {
            mPositiveButtonText = positiveButtonText;
            mPositiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int resourceId, OnClickListener listener) {
            mNegativeButtonText = (String) mContext.getText(resourceId);
            mNegativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText, OnClickListener listener) {
            mNegativeButtonText = negativeButtonText;
            mNegativeButtonClickListener = listener;
            return this;
        }

        public Builder setTopButton(String topButtonText, OnClickListener listener) {
            mTopButtonText = topButtonText;
            mTopButtonClickListener = listener;
            return this;
        }

        public Builder setTopButton(int resourceId, OnClickListener listener) {
            mTopButtonText = (String) mContext.getText(resourceId);
            mTopButtonClickListener = listener;
            return this;
        }

        public Builder setMiddleButton(String middleButtonText, OnClickListener listener) {
            mMiddleButtonText = middleButtonText;
            mMiddleButtonClickListener = listener;
            return this;
        }

        public Builder setMiddleButton(int resourceId, OnClickListener listener) {
            mMiddleButtonText = (String) mContext.getText(resourceId);
            mMiddleButtonClickListener = listener;
            return this;
        }

        public Builder setBottomButton(String bottomButtonText, OnClickListener listener) {
            mBottomButtonText = bottomButtonText;
            mBottomButtonClickListener = listener;
            return this;
        }

        public Builder setBottomButton(int resourceId, OnClickListener listener) {
            mBottomButtonText = (String) mContext.getText(resourceId);
            mBottomButtonClickListener = listener;
            return this;
        }

        public Builder setMessage(String message) {
            if (message == null || message.trim().length() < 1) return null;
            mMsgText = message;
            return this;
        }

        public NormalDialog create() {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            initView(inflater);
            mDialog = new NormalDialog(mContext);
            mDialog.addContentView(mDialogLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            if (mPositiveButtonText != null || mNegativeButtonText != null) {
                mNormalBtnLayout.setVisibility(View.VISIBLE);
                mThreeBtnLayout.setVisibility(View.GONE);
            } else if (mTopButtonText != null || mMiddleButtonText != null || mBottomButtonText != null){
                mNormalBtnLayout.setVisibility(View.GONE);
                mThreeBtnLayout.setVisibility(View.VISIBLE);
            }

            if (mMsgText != null && mMsgText.trim().length() > 1) {
                mTvMsg.setText(mMsgText);
            }

            if (mPositiveButtonText != null) {
                mBtnPositive.setText(mPositiveButtonText);
                if (mPositiveButtonClickListener != null) {
                    mBtnPositive.setOnClickListener(this);
                } else {
                    // if no confirm button just set the visibility to GONE
                    mBtnPositive.setVisibility(View.GONE);
                }
            } else {
                mBtnPositive.setVisibility(View.GONE);
            }

            if (mNegativeButtonText != null) {
                mBtnNegative.setText(mNegativeButtonText);
                if (mNegativeButtonClickListener != null) {
                    mBtnNegative.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mNegativeButtonClickListener.onClick(mDialog, DialogInterface.BUTTON_NEGATIVE);
                        }
                    });
                } else {
                    mBtnNegative.setVisibility(View.GONE);
                }
            } else {
                mBtnNegative.setVisibility(View.GONE);
            }

            if (mTopButtonText != null) {
                mBtnTop.setText(mTopButtonText);
                if (mTopButtonClickListener != null) {
                    mBtnTop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mTopButtonClickListener.onClick(mDialog, DialogInterface.BUTTON_NEGATIVE);
                        }
                    });
                } else {
                    mBtnTop.setVisibility(View.GONE);
                }
            } else {
                mBtnTop.setVisibility(View.GONE);
            }

            if (mMiddleButtonText != null) {
                mBtnMiddle.setText(mMiddleButtonText);
                if (mMiddleButtonClickListener != null) {
                    mBtnMiddle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mMiddleButtonClickListener.onClick(mDialog, DialogInterface.BUTTON_NEGATIVE);
                        }
                    });
                } else {
                    mBtnMiddle.setVisibility(View.GONE);
                }
            } else {
                mBtnMiddle.setVisibility(View.GONE);
            }

            if (mBottomButtonText != null) {
                mBtnBottom.setText(mBottomButtonText);
                if (mBottomButtonClickListener != null) {
                    mBtnBottom.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mBottomButtonClickListener.onClick(mDialog, DialogInterface.BUTTON_NEGATIVE);
                        }
                    });
                } else {
                    mBtnBottom.setVisibility(View.GONE);
                }
            } else {
                mBtnBottom.setVisibility(View.GONE);
            }


            mDialog.setCancelable(false);
            return mDialog;
        }

        private void initView(LayoutInflater inflater) {
            mDialogLayout = inflater.inflate(R.layout.layout_dialog_normal, null);
//            mNormalBtnLayout = mDialogLayout.findViewById(com.rehab.bonwo.R.id.layout_dialog_btn_normal);
//            mThreeBtnLayout = mDialogLayout.findViewById(com.rehab.bonwo.R.id.layout_dialog_btn_three);
//            mTvMsg = (TextView) mDialogLayout.findViewById(com.rehab.bonwo.R.id.tv_dialog_message);
//            mBtnPositive = (Button) mDialogLayout.findViewById(com.rehab.bonwo.R.id.btn_positive);
//            mBtnPositive.setOnClickListener(this);
//            mBtnNegative = (Button) mDialogLayout.findViewById(com.rehab.bonwo.R.id.btn_negative);
//            mBtnNegative.setOnClickListener(this);
//            mBtnTop = (Button) mDialogLayout.findViewById(com.rehab.bonwo.R.id.btn_top);
//            mBtnTop.setOnClickListener(this);
//            mBtnMiddle = (Button) mDialogLayout.findViewById(com.rehab.bonwo.R.id.btn_middle);
//            mBtnMiddle.setOnClickListener(this);
//            mBtnBottom = (Button) mDialogLayout.findViewById(com.rehab.bonwo.R.id.btn_bottom);
            mBtnBottom.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
//                case com.rehab.bonwo.R.id.btn_positive:
//                    mPositiveButtonClickListener.onClick(mDialog, DialogInterface.BUTTON_POSITIVE);
//                    break;
//                case com.rehab.bonwo.R.id.btn_negative:
//                    mNegativeButtonClickListener.onClick(mDialog, DialogInterface.BUTTON_NEGATIVE);
//                    break;
//                case com.rehab.bonwo.R.id.btn_top:
//                    mTopButtonClickListener.onClick(mDialog, DialogInterface.BUTTON_NEGATIVE);
//                    break;
//                case com.rehab.bonwo.R.id.btn_middle:
//                    mMiddleButtonClickListener.onClick(mDialog, DialogInterface.BUTTON_NEGATIVE);
//                    break;
//                case com.rehab.bonwo.R.id.btn_bottom:
//                    mBottomButtonClickListener.onClick(mDialog, DialogInterface.BUTTON_NEGATIVE);
//                    break;
//                default:
//                    break;
            }
        }
    }
}
