package com.geek.kaijo.mvp.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.cmmap.api.maps.Map;
import com.cmmap.api.maps.MapView;
import com.cmmap.api.maps.model.BitmapDescriptor;
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

    private double lng, lat;
    private LocationManager locationManager;
    private String provider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> locationList = locationManager.getProviders(true);
        if (locationList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (locationList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
        }

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            showLocation(location);
        }
        /**
         * 第一个参数是提供定位的方式
         * 第二个参数是多少秒刷新
         * 第三个参数是移动多少距离
         * 第四个参数是监听器
         */
        locationManager.requestLocationUpdates(provider, 5000, 1, locationListener);


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
        markerOptions.position(new LatLng(lat, lng));
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

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private void showLocation(Location location) {
        lng = location.getLatitude();
        lat = location.getLongitude();
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
        //将监听器移除
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(locationListener);
    }

}
