package com.geek.kaijo.Utils;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.cmmap.api.location.CmccLocation;
import com.cmmap.api.location.CmccLocationClient;
import com.cmmap.api.location.CmccLocationClientOption;
import com.cmmap.api.location.CmccLocationListener;
import com.geek.kaijo.R;
import com.geek.kaijo.app.Constant;
import com.geek.kaijo.app.MyApplication;
import com.geek.kaijo.app.service.LocalService;
import com.jess.arms.utils.DataHelper;
import com.jess.arms.utils.LogUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class GPSUtils {
    private static final double EARTH_RADIUS = 6378137.0;
    //声明CmccLocationClient类对象
    private CmccLocationClient mLocationClient = null;
    //声明CmccLocationClientOption对象
    private CmccLocationClientOption mLocationOption = null;
    private List<LocationListener> locationListenerList;
    private MyHandler myHandler;
    private CmccLocation mcmccLocation;

    public interface LocationListener {
        void onLocationChanged(CmccLocation cmccLocation);
    }

    private GPSUtils() {
        myHandler = new MyHandler(this);
        myHandler.sendEmptyMessageDelayed(1,60000);
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

//        mLocationOption.enableLog(true);

        //给定位客户端对象设置定位参数 GPS定位结果不会被缓存
        mLocationClient.setLocationOption(mLocationOption);

    }

    private CmccLocationListener cmccLocationListener = new CmccLocationListener() {
        @Override
        public void onLocationChanged(CmccLocation cmccLocation) {
            mcmccLocation = cmccLocation;
            if (cmccLocation != null) {
                Timber.v("11111111Code==" + cmccLocation.getErrorCode() + "纬度=" + cmccLocation.getLatitude());
//                    Log.i(this.getClass().getName(), "1111111日志==" + cmccLocation.getLocationDetail());
            } else {
                Timber.v("111111111111111111111cmccLocation==" + cmccLocation);
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

    private static class MyHandler extends Handler {
        private final WeakReference<GPSUtils> weakTrainModelActivity;

        public MyHandler(GPSUtils activity) {
            weakTrainModelActivity = new WeakReference<GPSUtils>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            GPSUtils weakActivity;
            if (weakTrainModelActivity.get() == null) {
                return;
            } else {
                weakActivity = weakTrainModelActivity.get();
            }
            switch (msg.what) {
                case 1:
//                initLocal();
                        if(weakActivity.mLocationClient!=null){
                            if (weakActivity.mcmccLocation.getErrorCode() != 0) {
                                weakActivity.mLocationClient.startLocation();
                            }
                        }
                    sendEmptyMessageDelayed(1,60000);

                    break;
            }
        }
    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     * @param context
     * @return true 表示开启
     */
    public static final boolean isOPen(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
//        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps) {
            return true;
        }

        return false;
    }

    /**
     * 提示需要权限 AlertDialog
     */
    public static void showGPSDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("GPS提醒");
        builder.setMessage("获取定位需要开启GPS");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent locationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                MyApplication.get().startActivity(locationIntent);
            }
        });
        builder.show();
    }

}