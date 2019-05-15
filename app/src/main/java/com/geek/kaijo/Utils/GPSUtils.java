package com.geek.kaijo.Utils;

import android.util.Log;

import com.cmmap.api.location.CmccLocation;
import com.cmmap.api.location.CmccLocationClient;
import com.cmmap.api.location.CmccLocationClientOption;
import com.cmmap.api.location.CmccLocationListener;
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
    private List<LocationListener>  locationListenerList;

    public interface LocationListener{
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
        mLocationClient.setLocationListener(new CmccLocationListener() {
            @Override
            public void onLocationChanged(CmccLocation cmccLocation) {
                if(cmccLocation!=null){
                    Log.i(this.getClass().getName(), "111111111111111111111getErrorCode==" + cmccLocation.getErrorCode());
                    Log.i(this.getClass().getName(), "111111111111111111111getLocationDetail==" + cmccLocation.getLocationDetail());
                    Log.i(this.getClass().getName(), "111111111111111111111getLatitude==" + cmccLocation.getLatitude());
                }else {
                    Log.i(this.getClass().getName(), "111111111111111111111cmccLocation==" + cmccLocation);
                }

                if(locationListenerList==null)return;
                for(int i=0;i<locationListenerList.size();i++){
                    if(locationListenerList.get(i)!=null){
                        locationListenerList.get(i).onLocationChanged(cmccLocation);
                    }
                }
            }
        });

        //初始化CmccLocationClientOption对象
        mLocationOption = new CmccLocationClientOption();
        //设置定位模式为CmccLocationClientOption.CmccLocationMode.Hight_Accuracy，高精度模式。pgs+网络
//        mLocationOption.setLocationMode(CmccLocationClientOption.CmccLocationMode.Hight_Accuracy);
        //设置定位模式为CmccLocationClientOption.CmccLocationMode.Device_Sensors，仅设备模式GPS。
        mLocationOption.setLocationMode(CmccLocationClientOption.CmccLocationMode. Device_Sensors);

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

    public void startLocation(LocationListener locationListener) {
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
        startLocation();

    }

    public void startLocation(){
//        mLocationClient.
        if (mLocationClient == null || !mLocationClient.isStarted()) {
            initLocation();
        }
        if(!mLocationClient.isStarted()){
            mLocationClient.startLocation();
        }
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


}