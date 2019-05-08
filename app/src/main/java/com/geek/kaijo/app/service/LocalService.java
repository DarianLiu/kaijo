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

    private double latitude = 0.0; //经度
    private double longitude = 0.0;
    private MyHandler myHandler;
    private String userId;
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
        userId = DataHelper.getStringSF(this, Constant.SP_KEY_USER_ID);
        GPSUtils.getInstance().startLocation(locationListener);
        myHandler = new MyHandler(this);
        myHandler.sendEmptyMessageDelayed(1, 3000);

        locationReceiver = new LocationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.SP_KEY_Patrol_state);
        registerReceiver(locationReceiver, filter);
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
//                    GPSUtils.getInstance().startLocation(weakActivity.locationListener);

                    if(weakActivity.cmccLocation!=null){
                        Double mLat = weakActivity.cmccLocation.getLatitude();
                        Double mLng = weakActivity.cmccLocation.getLongitude();
                        if(mLat>0 && mLng>0){
                            weakActivity.httpUploadGpsLocation(weakActivity.userId, weakActivity.cmccLocation.getLatitude(), weakActivity.cmccLocation.getLongitude());
                            weakActivity.cmccLocation = null;
                        }
                    }
                    sendEmptyMessageDelayed(1, 60000); //1分钟 上传一次经纬度

                    break;
            }
        }
    }


    private GPSUtils.LocationListener locationListener = new GPSUtils.LocationListener() {
        @Override
        public void onLocationChanged(CmccLocation cmccLocation) {
            LocalService.this.cmccLocation = cmccLocation;
            if(cmccLocation!=null && result!=null && state==1){
                for(int i=0;i<result.size();i++){
                    if(result.get(i).getStatus()==0){
                        double distance = GPSUtils.getInstance().getDistance(cmccLocation.getLongitude(),cmccLocation.getLatitude(),result.get(i).getLng(),result.get(i).getLat());
                        if(distance<=20){ //误差20
                            //点位巡查成功，状态保存到数据库
                            result.get(i).setStatus(1);
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
//                if(intent.hasExtra("resutl")){
//                    result = (List<IPRegisterBean>)intent.getSerializableExtra("resutl");
//                }
                LogUtils.debugInfo("1111111111111111111111111111111111111111state===="+state);
            }else if(intentAction.equals(Constant.SP_KEY_Patrol_state_db)){ //数据更新
                DaoSession daoSession1 = MyApplication.get().getDaoSession();
                IPRegisterBeanDao ipRegisterBeanDao = daoSession1.getIPRegisterBeanDao();
                result = ipRegisterBeanDao.loadAll();
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        GPSUtils.getInstance().removeLocationListener(locationListener);
        unregisterReceiver(locationReceiver);
    }


}
