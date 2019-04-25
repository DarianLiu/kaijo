package com.geek.kaijo.app.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.cmcc.api.fpp.bean.CmccLocation;
import com.cmcc.api.fpp.bean.LocationParam;
import com.cmcc.api.fpp.login.SecurityLogin;
import com.geek.kaijo.Utils.PermissionUtils;
import com.geek.kaijo.app.Constant;
import com.geek.kaijo.app.api.Api;
import com.geek.kaijo.mvp.ui.activity.MainActivity;
import com.geek.kaijo.mvp.ui.activity.ReportActivity;
import com.jess.arms.utils.DataHelper;
import com.jess.arms.utils.LogUtils;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.ParserConfigurationException;

import io.reactivex.functions.Consumer;
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
    private LocationParam locParam = null;//移动定位
    private SecurityLogin mClient;
    private RxPermissions rxPermissions;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

//        getGpsLocation();
        initLocation();
        mClient.start();
        myHandler = new MyHandler(this);
        myHandler.sendEmptyMessageDelayed(1, 3000);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        userId = DataHelper.getStringSF(this, Constant.SP_KEY_USER_ID);
        return super.onStartCommand(intent, flags, startId);

    }

    private void initLocation() {
        locParam = new LocationParam();
        locParam.setServiceId(Constant.MobileAppId);//此ID仅对应本网站下载的SDK，作为测试账号使用。
        locParam.setLocType("1");
//        locParam.setForceUseWifi(true);
        locParam.setOffSet(false);// It should be set in onCreate() func
        mClient = new SecurityLogin(this);
        mClient.setLocationParam(locParam);
    }

//    @SuppressLint("CheckResult")
//    private void checkPermissionAndAction() {
//        if (rxPermissions == null) {
//            rxPermissions = new RxPermissions(this);
//        }
//        //同时申请多个权限
//        rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION,
//                Manifest.permission.READ_PHONE_STATE,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(granted -> {
//            if (granted) {           // All requested permissions are granted
//                startLocation();
//            } else {
//                showPermissionsDialog();
//            }
//        });
//    }

    private void startLocation() {
        new Thread(() -> {
            Message msg = Message.obtain();
            msg.what = 0x1233;
            try {
                CmccLocation loc = mClient.locCapability();
                latitude = loc.getLatitude();
                longitude = loc.getLongitude();
                Log.i(this.getClass().getName(),"11111111111111111111111latitude======"+loc.getLatitude());
                Log.i(this.getClass().getName(),"11111111111111111111111longitude======"+longitude);
                if (userId != null && latitude > 0 && longitude > 0) {
                    httpUploadGpsLocation(userId, latitude, longitude);
                }
//                myHandler.sendMessage(msg);
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * 上传位置信息
     * @param userId
     * @param lat
     * @param lng
     */
    private void httpUploadGpsLocation(String userId,double lat,double lng){

        OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("userId",userId)
                .add("lat",lat+"")
                .add("lng",lng+"")
                .build();
        Request request = new Request.Builder().url(Api.BASE_URL+"/user/addUserCoordinate.json").post(formBody).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(this.getClass().getName(),"11111111111111111111111上传位置信息的返回"+response.body().string());
            }
        });
    }

    /*private void initGpsLocation() {
        if (rxPermissions == null) {
            rxPermissions = new RxPermissions(this);
        }
        rxPermissions.requestEach(Manifest.permission.ACCESS_FINE_LOCATION) //gps定位
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            getGpsLocation();
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
//                            Log.d(TAG, permission.name + " is denied. More info should be provided.");
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
//                            Log.d(TAG, permission.name + " is denied.");
//                            showDialog();

                        }
                    }
                });

    }
*/
    private void getGpsLocation(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }else {
                Location locationNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (locationNet != null) {
                    latitude = locationNet.getLatitude(); //经度
                    longitude = locationNet.getLongitude(); //纬度
                }
            }
        } else {
            LocationListener locationListener = new LocationListener() {

                // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                // Provider被enable时触发此函数，比如GPS被打开
                @Override
                public void onProviderEnabled(String provider) {

                }

                // Provider被disable时触发此函数，比如GPS被关闭
                @Override
                public void onProviderDisabled(String provider) {

                }

                //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
                @Override
                public void onLocationChanged(Location location) {
                    if (location != null) {
                        Log.e("Map", "Location changed : Lat: "
                                + location.getLatitude() + " Lng: "
                                + location.getLongitude());
                    }
                }
            };
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude(); //经度
                longitude = location.getLongitude(); //纬度
            }

        }
    }

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
                    weakActivity.startLocation();
//                    if (weakActivity.userId != null && weakActivity.latitude > 0 && weakActivity.longitude > 0) {
//                        weakActivity.httpUploadGpsLocation(weakActivity.userId, weakActivity.latitude, weakActivity.longitude);
////                        sendEmptyMessageDelayed(1, 60000); //1分钟 上传一次经纬度
//                    }
                    sendEmptyMessageDelayed(1, 60000); //1分钟 上传一次经纬度
                    break;

            }
        }
    }

    /**
     * 提示需要权限 AlertDialog
     */
    private void showPermissionsDialog() {
        /*
         * 这里使用了 android.support.v7.app.AlertDialog.Builder
         * 可以直接在头部写 import android.support.v7.app.AlertDialog
         * 那么下面就可以写成 AlertDialog.Builder
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("权限提醒");
        builder.setMessage("获取坐标需要位置权限");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PermissionUtils.permissionSkipSetting(getApplicationContext());
            }
        });
        builder.show();
    }
}
