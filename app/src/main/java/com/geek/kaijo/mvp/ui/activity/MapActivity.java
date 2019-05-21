package com.geek.kaijo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cmmap.api.maps.CameraUpdateFactory;
import com.cmmap.api.maps.Map;
import com.cmmap.api.maps.MapView;
import com.cmmap.api.maps.model.Arc;
import com.cmmap.api.maps.model.LatLng;
import com.cmmap.api.maps.model.Marker;
import com.cmmap.api.maps.model.MarkerOptions;
import com.cmmap.api.maps.model.PolylineOptions;
import com.geek.kaijo.R;
import com.geek.kaijo.app.Constant;
import com.geek.kaijo.app.api.Api;
import com.geek.kaijo.mvp.model.entity.GridBorder;
import com.geek.kaijo.mvp.model.entity.UserInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jess.arms.utils.DataHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

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
    private TextView btnSure;

    //地图
    private Map mMap;
    private Marker marker;

    private double lng, lat;  //传递过来的经纬度
    private double selectLng, selectLat;  //传递过来的经纬度

    private UserInfo userInfo;
    private double lngDefault = 116.486073, latDefaut = 40.000565;  //定位失败 默认显示点
    private String MAP_LOOK;
    MarkerOptions markerOptions;


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

        if (!TextUtils.isEmpty(MAP_LOOK)) {
            btnSure.setVisibility(View.GONE);
        }
        initMap();

        btnSure.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("lng", selectLng);
            intent.putExtra("lat", selectLat);
            setResult(Constant.MAP_REQUEST_CODE, intent);
            finish();
        });

        if (lng == 0 || lat == 0) {  //定位失败  显示默认位置
            selectLng = lngDefault;
            selectLat = latDefaut;
//            checkPermissionAndAction();
            markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(latDefaut, lngDefault));
            markerOptions.draggable(true);
            mMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(latDefaut, lngDefault)));
            marker = mMap.addMarker(markerOptions);
        } else {
            selectLng = lng;
            selectLat = lat;
            markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(lat, lng));
            markerOptions.draggable(true);
            mMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(lat, lng)));
            marker = mMap.addMarker(markerOptions);
        }

        if (userInfo != null) {
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
                selectLng = latLng.longitude;
                selectLat = latLng.latitude;
                if (marker != null) {
                    marker.setPosition(new LatLng(selectLat, selectLng));
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
                selectLng = marker.getPosition().longitude;
                selectLat = marker.getPosition().latitude;
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
        if (mMap != null) {
            mMap.clear();
            mMap = null;
        }
    }

    /**
     * 添加网格员显示区域
     */
    private void initPolylineOptions(List<GridBorder> gridBorderList) {
        if (gridBorderList != null && gridBorderList.size() > 0) {
            PolylineOptions polylineOptions = new PolylineOptions();
            for (int i = 0; i < gridBorderList.size(); i++) {
                LatLng latLng = new LatLng(gridBorderList.get(i).getLat(), gridBorderList.get(i).getLng());
                polylineOptions.add(latLng);
            }
            polylineOptions.width(5);
            polylineOptions.color(getResources().getColor(R.color.blue));
            if (mMap != null) {
                mMap.addPolyline(polylineOptions);
            }
            if(lat==0 && lng==0){
                //显示中心点坐标
                for(int i=0;i<gridBorderList.size();i++){
                    if(gridBorderList.get(i).getMark()==1){  //mark=1中心点坐标
                        if(gridBorderList.get(i).getLng()>0 &&gridBorderList.get(i).getLat()>0){
                            selectLng = gridBorderList.get(i).getLng();
                            selectLat = gridBorderList.get(i).getLat();
                            markerOptions.position(new LatLng(selectLat, selectLng));
                            markerOptions.draggable(true);
                            mMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(selectLat, selectLng)));
                            if(marker!=null){
                                marker.remove();
                            }
                            marker = mMap.addMarker(markerOptions);
                        }
                        break;
                    }
                }
            }

        }
    }

    /**
     * 显示网格边界
     */
    private void httpFindCoordinateListByGridId(int gridId) {

        OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("gridId", gridId + "")
                .build();
        Request request = new Request.Builder().url(Api.BASE_URL + "/grid/findCoordinateListByGridId.json").post(formBody).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (MapActivity.this.isFinishing()) return;
                String result = response.body().string();
                Log.i(this.getClass().getName(), "11111111111111111111111获取网格员所在地区网格边界" + result);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    int code = jsonObject.getInt("result");
                    if (code == 0) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (jsonArray != null) {
                            Gson gson = new Gson();
                            List<GridBorder> appList = gson.fromJson(jsonArray.toString(), new TypeToken<List<GridBorder>>() {
                            }.getType());
                            if (appList != null) {
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
