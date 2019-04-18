package com.geek.kaijo.mvp.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.geek.kaijo.R;
import com.geek.kaijo.app.Constant;
import com.geek.kaijo.app.EventBusTags;
import com.geek.kaijo.di.component.DaggerMainComponent;
import com.geek.kaijo.di.module.MainModule;
import com.geek.kaijo.mvp.contract.MainContract;
import com.geek.kaijo.mvp.model.entity.Banner;
import com.geek.kaijo.mvp.model.entity.UserInfo;
import com.geek.kaijo.mvp.presenter.MainPresenter;
import com.geek.kaijo.mvp.ui.fragment.CaseManagerFragment;
import com.geek.kaijo.mvp.ui.fragment.MyMessageFragment;
import com.geek.kaijo.mvp.ui.fragment.SocialManageFragment;
import com.geek.kaijo.view.FragmentTabHost;
import com.geek.kaijo.view.autoviewpager.AutoScrollViewPager;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.DataHelper;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.simple.eventbus.Subscriber;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import io.reactivex.functions.Consumer;

import static com.geek.kaijo.app.api.Api.URL_FILE_UPLOAD;
import static com.jess.arms.utils.Preconditions.checkNotNull;


public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(android.R.id.tabs)
    TabWidget tabs;
    @BindView(android.R.id.tabhost)
    FragmentTabHost tabHost;
    @BindView(R.id.auto_scroll)
    AutoScrollViewPager autoScroll;

    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_grid_name)
    TextView tvGridName;


    @BindString(R.string.tab_case)
    String str_tab_case;
    @BindString(R.string.tab_social)
    String str_tab_social;
    @BindString(R.string.tab_business)
    String str_tab_business;
    @BindString(R.string.tab_service)
    String str_tab_service;
    @BindString(R.string.tab_message)
    String str_tab_message;

    @BindDrawable(R.drawable.selector_tab_case)
    Drawable selector_tab_case;
    @BindDrawable(R.drawable.selector_tab_social)
    Drawable selector_tab_social;
    @BindDrawable(R.drawable.selector_tab_mine)
    Drawable selector_tab_mine;
    @BindDrawable(R.drawable.selector_tab_service)
    Drawable selector_tab_service;
    @BindDrawable(R.drawable.selector_tab_business)
    Drawable selector_tab_business;

    @Inject
    ImageLoader mImageLoader;

    //    //轮播图低部滑动图片红点
