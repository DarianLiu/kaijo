package com.geek.kaijo.mvp.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.cmcc.api.fpp.bean.CmccLocation;
import com.cmcc.api.fpp.bean.LocationParam;
import com.cmcc.api.fpp.login.SecurityLogin;
import com.cmmap.api.maps.CameraUpdateFactory;
import com.cmmap.api.maps.Map;
import com.cmmap.api.maps.MapView;
import com.cmmap.api.maps.model.Arc;
import com.cmmap.api.maps.model.LatLng;
import com.cmmap.api.maps.model.Marker;
import com.cmmap.api.maps.model.MarkerOptions;
import com.geek.kaijo.R;
import com.geek.kaijo.Utils.PermissionUtils;
import com.geek.kaijo.app.Constant;
import com.geek.kaijo.mvp.model.event.LocationEvent;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.simple.eventbus.EventBus;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

import timber.log.Timber;

/**
 * 地图
 * Created by LiuLi on 2018/9/8.
 */

public class MapActivity extends AppCompatActivity {

    private MapView mMapView;
    private Button btnSure;

    //地图
    private Map mMap;
    private Marker marker;

    private double lng, lat;

    private LocationParam locParam = null;//移动定位
    private SecurityLogin mClient;
    private MessageHandler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mMapView = findViewById(R.id.mMap);
        mMapView.onCreate(savedInstanceState);
        btnSure = findViewById(R.id.btn_sure);

        handler = new MessageHandler();

        btnSure.setOnClickListener(v -> {
            LocationEvent event = new LocationEvent();
            event.setLat(lat);
            event.setLng(lng);
            EventBus.getDefault().post(event, "location");
            finish();
        });
        lng = getIntent().getDoubleExtra("lng", 0);
        lat = getIntent().getDoubleExtra("lat", 0);
        initMap();
        initLocation();


        if (lng == 0 || lat == 0) {
            checkPermissionAndAction();
        } else {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(lat, lng));
            markerOptions.draggable(true);
            marker = mMap.addMarker(markerOptions);
        }


        mMap.setOnArcDragListener(new Map.OnArcDragListener() {
            @Override
            public void onArcDragStart(Arc arc, LatLng latLng) {
                Timber.d("=====onArcDragStart " + "lat: " + latLng.latitude + "lng: " + latLng.longitude);
            }

            @Override
            public void onArcDrag(Arc arc, LatLng latLng) {
                Timber.d("=====onArcDrag " + "lat: " + latLng.latitude + "lng: " + latLng.longitude);
            }

            @Override
            public void onArcDragEnd(Arc arc, LatLng latLng) {
                Timber.d("=====onArcDrag " + "lat: " + latLng.latitude + "lng: " + latLng.longitude);
            }
        });

        //地图点击事件
        mMap.setOnMapClickListener(new Map.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Timber.d("=====onMapClick " + "lat: " + latLng.latitude + "lng: " + latLng.longitude);
                marker.setPosition(new LatLng(latLng.latitude, latLng.longitude));
            }
        });

        mMap.setOnMarkerDragListener(new Map.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                lng = marker.getPosition().longitude;
                lat = marker.getPosition().latitude;
            }
        });
    }

    /**
     * 初始化地图
     */
    private void initMap() {
        mMap = mMapView.getMap();
        mMap.setMapType(Map.MAP_TYPE_NORMAL);
        mMap.showMapText(true);//是否显示文字
        mMap.showBuildings(true);//是否显示建筑物
//        41.072847
        mMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(lat, lng)));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    @SuppressLint("HandlerLeak")
    private class MessageHandler extends Handler {
        public MessageHandler() {
            super();
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x1233) {
                mClient.pause();

                if (lat == 0 || lng == 0) {
                } else {
                    if (marker == null) {
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(new LatLng(lat, lng));
                        markerOptions.draggable(true);
                        marker = mMap.addMarker(markerOptions);
                    }
                    marker.setPosition(new LatLng(lat, lng));
                }
            }

        }
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

    @SuppressLint("CheckResult")
    private void checkPermissionAndAction() {

        //同时申请多个权限
        new RxPermissions(this).request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(granted -> {
            if (granted) {           // All requested permissions are granted
                startLocation();
            } else {
                showPermissionsDialog();
            }
        });
    }

    private void startLocation() {
        new Thread(() -> {
            Message msg = Message.obtain();
            msg.what = 0x1233;
            try {
                CmccLocation loc = mClient.locCapability();
                lat = loc.getLatitude();
                lng = loc.getLongitude();
                if (handler != null)
                    handler.sendMessage(msg);
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
        }).start();
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
                PermissionUtils.permissionSkipSetting(MapActivity.this);
            }
        });
        builder.show();
    }

    @Override
    protected void onStart() {
        mClient.start();
        super.onStart();
    }

    @Override
    protected void onResume() {
        mClient.restart();
        super.onResume();
        mMapView.onResume();

    }

    @Override
    protected void onPause() {
        mClient.pause();
        super.onPause();
        mMapView.onPause();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mMap.clear();
        mMap = null;
        handler.removeCallbacksAndMessages(null);
        handler = null;
        locParam = null;
    }

}
