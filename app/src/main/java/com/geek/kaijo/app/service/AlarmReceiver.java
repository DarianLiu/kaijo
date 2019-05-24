package com.geek.kaijo.app.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("111111111111111接收到意图开启的广播,当前时间为："+new Date().toString());
        Intent i=new Intent(context,LocalService.class);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            context.startForegroundService(i);//开启AlarmService服务
        }else {
            context.startService(i);//开启AlarmService服务
        }

    }
}
