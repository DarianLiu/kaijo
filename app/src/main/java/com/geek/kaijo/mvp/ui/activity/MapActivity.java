package com.geek.kaijo.mvp.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cmmap.api.location.CmccLocation;
import com.cmmap.api.location.CmccLocationClient;
import com.cmmap.api.location.CmccLocationClientOption;
import com.cmmap.api.location.CmccLocationListener;
import com.cmmap.api.maps.CameraUpdateFactory;
import com.cmmap.api.maps.Map;
import com.cmmap.api.maps.MapView;
import com.cmmap.api.maps.model.Arc;
import com.cmmap.api.maps.model.CircleOptions;
import com.cmmap.api.maps.model.LatLng;
import com.cmmap.api.maps.model.Marker;
import com.cmmap.api.maps.model.MarkerOptions;
import com.cmmap.api.maps.model.Polyline;
import com.cmmap.api.maps.model.PolylineOptions;
import com.geek.kaijo.R;
import com.geek.kaijo.Utils.PermissionUtils;
import com.geek.kaijo.app.Constant;
import com.geek.kaijo.app.api.Api;
import com.geek.kaijo.mvp.model.entity.GridBorder;
import com.geek.kaijo.mvp.model.entity.UserInfo;
import com.geek.kaijo.mvp.model.event.LocationEvent;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.jess.arms.utils.DataHelper;
import com.jess.arms.utils.LogUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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

    private UserInfo userInfo;
    private double lngDefault = 116.486073, latDefaut = 40.000565;  //定位失败 默认显示点
    private String MAP_LOOK;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mMapView = findViewById(R.id.mMap);
        mMapView.onCreate(savedInstanceState);
        btnSure = findViewById(R.id.btn_sure);
        userInfo = DataHelper.getDeviceData(this, Constant.SP_KEY_USER_INFO);
        lng = getIntent().getDoubleExtra("lng", 0);
        lat = getIntent().getDoubleExtra("lat", 0);
        MAP_LOOK = getIntent().getStringExtra(Constant.MAP_LOOK);

        if(!TextUtils.isEmpty(MAP_LOOK)){
            btnSure.setVisibility(View.GONE);
        }
        initMap();

        btnSure.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("lng",lng);
            intent.putExtra("lat",lat);
            setResult(Constant.MAP_REQUEST_CODE,intent);
            finish();
        });

        if (lng == 0 || lat == 0) {  //定位失败  显示默认位置
            lng = lngDefault;
            lat = latDefaut;
//            checkPermissionAndAction();
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(latDefaut, lngDefault));
            markerOptions.draggable(true);
            mMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(latDefaut, lngDefault)));
            marker = mMap.addMarker(markerOptions);
        } else {
            MarkerOptions markerOptions = new MarkerOptions();
            Timber.e("经度=：" + lat + "纬度=：" + lng);
            markerOptions.position(new LatLng(lat, lng));
            markerOptions.draggable(true);
            mMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(lat, lng)));
            marker = mMap.addMarker(markerOptions);
        }

        if(userInfo!=null){
            httpFindCoordinateListByGridId(userInfo.getGridId());
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
//                Timber.d("=====onMapClick " + "lat: " + latLng.latitude + "lng: " + latLng.longitude);
                lng = latLng.longitude;
                lat = latLng.latitude;
                if(marker!=null){
                    marker.setPosition(new LatLng(latLng.latitude, latLng.longitude));
                }
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
                Log.i(this.getClass().getName(),"地图移动位置得到经纬度：lng=="+lng+"lat=="+lat);
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
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
    }



    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();

    }

    @Override
    protected void onPause() {
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
        if(mMap!=null){
            mMap.clear();
            mMap = null;
        }
    }

    /**
     * 添加网格员显示区域
     */
    private void initPolylineOptions(List<GridBorder> gridBorderList){
        if(gridBorderList!=null && gridBorderList.size()>0){
            PolylineOptions polylineOptions = new PolylineOptions();
            for (int i=0;i<gridBorderList.size();i++){
                LatLng latLng = new LatLng(gridBorderList.get(i).getLat(),gridBorderList.get(i).getLng());
                polylineOptions.add(latLng);
            }
            polylineOptions.width(5);
            polylineOptions.color(getResources().getColor(R.color.blue));
            if(mMap!=null){
                mMap.addPolyline(polylineOptions);
            }


//            // 绘制一条虚线
//            mMap.addPolyline(new PolylineOptions()
//                    .add(new LatLng(40.001106, 116.487254), new LatLng(39.999783, 116.485666))
//                    .width(10).color(Color.CYAN)
//                    .setDottedLine(true));
//
//            mMap.addCircle(new CircleOptions()
//                    .center(new LatLng(40.000399, 116.487232))
//                    .radius(300).strokeWidth(3)
//                    .strokeColor(0xff00aa00)
//                    .fillColor(0xff00aaaa));
//
//            MarkerOptions markerOptions = new MarkerOptions();
//            markerOptions.position(new LatLng(40.000399, 116.487232));
//            markerOptions.draggable(true);
//            mMap.addMarker(markerOptions);
        }
    }

    /**
     * 显示网格边界
     */
    private void httpFindCoordinateListByGridId(int gridId) {

        OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("gridId", gridId+"")
                .build();
        Request request = new Request.Builder().url(Api.BASE_URL + "/grid/findCoordinateListByGridId.json").post(formBody).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(MapActivity.this.isFinishing())return;
                String result = response.body().string();
                Log.i(this.getClass().getName(), "11111111111111111111111获取网格员所在地区网格边界" + result);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    int code = jsonObject.getInt("result");
                    if(code==0){
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if(jsonArray!=null){
                            Gson gson = new Gson();
                            List<GridBorder> appList = gson.fromJson(jsonArray.toString(), new TypeToken<List<GridBorder>>() {
                            }.getType());
                            if(appList!=null){
                                MapActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        initPolylineOptions(appList);
                                    }
                                });
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


}
