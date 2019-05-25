package com.geek.kaijo.Utils;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.cmmap.api.location.CmccLocation;
import com.cmmap.api.location.CmccLocationClient;
import com.cmmap.api.location.CmccLocationClientOption;
import com.cmmap.api.location.CmccLocationListener;
import com.geek.kaijo.R;
import com.geek.kaijo.app.MyApplication;
import com.jess.arms.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class GPSUtils {
    private static final double EARTH_RADIUS = 6378137.0;
    //声明CmccLocationClient类对象
    private CmccLocationClient mLocationClient = null;
    //声明CmccLocationClientOption对象
    private CmccLocationClientOption mLocationOption = null;
    private List<LocationListener> locationListenerList;

    public interface LocationListener {
        void onLocationChanged(CmccLocation cmccLocation);
    }

    private GPSUtils() {
    }

    public static GPSUtils getInstance() {
        return SingletonClassInstance.instance;
    }

    private static class SingletonClassInstance {
        private static final GPSUtils instance = new GPSUtils();
    }

    private void initLocation() {

        //初始化定位
        mLocationClient = new CmccLocationClient(MyApplication.get());
        //设置定位回调监听
        mLocationClient.setLocationListener(cmccLocationListener);

        //初始化CmccLocationClientOption对象
        mLocationOption = new CmccLocationClientOption();
        //设置定位模式为CmccLocationClientOption.CmccLocationMode.Hight_Accuracy，高精度模式。pgs+网络
//        mLocationOption.setLocationMode(CmccLocationClientOption.CmccLocationMode.Hight_Accuracy);
        //设置定位模式为CmccLocationClientOption.CmccLocationMode.Device_Sensors，仅设备模式GPS。
        mLocationOption.setLocationMode(CmccLocationClientOption.CmccLocationMode.Device_Sensors);

        //获取一次定位结果：
        //该方法默认为false。
//        mLocationOption.setOnceLocation(true);

        //SDK默认采用连续定位模式，时间间隔2000ms。如果您需要自定义调用间隔：
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(2000);

        //超时时间设置 单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);


        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);

        mLocationOption.enableLog(true);

        //给定位客户端对象设置定位参数 GPS定位结果不会被缓存
        mLocationClient.setLocationOption(mLocationOption);

    }

    private CmccLocationListener cmccLocationListener = new CmccLocationListener() {
        @Override
        public void onLocationChanged(CmccLocation cmccLocation) {
//            if (cmccLocation.getErrorCode() != 0) {
//                initLocal();
//            }
            if (cmccLocation != null) {
                Log.i(this.getClass().getName(), "11111111Code==" + cmccLocation.getErrorCode() + "纬度=" + cmccLocation.getLatitude());
//                    Log.i(this.getClass().getName(), "1111111日志==" + cmccLocation.getLocationDetail());
            } else {
                Log.i(this.getClass().getName(), "111111111111111111111cmccLocation==" + cmccLocation);
            }

            if (locationListenerList == null) return;
            for (int i = 0; i < locationListenerList.size(); i++) {
                if (locationListenerList.get(i) != null) {
                    locationListenerList.get(i).onLocationChanged(cmccLocation);
                }
            }
        }
    };

    //打补丁唤醒gps
    private LocationManager locationManager;
    private void initLocal() {
        locationManager = (LocationManager) MyApplication.get().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(MyApplication.get(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MyApplication.get(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, null);
    }

//    public void startLocation(LocationListener locationListener) {
//        if(locationListenerList==null){
//            locationListenerList = new ArrayList<>();
//        }
//        boolean flag = true;
//        for(int i=0;i<locationListenerList.size();i++){
//            if(locationListenerList.get(i)==locationListener){
//                flag = false;
//                break;
//            }
//        }
//        if(flag){
//            locationListenerList.add(locationListener);
//        }
//        startLocation();
//
//    }

    public void setOnLocationListener(LocationListener locationListener) {
        if(locationListenerList==null){
            locationListenerList = new ArrayList<>();
        }
        boolean flag = true;
        for(int i=0;i<locationListenerList.size();i++){
            if(locationListenerList.get(i)==locationListener){
                flag = false;
                break;
            }
        }
        if(flag){
            locationListenerList.add(locationListener);
        }

    }

    public void startLocation(){

        Log.i(this.getClass().getName(),"1111111111111111111111初始化吗。。。。===mLocationClient=="+mLocationClient);
        if (mLocationClient == null) {
            initLocation();
        }
            mLocationClient.startLocation();
            mLocationClient.enableBackgroundLocation(543543, buildNotification());
    }

    public void onDestroy(){
        if(mLocationClient!=null){
            mLocationClient.stopLocation();
            mLocationClient.destory();
            mLocationClient = null;
        }
    }

    public void removeLocationListener(LocationListener locationListener){
        if(locationListenerList!=null && locationListener!=null){
            locationListenerList.remove(locationListener);
        }

    }

    // 返回单位是米
    public static double getDistance(double longitude1, double latitude1,
                                     double longitude2, double latitude2) {
        double Lat1 = rad(latitude1);
        double Lat2 = rad(latitude2);
        double a = Lat1 - Lat2;
        double b = rad(longitude1) - rad(longitude2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(Lat1) * Math.cos(Lat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }



    private static final String NOTIFICATION_CHANNEL_NAME = "BackgroundLocation";
    private Notification buildNotification() {

        Notification.Builder builder = null;
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            //Android O上对Notification进行了修改，如果设置的targetSDKVersion>=26建议使用此种方式创建通知栏

//            if (null == notificationManager) {
            NotificationManager notificationManager = (NotificationManager) MyApplication.get().getSystemService(Context.NOTIFICATION_SERVICE);
//            }
            String channelId = MyApplication.get().getPackageName();
//            if (!isCreateChannel) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId,
                    NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableLights(true);//是否在桌面icon右上角展示小圆点
            notificationChannel.setLightColor(Color.BLUE); //小圆点颜色
            notificationChannel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
            notificationManager.createNotificationChannel(notificationChannel);
//                isCreateChannel = true;
//            }
            builder = new Notification.Builder(MyApplication.get(), channelId);
        } else {
            builder = new Notification.Builder(MyApplication.get());
        }
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("定位SDK")
                .setContentText("正在后台运行")
                .setWhen(System.currentTimeMillis());

        if (android.os.Build.VERSION.SDK_INT >= 16) {
            notification = builder.build();
        } else {
            return builder.getNotification();
        }
        return notification;
    }
//
//
//
//    android.location.LocationListener locationListener = new android.location.LocationListener() {
//        // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//
//            switch (status){
//
//                case LocationProvider.AVAILABLE:
//
//                    Toast.makeText(MyApplication.get(),"当前GPS为可用状态!",Toast.LENGTH_SHORT).show();
//
//                    break;
//
//                case LocationProvider.OUT_OF_SERVICE:
//
//                    Toast.makeText(MyApplication.get(),"当前GPS不在服务内",Toast.LENGTH_SHORT).show();
//
//                    break;
//
//                case LocationProvider.TEMPORARILY_UNAVAILABLE:
//
//                    Toast.makeText(MyApplication.get(),"当前GPS为暂停服务状态",Toast.LENGTH_SHORT).show();
//                    break;
//
//
//            }
//        }
//
//        // Provider被enable时触发此函数，比如GPS被打开
//        @Override
//        public void onProviderEnabled(String provider) {
//
//        }
//
//        // Provider被disable时触发此函数，比如GPS被关闭
//        @Override
//        public void onProviderDisabled(String provider) {
//
//        }
//
//        // 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
//        @Override
//        public void onLocationChanged(Location location) {
//            if (location != null) {
//
//            }
//        }
//    };


}