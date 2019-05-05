package com.geek.kaijo.app.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.cmcc.api.fpp.bean.CmccLocation;
import com.cmcc.api.fpp.bean.LocationParam;
import com.cmcc.api.fpp.login.SecurityLogin;
import com.geek.kaijo.app.Constant;
import com.geek.kaijo.app.MyApplication;
import com.geek.kaijo.app.api.Api;
import com.jess.arms.utils.DataHelper;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.lang.ref.WeakReference;

import javax.xml.parsers.ParserConfigurationException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LocalService extends Service {

    private double latitude = 0.0; //经度
    private double longitude = 0.0;
    private MyHandler myHandler;
    private String userId;
    private RxPermissions rxPermissions;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        getGpsLocation();
        myHandler = new MyHandler(this);
        myHandler.sendEmptyMessageDelayed(1, 3000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        userId = DataHelper.getStringSF(this, Constant.SP_KEY_USER_ID);
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    /**
     * 上传位置信息
     *
     * @param userId
     * @param lat
     * @param lng
     */
    private void httpUploadGpsLocation(String userId, double lat, double lng) {

        OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("userId", userId)
                .add("lat", lat + "")
                .add("lng", lng + "")
                .build();
        Request request = new Request.Builder().url(Api.BASE_URL + "/user/addUserCoordinate.json").post(formBody).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(this.getClass().getName(), "11111111111111111111111上传位置信息的返回" + response.body().string());
            }
        });
    }

    private void getGpsLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // 绑定监听，有4个参数
        // 参数1，设备：有GPS_PROVIDER和NETWORK_PROVIDER两种
        // 参数2，位置信息更新周期，单位毫秒
        // 参数3，位置变化最小距离：当位置距离变化超过此值时，将更新位置信息
        // 参数4，监听
        // 备注：参数2和3，如果参数3不为0，则以参数3为准；参数3为0，则通过时间来定时更新；两者为0，则随时刷新

        // 1秒更新一次，或最小位移变化超过1米更新一次；
        // 注意：此处更新准确度非常低，推荐在service里面启动一个Thread，在run中sleep(10000);然后执行handler.sendMessage(),更新位置
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);

    }

    // 位置监听
    private static LocationListener locationListener = new LocationListener() {

        //位置信息变化时触发
        public void onLocationChanged(Location location) {
            Toast.makeText(MyApplication.get(),"位置发生变化:"+location.getLongitude(),Toast.LENGTH_LONG).show();
//            lat = location.getLatitude();
//            mLocation = location;
//            Log.i(TAG, "时间：" + location.getTime());
//            Log.i(TAG, "经度：" + location.getLongitude());
//            Log.i(TAG, "纬度：" + location.getLatitude());
//            Log.i(TAG, "海拔：" + location.getAltitude());
        }

        //GPS状态变化时触发
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                // GPS状态为可见时
                case LocationProvider.AVAILABLE:
//                    Log.i(TAG, "当前GPS状态为可见状态");
                    break;
                // GPS状态为服务区外时
                case LocationProvider.OUT_OF_SERVICE:
//                    Log.i(TAG, "当前GPS状态为服务区外状态");
                    break;
                // GPS状态为暂停服务时
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
//                    Log.i(TAG, "当前GPS状态为暂停服务状态");
                    break;
            }
        }

        //GPS开启时触发
        public void onProviderEnabled(String provider) {
//            Location location = mLocationManager.getLastKnownLocation(provider);
//            mLocation = location;
        }

        //GPS禁用时触发
        public void onProviderDisabled(String provider) {
//            mLocation = null;
        }
    };


    private static class MyHandler extends Handler {
        private final WeakReference<LocalService> weakTrainModelActivity;

        public MyHandler(LocalService activity) {
            weakTrainModelActivity = new WeakReference<LocalService>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LocalService weakActivity;
            if (weakTrainModelActivity.get() == null) {
                return;
            } else {
                weakActivity = weakTrainModelActivity.get();
            }
            switch (msg.what) {
                case 1:
//                    weakActivity.startLocation();
//                    if (weakActivity.userId != null && weakActivity.latitude > 0 && weakActivity.longitude > 0) {
//                        weakActivity.httpUploadGpsLocation(weakActivity.userId, weakActivity.latitude, weakActivity.longitude);
////                        sendEmptyMessageDelayed(1, 60000); //1分钟 上传一次经纬度
//                    }
                    sendEmptyMessageDelayed(1, 60000); //1分钟 上传一次经纬度
                    break;
                case 0x1233:
                    Toast.makeText(weakActivity.getApplicationContext(),"上传位置信息：经度="+weakActivity.longitude+"纬度="+weakActivity.latitude,Toast.LENGTH_LONG).show();

                    break;
            }
        }
    }

}
