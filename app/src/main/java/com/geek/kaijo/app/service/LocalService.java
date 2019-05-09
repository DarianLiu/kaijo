package com.geek.kaijo.app.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.cmmap.api.location.CmccLocation;
import com.cmmap.api.location.CmccLocationClient;
import com.cmmap.api.location.CmccLocationClientOption;
import com.geek.kaijo.Utils.GPSUtils;
import com.geek.kaijo.app.Constant;
import com.geek.kaijo.app.MyApplication;
import com.geek.kaijo.app.api.Api;
import com.geek.kaijo.mvp.model.entity.IPRegisterBean;
import com.geek.kaijo.mvp.ui.activity.InspectionProjectRegisterActivity;
import com.jess.arms.utils.DataHelper;
import com.jess.arms.utils.LogUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import dao.DaoSession;
import dao.IPRegisterBeanDao;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LocalService extends Service {

    private MyHandler myHandler;
    private CmccLocation cmccLocation;
    private List<IPRegisterBean> result;
    LocationReceiver locationReceiver;
    int state; //0 未巡查 1 开始巡查 2 结束巡查

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        myHandler = new MyHandler(this);
        myHandler.sendEmptyMessageDelayed(1, 60000);

        locationReceiver = new LocationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.SP_KEY_Patrol_state);
        registerReceiver(locationReceiver, filter);

        GPSUtils.getInstance().startLocation(locationListener);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        userId = DataHelper.getStringSF(this, Constant.SP_KEY_USER_ID);
        return super.onStartCommand(intent, flags, startId);

    }

    /**
     * 上传位置信息
     *
     * @param userId
     * @param lat
     * @param lng
     */
    private void httpUploadGpsLocation(String userId, double lat, double lng) {
        if (lat == 0 || lng == 0) {
            return;
        }
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
                    if(weakActivity.cmccLocation!=null){
                        Double mLat = weakActivity.cmccLocation.getLatitude();
                        Double mLng = weakActivity.cmccLocation.getLongitude();
                        Toast.makeText(weakActivity,"经度："+mLng+"纬度："+mLat,Toast.LENGTH_LONG).show();
                        if(mLat>0 && mLng>0){
                           String userId = DataHelper.getStringSF(weakActivity, Constant.SP_KEY_USER_ID);
                            weakActivity.httpUploadGpsLocation(userId, weakActivity.cmccLocation.getLatitude(), weakActivity.cmccLocation.getLongitude());
                            weakActivity.cmccLocation = null;
                        }
                    }else {
                        Toast.makeText(weakActivity,"定位获取失败",Toast.LENGTH_LONG).show();
                    }
                    sendEmptyMessageDelayed(1, 60000); //1分钟 上传一次经纬度

                    break;
            }
        }
    }


    private GPSUtils.LocationListener locationListener = new GPSUtils.LocationListener() {
        @Override
        public void onLocationChanged(CmccLocation cmccLocation) {

            if(cmccLocation==null)return;
            LocalService.this.cmccLocation = cmccLocation;
            if(cmccLocation!=null && result!=null && state==1){
                for(int i=0;i<result.size();i++){
                    if(result.get(i).getStatus()==0){
                        double distance = GPSUtils.getInstance().getDistance(cmccLocation.getLongitude(),cmccLocation.getLatitude(),result.get(i).getLng(),result.get(i).getLat());
                        if(distance<=50){ //误差50
                            //点位巡查成功，状态保存到数据库
                            result.get(i).setStatus(1);
                            Toast.makeText(getApplication(),result.get(i).getName()+"已巡查",Toast.LENGTH_LONG).show();
                            new Thread(){
                                @Override
                                public void run() {
                                    super.run();
                                    DaoSession daoSession1 = MyApplication.get().getDaoSession();
                                    IPRegisterBeanDao ipRegisterBeanDao = daoSession1.getIPRegisterBeanDao();
                                    ipRegisterBeanDao.insertOrReplaceInTx(result);

                                    Intent intent = new Intent();
                                    intent.setAction(Constant.service_patrol);
                                    sendBroadcast(intent);
                                }
                            }.start();
                        }
                    }

                }

            }
        }
    };

    /**
     * 巡查状态广播
     */
    public class LocationReceiver extends BroadcastReceiver {
        //必须要重载的方法，用来监听是否有广播发送
        @Override
        public void onReceive(Context context, Intent intent) {
            String intentAction = intent.getAction();
            if (intentAction.equals(Constant.SP_KEY_Patrol_state)) {
                state = intent.getIntExtra(Constant.SP_KEY_Patrol_state,0);
//                if(state==1){ //开始巡查
//                    DaoSession daoSession1 = MyApplication.get().getDaoSession();
//                    IPRegisterBeanDao ipRegisterBeanDao = daoSession1.getIPRegisterBeanDao();
//                    result = ipRegisterBeanDao.loadAll();
//                }
                if(intent.hasExtra(Constant.SP_KEY_Patrol_state_db)){
                    DaoSession daoSession1 = MyApplication.get().getDaoSession();
                    IPRegisterBeanDao ipRegisterBeanDao = daoSession1.getIPRegisterBeanDao();
                    result = ipRegisterBeanDao.loadAll();
                }
                LogUtils.debugInfo("1111111111111111111111111111111111111111state===="+state);
            }
//            else if(intentAction.equals(Constant.SP_KEY_Patrol_state_db)){ //数据更新
//                DaoSession daoSession1 = MyApplication.get().getDaoSession();
//                IPRegisterBeanDao ipRegisterBeanDao = daoSession1.getIPRegisterBeanDao();
//                result = ipRegisterBeanDao.loadAll();
//            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        GPSUtils.getInstance().removeLocationListener(locationListener);
        GPSUtils.getInstance().onDestroy();
        unregisterReceiver(locationReceiver);
    }


}
