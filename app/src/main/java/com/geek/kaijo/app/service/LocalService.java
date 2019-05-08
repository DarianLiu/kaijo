package com.geek.kaijo.app.service;

import android.app.Service;
import android.content.Intent;
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
import com.geek.kaijo.app.api.Api;
import com.jess.arms.utils.DataHelper;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.IOException;
import java.lang.ref.WeakReference;

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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        userId = DataHelper.getStringSF(this, Constant.SP_KEY_USER_ID);
        myHandler = new MyHandler(this);
        myHandler.sendEmptyMessageDelayed(1, 3000);
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
                    GPSUtils.getInstance().startLocation(weakActivity.locationListener);
//                    sendEmptyMessageDelayed(1, 60000); //1分钟 上传一次经纬度
                    break;
            }
        }
    }


    private GPSUtils.LocationListener locationListener = new GPSUtils.LocationListener() {
        @Override
        public void onLocationChanged(CmccLocation cmccLocation) {
            httpUploadGpsLocation(userId, cmccLocation.getLatitude(), cmccLocation.getLongitude());
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        GPSUtils.getInstance().removeLocationListener(locationListener);
    }


}
