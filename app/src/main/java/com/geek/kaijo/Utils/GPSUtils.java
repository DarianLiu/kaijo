package com.geek.kaijo.Utils;

import android.util.Log;

import com.cmmap.api.location.CmccLocation;
import com.cmmap.api.location.CmccLocationClient;
import com.cmmap.api.location.CmccLocationClientOption;
import com.cmmap.api.location.CmccLocationListener;
import com.geek.kaijo.app.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class GPSUtils {

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
                for(int i=0;i<locationListenerList.size();i++){
                    if(locationListenerList.get(i)!=null){
                        locationListenerList.get(i).onLocationChanged(cmccLocation);
                    }
                }
                Log.i(this.getClass().getName(), "111111111111111111111" + cmccLocation.getLongitude());
                Log.i(this.getClass().getName(), "111111111111111111111" + cmccLocation.getLatitude());
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
        mLocationOption.setOnceLocation(true);

        //SDK默认采用连续定位模式，时间间隔2000ms。如果您需要自定义调用间隔：
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
//        mLocationOption.setInterval(1000);

        //超时时间设置 单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);


        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);

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
        if (mLocationClient == null) {
            initLocation();
        }
        mLocationClient.startLocation();
    }

    public void removeLocationListener(LocationListener locationListener){
        if(locationListenerList!=null){
            locationListenerList.remove(locationListener);
        }

    }


}