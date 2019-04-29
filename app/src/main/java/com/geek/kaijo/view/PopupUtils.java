package com.geek.kaijo.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;

/**
 * Created by $Liudan on 2018/4/8 0008.
 */

public class PopupUtils {
    private PopupWindowListener popupWindowListener;
    private PopupWindow popupWindow;
    private Context context;
    private float alpha = 1;
    boolean againAlpha = false; //删除弹出确认框时 背景透明控制
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    setBackgroundAlpha(context, (float) msg.obj);
                    break;
            }
        }
    };


    /**
     * 从控件位置往上
     *
     * @param parent
     * @param context
     */
    public <T> void showPopWindowFromViewToUp(Context context, View parent, View layout, T t, PopupWindowListener popupWindowListener, boolean isBottom) {
        this.context = context;
//        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = layoutInflater.inflate(layout, null);
//        view.getBackground().setAlpha(80); // 设置透明度0为完全透明，255为不透明
        if (layout != null) {
            if (popupWindowListener != null) {
                popupWindowListener.onInitView(layout, t);
            }
        }
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        if (popupWindow == null) {
            popupWindow = new PopupWindow(layout, RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
            // 重置PopupWindow高度
            popupWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//            int screenHeigh = context.getResources().getDisplayMetrics().heightPixels;
//            int popHeight = popupWindow.getContentView().getMeasuredHeight();
//            if (popHeight > Math.round(screenHeigh * 0.7f)) {
//                popupWindow.setHeight(Math.round(screenHeigh * 0.7f));
//            }
            popupWindow.setFocusable(true); // 使其聚集
            popupWindow.setOutsideTouchable(true);  // 设置允许在外点击消失
            // popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);// 防止虚拟软键盘被弹出菜单遮住
            popupWindow.setBackgroundDrawable(new BitmapDrawable());// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
//            popupWindow.showAsDropDown(parent, 0, 0); //默认
//            popupWindow.setAnimationStyle(R.style.PopupAnimation_belowtoup); // 设置动画
        }
        popupWindow.showAtLocation(parent, Gravity.CENTER , 0, 0);
//        if (isBottom) {
//            popupWindow.showAtLocation(parent, Gravity.CENTER | Gravity.BOTTOM, 0, 40);
//        } else {
//            if(ScreenUtil.checkDeviceHasNavigationBar(context)){
//                popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, parent.getHeight()+ScreenUtil.getDaoHangHeight(context));
//            }else {
//                popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, parent.getHeight());
//            }
//        }

        againAlpha = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (alpha > 0.5f) {
                    try {
                        //4是根据弹出动画时间和减少的透明度计算
                        Thread.sleep(4);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message msg = mHandler.obtainMessage();
                    msg.what = 1;
                    //每次减少0.01，精度越高，变暗的效果越流畅
                    alpha -= 0.01f;
                    msg.obj = alpha;
                    mHandler.sendMessage(msg);
                }
                againAlpha = true;
            }

        }).start();
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //此处while的条件alpha不能<= 否则会出现黑屏
                        while (alpha < 1f && againAlpha) {
                            try {
                                Thread.sleep(4);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Message msg = mHandler.obtainMessage();
                            msg.what = 1;
                            alpha += 0.01f;
                            msg.obj = alpha;
                            mHandler.sendMessage(msg);
                        }
                    }

                }).start();
            }
        });
    }

    /**
     * 下拉列表
     *
     * @param parent
     * @param context
     */
    public void showPopWindowList(View parent, Context context, View view, final ImageView arrow) {
        if (view == null) return;
//        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = layoutInflater.inflate(R.layout.popupwindow_list, null);
        view.getBackground().setAlpha(80); // 设置透明度0为完全透明，255为不透明
//        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
//        recyclerView.setLayoutManager(layoutManager);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        popupWindow = new PopupWindow(view, RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT);
//        popupWindow = new PopupWindow(view, RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true); // 使其聚集
        popupWindow.setOutsideTouchable(true);  // 设置允许在外点击消失
        // popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);// 防止虚拟软键盘被弹出菜单遮住
        popupWindow.setBackgroundDrawable(new BitmapDrawable());// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
//        popupWindow.setAnimationStyle(R.style.PopupAnimation_spinner); // 设置动画
//        popupWindow.showAtLocation(parent, Gravity.CENTER | Gravity.BOTTOM, 0, 0);
        popupWindow.showAsDropDown(parent, 0, 0); //默认

        arrow.animate().setDuration(300).rotation(-180).start();
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                arrow.animate().setDuration(300).rotation(0).start();
            }
        });
    }

    public void dismiss() {
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha 屏幕透明度0.0-1.0 1表示完全不透明
     */
    public void setBackgroundAlpha(Context context, float bgAlpha) {
        if (context == null) return;
        WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) context).getWindow().setAttributes(lp);
    }


    public interface PopupWindowListener {
        <T> void onInitView(View view, T t);

    }

    public void setOnPopupWindowListener(PopupWindowListener popupWindowListener) {
        this.popupWindowListener = popupWindowListener;
    }


}
