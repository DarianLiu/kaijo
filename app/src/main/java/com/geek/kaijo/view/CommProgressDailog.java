package com.geek.kaijo.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;

/**
 * Created by $Liudan on 2018/8/3 0003.
 */
public class CommProgressDailog {
    private ProgressDialog progressDialog;

    public void showProgressDailog(Context context,String message){
        if(progressDialog==null){
            progressDialog = new ProgressDialog(context);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
            progressDialog.setCancelable(true);// 设置是否可以通过点击Back键取消
            progressDialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
//            progressDialog.setIcon(R.drawable.ic_launcher);//
            // 设置提示的title的图标，默认是没有的，如果没有设置title的话只设置Icon是不会显示图标的
//            progressDialog.setTitle("正在连接蓝牙设备");
            if(!TextUtils.isEmpty(message)){
                progressDialog.setMessage(message);
            }

        }
//        if(progressDialog.isShowing())progressDialog.cancel();
        progressDialog.show();
    }

    public void cancelProgressDailog(){
        if(progressDialog!=null){
            progressDialog.cancel();
        }
    }
}
