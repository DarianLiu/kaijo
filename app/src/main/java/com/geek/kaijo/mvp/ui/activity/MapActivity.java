package com.geek.kaijo.mvp.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.cmmap.api.maps.Map;
import com.cmmap.api.maps.MapView;
import com.cmmap.api.maps.model.LatLng;
import com.cmmap.api.maps.model.Marker;
import com.cmmap.api.maps.model.MarkerOptions;
import com.geek.kaijo.R;
import com.geek.kaijo.mvp.model.event.LocationEvent;

import org.simple.eventbus.EventBus;

import java.util.List;

/**
 * 地图
 * Created by LiuLi on 2018/9/8.
 */

public class MapActivity extends AppCompatActivity {

    private MapView mMapView;
    private Button btnSure;

    private Map mMap;

    private double lng = 38.9381318, lat = 121.6110992;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mMapView = findViewById(R.id.mMap);
        mMapView.onCreate(savedInstanceState);
        btnSure = findViewById(R.id.btn_sure);
        btnSure.setOnClickListener(v -> {
            LocationEvent event = new LocationEvent();
            event.setLat(lat);
            event.setLng(lng);
            EventBus.getDefault().post(event, "location");
            finish();
        });

        mMap = mMapView.getMap();
        mMap.setMapType(Map.MAP_TYPE_NORMAL);
//        mMap.showMapText(true);//是否显示文字
//        mMap.showBuildings(true);//是否显示建筑物
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(38.9381318, 121.6110992));
        markerOptions.draggable(true);
        mMap.addMarker(markerOptions);

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
        mMap.clear();
        mMap = null;
    }

}