//    private ArrayList<ImageView> mScrollImageViews = new ArrayList<>();
    //轮播图图片
    private List<Banner> mBannerList = new ArrayList<>();

    private double latitude = 0.0; //经度
    private double longitude = 0.0;
    private MyHandler myHandler;
    private String userId;
    private RxPermissions rxPermissions;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerMainComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .mainModule(new MainModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_main; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        tvToolbarTitle.setText(R.string.app_name);
        UserInfo userInfo = DataHelper.getDeviceData(this, Constant.SP_KEY_USER_INFO);
        if (userInfo == null) {
            launchActivity(new Intent(this, LoginActivity.class));
        } else {
            updateUserInfo(userInfo);
        }
        initTabHost();
        if (mPresenter != null) {
            mPresenter.findAllBannerList();
//            mPresenter.findStreetById();
        }

        setBannerHeight();

        initGpsLocation();
        userId = DataHelper.getStringSF(this, Constant.SP_KEY_USER_ID);
        myHandler = new MyHandler(this);
        myHandler.sendEmptyMessageDelayed(1, 3000);
    }

    /**
     * 设置banner控件的高度
     */
    private void setBannerHeight() {
        int screenWidth = ArmsUtils.getScreenWidth(getApplicationContext());
        int height = screenWidth / 5 * 2;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidth, height);
        autoScroll.setLayoutParams(params);
    }

    /**
     * 初始化FragmentTabHost(底部菜单点击事件处理)
     */
    public void initTabHost() {
        tabHost.setup(MainActivity.this, getSupportFragmentManager(), android.R.id.tabcontent);
        tabHost.getTabWidget().setDividerDrawable(null); // 去掉分割线


        for (int i = 0; i < 3; i++) {
            TabHost.TabSpec tabSpec;
            switch (i) {
                case 0:
                    tabSpec = tabHost.newTabSpec(str_tab_case)
                            .setIndicator(getTabView(str_tab_case, selector_tab_case));
                    tabHost.addTab(tabSpec, CaseManagerFragment.class, null);
                    break;
                case 1:
                    tabSpec = tabHost.newTabSpec(str_tab_social)
                            .setIndicator(getTabView(str_tab_social, selector_tab_social));
                    tabHost.addTab(tabSpec, SocialManageFragment.class, null);
                    break;
//                case 2:
//                    tabSpec = tabHost.newTabSpec(str_tab_business)
//                            .setIndicator(getTabView(str_tab_business, selector_tab_business));
//                    tabHost.addTab(tabSpec, BusinessEnvironmentFragment.class, null);
//                    break;
//                case 3:
//                    tabSpec = tabHost.newTabSpec(str_tab_service)
//                            .setIndicator(getTabView(str_tab_service, selector_tab_service));
//                    tabHost.addTab(tabSpec, BenefitServiceFragment.class, null);
//                    break;
                case 2:
                    tabSpec = tabHost.newTabSpec(str_tab_message)
                            .setIndicator(getTabView(str_tab_message, selector_tab_mine));
                    tabHost.addTab(tabSpec, MyMessageFragment.class, null);
                    break;

            }
        }
    }


    /**
     * 获取当前tab视图
     */
    private View getTabView(String tabName, Drawable tabResource) {
        View view = getLayoutInflater().inflate(R.layout.view_tab, null);
        ImageView imageView = view.findViewById(R.id.iv_menu);
        TextView textView = view.findViewById(R.id.tv_menu);
        imageView.setImageDrawable(tabResource);
        textView.setText(tabName);
        return view;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.makeText(getApplicationContext(), message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    /**
     * 初始化轮播图控件
     */
    private void initAutoScrollViewPager() {
        autoScroll.setAdapter(mPagerAdapter);

        autoScroll.setScrollFactgor(10); // 控制滑动速度
        autoScroll.setOffscreenPageLimit(5); //设置缓存屏数
        autoScroll.startAutoScroll(3000);  //设置间隔时间

        autoScroll.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
//                showSelectScrollImage(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    /**
     * 设置轮播图数据
     *
     * @param banners 轮播图列表
     */
    @Override
    public void setAutoBanner(List<Banner> banners) {
        mBannerList = banners;
        initAutoScrollViewPager();
    }

    @Subscriber(tag = EventBusTags.TAG_LOGIN_STATE)
    public void receiveLoginState(UserInfo userInfo) {
        if (userInfo != null)
            updateUserInfo(userInfo);
    }

    /**
     * 更新用户网格信息
     *
     * @param userInfo 用户网格信息
     */
    public void updateUserInfo(UserInfo userInfo) {
        tvUserName.setText(String.format("登录名称：%s", userInfo.getUsername()));
        tvGridName.setText(String.format("网格名称：%s", userInfo.getGridName()));
    }

//    /**
//     * 当前滑动的轮播图对应底部的标识
//     *
//     * @param position 当前位置
//     */
//    private void showSelectScrollImage(int position) {
//        if (position < 0 || position >= mScrollImageViews.size()) return;
//        if (mScrollImageViews != null) {
//            for (ImageView iv : mScrollImageViews) {
//                iv.setImageResource(R.drawable.icon_indicator_normal);
//            }
//            mScrollImageViews.get(position).setImageResource(R.drawable.icon_indicator_selected);
//        }
//    }

//    /**
//     * 轮播图底部的滑动的下划线
//     *
//     * @param size 轮播图数量
//     */
//    private void addScrollImage(int size) {
////        autoScrollIndicator.removeAllViews();
//        mScrollImageViews.clear();
//
//        for (int i = 0; i < size; i++) {
//            ImageView iv = new ImageView(getActivity());
//            iv.setPadding(10, 0, 10, 20);
//            if (i != 0) {
//                iv.setImageResource(R.drawable.icon_indicator_normal);
//            } else {
//                iv.setImageResource(R.drawable.icon_indicator_selected);
//            }
//            iv.setLayoutParams(new ViewGroup.LayoutParams(40, 40));
////            autoScrollIndicator.addView(iv);// 将图片加到一个布局里
//            mScrollImageViews.add(iv);
//        }
//    }

    /**
     * 轮播图适配器
     */
    PagerAdapter mPagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return mBannerList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
            View view = getLayoutInflater().inflate(R.layout.include_viewpager_banner, null);
            ImageView ivBanner = view.findViewById(R.id.bannerImg);
            Log.e("====", "====== imageUlr: " + URL_FILE_UPLOAD + "/" + mBannerList.get(position).getUrl());
            mImageLoader.loadImage(MainActivity.this, ImageConfigImpl.builder()
                    .url(URL_FILE_UPLOAD + "/" + mBannerList.get(position).getUrl())
                    .isCenterCrop(true)
                    .imageView(ivBanner)
                    .build());
            container.addView(view);

//            view.setOnClickListener(v -> {
//                if (!TextUtils.isEmpty(bannerBeans.get(position).getEvent())) {
//                    skipType(bannerBeans.get(position).getEvent(), bannerBeans.get(position).getUrl());
//                }
//
//            });
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBannerList != null) {
            mBannerList.clear();
            mBannerList = null;
        }
        mImageLoader = null;
        mPagerAdapter = null;
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private long exitTime = 0;

    /**
     * 退出应用
     */
    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            showMessage("再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            ArmsUtils.exitApp();
        }
    }

    private void initGpsLocation() {
        if (rxPermissions == null) {
            rxPermissions = new RxPermissions(this);
        }
        rxPermissions.requestEach(Manifest.permission.ACCESS_FINE_LOCATION) //gps定位
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            getGpsLocation();
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
//                            Log.d(TAG, permission.name + " is denied. More info should be provided.");
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
//                            Log.d(TAG, permission.name + " is denied.");
//                            showDialog();

                        }
                    }
                });

    }

    private void getGpsLocation(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }else {
                Location locationNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (locationNet != null) {
                    latitude = locationNet.getLatitude(); //经度
                    longitude = locationNet.getLongitude(); //纬度
                }
            }
        } else {
            LocationListener locationListener = new LocationListener() {

                // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                // Provider被enable时触发此函数，比如GPS被打开
                @Override
                public void onProviderEnabled(String provider) {

                }

                // Provider被disable时触发此函数，比如GPS被关闭
                @Override
                public void onProviderDisabled(String provider) {

                }

                //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
                @Override
                public void onLocationChanged(Location location) {
                    if (location != null) {
                        Log.e("Map", "Location changed : Lat: "
                                + location.getLatitude() + " Lng: "
                                + location.getLongitude());
                    }
                }
            };
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude(); //经度
                longitude = location.getLongitude(); //纬度
            }

        }
    }

    private static class MyHandler extends Handler {
        private final WeakReference<MainActivity> weakTrainModelActivity;

        public MyHandler(MainActivity activity) {
            weakTrainModelActivity = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainActivity weakActivity;
            if (weakTrainModelActivity.get() == null) {
                return;
            } else {
                weakActivity = weakTrainModelActivity.get();
            }
            switch (msg.what) {
                case 1:
                    if (weakActivity.userId != null && weakActivity.latitude > 0 && weakActivity.longitude > 0) {
                        weakActivity.mPresenter.uploadGpsLocation(weakActivity.userId, weakActivity.latitude, weakActivity.longitude);
                        sendEmptyMessageDelayed(1, 60000); //1分钟 上传一次经纬度
                    }
                    break;

            }
        }
    }
}
