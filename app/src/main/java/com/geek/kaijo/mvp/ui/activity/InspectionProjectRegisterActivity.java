package com.geek.kaijo.mvp.ui.activity;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmmap.api.location.CmccLocation;
import com.cmmap.api.location.CmccLocationQualityReport;
import com.cmmap.api.maps.CameraUpdateFactory;
import com.cmmap.api.maps.Map;
import com.cmmap.api.maps.MapView;
import com.cmmap.api.maps.model.LatLng;
import com.cmmap.api.maps.model.PolylineOptions;
import com.geek.kaijo.R;
import com.geek.kaijo.Utils.DateUtils;
import com.geek.kaijo.Utils.GPSUtils;
import com.geek.kaijo.Utils.PermissionUtils;
import com.geek.kaijo.app.Constant;
import com.geek.kaijo.app.MyApplication;
import com.geek.kaijo.app.service.LocalService;
import com.geek.kaijo.di.component.DaggerInspectionProjectRegisterComponent;
import com.geek.kaijo.di.module.InspectionProjectRegisterModule;
import com.geek.kaijo.mvp.contract.InspectionProjectRegisterContract;
import com.geek.kaijo.mvp.model.entity.GridBorder;
import com.geek.kaijo.mvp.model.entity.IPRegisterBean;
import com.geek.kaijo.mvp.model.entity.Inspection;
import com.geek.kaijo.mvp.model.entity.InspentionResult;
import com.geek.kaijo.mvp.model.entity.UserInfo;
import com.geek.kaijo.mvp.presenter.InspectionProjectRegisterPresenter;
import com.geek.kaijo.mvp.ui.adapter.IPRegisterAdapter;
import com.geek.kaijo.view.LoadingProgressDialog;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.DataHelper;
import com.jess.arms.utils.LogUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import dao.DaoSession;
import dao.IPRegisterBeanDao;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * 巡查项登记
 */
public class InspectionProjectRegisterActivity extends BaseActivity<InspectionProjectRegisterPresenter> implements InspectionProjectRegisterContract.View {

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_toolbar_title_right)
    ImageView tvToolbarTitleRight;
    @BindView(R.id.tv_toolbar_title_right_text)
    TextView tvToolbarTitleRightText;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;
    @BindView(R.id.ipr_start)
    Button iprStart;
    @BindView(R.id.ipr_complete)
    Button iprComplete;
    @BindView(R.id.ipr_cancel)
    Button iprCancel;

    @BindView(R.id.tv_lng)
    TextView tv_lng;
    @BindView(R.id.tv_lat)
    TextView tv_lat;
    @BindView(R.id.tv_time)
    TextView tv_time;


    private List<IPRegisterBean> mList;
    private IPRegisterAdapter mAdapter;
    private UserInfo userInfo;
    private LoadingProgressDialog loadingDialog;
    private LocationReceiver locationReceiver;
    private RxPermissions rxPermissions;
    private int sfState;
    private InspentionResult result;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerInspectionProjectRegisterComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .inspectionProjectRegisterModule(new InspectionProjectRegisterModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_inspection_project_register; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        tvToolbarTitle.setText(R.string.inspection_register);

        iprStart.setEnabled(false);
        iprComplete.setEnabled(false);
        iprCancel.setEnabled(false);

        userInfo = DataHelper.getDeviceData(this, Constant.SP_KEY_USER_INFO);
        result = DataHelper.getDeviceData(this, Constant.SP_KEY_Patrol_state);
//        sfState = DataHelper.getIntergerSF(this,Constant.SP_KEY_Patrol_state);
        if(result!=null){
            sfState = result.getState();
        }
        initView();

        locationReceiver = new LocationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.service_patrol);
        registerReceiver(locationReceiver, filter);

        GPSUtils.getInstance().setOnLocationListener(locationListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (rxPermissions == null) {
            rxPermissions = new RxPermissions(this);
        }
        //同时申请多个权限
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(granted -> {
            if (granted) {           // All requested permissions are granted
                boolean isOpen = GPSUtils.isOPen(this);//判断GPS是否打开
                if (!isOpen) {
                    GPSUtils.showGPSDialog(this);
                }
            } else {
                showPermissionsDialog();
            }
        });
    }

    private void refreshState(){

        if(sfState==1){ //开始巡查
            iprStart.setEnabled(false);
            iprComplete.setEnabled(true);
            iprCancel.setEnabled(true);
        }else {
            iprStart.setEnabled(true);
            iprComplete.setEnabled(false);
            iprCancel.setEnabled(false);
        }
    }

    /**
     * 初始化View
     */
    private void initView() {


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        mList = new ArrayList<>();
        mAdapter = new IPRegisterAdapter(mList);
        mAdapter.setOnItemClickListener((view, viewType, data, position) -> {

        });
        recyclerView.setAdapter(mAdapter);

        smartRefresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

                mPresenter.dbFindThingList();

            }
        });
        smartRefresh.autoRefresh();
        smartRefresh.setEnableLoadMore(false);
    }

    @Override
    public void showLoading() {
        if (loadingDialog == null)
            loadingDialog = new LoadingProgressDialog.Builder(this)
                    .setCancelable(true)
                    .setCancelOutside(true).create();
        if (!loadingDialog.isShowing())
            loadingDialog.show();
    }

    @Override
    public void hideLoading() {
        if (loadingDialog != null && loadingDialog.isShowing())
            loadingDialog.dismiss();
    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }


    @OnClick({R.id.ipr_start, R.id.ipr_complete, R.id.ipr_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ipr_start://开始巡查
                if(mPresenter!=null && userInfo!=null){
                    mPresenter.startPath(userInfo.getUserId(),1);
                }

                break;
            case R.id.ipr_complete://完成巡查
                if(mPresenter!=null && userInfo!=null && result!=null){
                    mPresenter.endPath(userInfo.getUserId(),2,mList,result.getPathId());
                }
                break;
            case R.id.ipr_cancel://取消巡查
                if(mPresenter!=null && userInfo!=null && result!=null){
                    mPresenter.cancelPath(userInfo.getUserId(),3,result.getPathId());
                }

                break;
        }
    }

    @Override
    public void httpGetThingListSuccess(List<IPRegisterBean> inspectionList) {
        if(inspectionList!=null){
            mList.clear();
            mList.addAll(inspectionList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void httpStartSuccess(InspentionResult result) {
        if(this.isFinishing() || result==null)return;
        this.result = result;
//        DataHelper.setIntergerSF(MyApplication.get(), Constant.SP_KEY_Patrol_state, 1);
        DataHelper.saveDeviceData(this,Constant.SP_KEY_Patrol_state,result);
        sfState = result.getState();
        iprComplete.setEnabled(true);
        iprCancel.setEnabled(true);
        iprStart.setEnabled(false);
        Intent intent = new Intent();
        intent.putExtra(Constant.SP_KEY_Patrol_state, sfState);
        intent.setAction(Constant.SP_KEY_Patrol_state);
        sendBroadcast(intent);


    }

    @Override
    public void httpEndSuccess(InspentionResult result) {   //结束巡查
        if(this.isFinishing() || result==null)return;
        if(mList!=null){
            for(int i=0;i<mList.size();i++){
                mList.get(i).setStatus(0);
            }
            mPresenter.dbEndState(mList);
        }

    }

    @Override
    public void httpCancelSuccess(InspentionResult result) {
//        DataHelper.setIntergerSF(MyApplication.get(), Constant.SP_KEY_Patrol_state, 0);
        if(this.isFinishing() || result==null)return;

        DataHelper.saveDeviceData(this,Constant.SP_KEY_Patrol_state,result);
        sfState = result.getState();
        if(mList!=null){
            for(int i=0;i<mList.size();i++){
                mList.get(i).setStatus(0);
            }
            mAdapter.notifyDataSetChanged();
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    DaoSession daoSession1 = MyApplication.get().getDaoSession();
                    IPRegisterBeanDao ipRegisterBeanDao = daoSession1.getIPRegisterBeanDao();
                    ipRegisterBeanDao.insertOrReplaceInTx(mList);
                }
            }.start();
        }

        refreshState();
        Intent intent = new Intent();
        intent.putExtra(Constant.SP_KEY_Patrol_state, 0);
        intent.setAction(Constant.SP_KEY_Patrol_state);
        sendBroadcast(intent);
    }

    @Override
    public void dbEndStateSuccess() { //结束巡查 数据库状态更新成功
        if(this.isFinishing())return;
        if(mAdapter!=null){
            mAdapter.notifyDataSetChanged();
        }
        if(result!=null){
            result.setState(2);
        }
        DataHelper.saveDeviceData(this,Constant.SP_KEY_Patrol_state,result);
//        DataHelper.setIntergerSF(MyApplication.get(), Constant.SP_KEY_Patrol_state, 2);
        sfState = 2;
        refreshState();
        Intent intent = new Intent();
        intent.putExtra(Constant.SP_KEY_Patrol_state, sfState);
        intent.setAction(Constant.SP_KEY_Patrol_state);
        sendBroadcast(intent);
    }

    @Override
    public void finishRefresh() {
        smartRefresh.finishRefresh();

    }

    @Override
    public void dbGetThingListSuccess(List<IPRegisterBean> result) {  //数据库初始化数据
        if(result!=null){
            mList.clear();
            mList.addAll(result);
            mAdapter.notifyDataSetChanged();

            refreshState();

            Intent intent = new Intent();
            intent.putExtra(Constant.SP_KEY_Patrol_state, sfState);
            intent.putExtra(Constant.SP_KEY_Patrol_state_db, 19);
            intent.setAction(Constant.SP_KEY_Patrol_state);
            sendBroadcast(intent);

        }
        if(userInfo!=null){  //开始巡查状态
            mPresenter.findThingPositionListBy(String.valueOf(userInfo.getStreetId()),String.valueOf(userInfo.getCommunityId()),String.valueOf(userInfo.getGridId()));
        }

    }

    @Override
    public void dbHttpShowContent(List<IPRegisterBean> result) { //网络获取后 与本地状态合并后的数据
        if(result!=null){
            mList.clear();
            mList.addAll(result);
            mAdapter.notifyDataSetChanged();

            Intent intent = new Intent();
            intent.putExtra(Constant.SP_KEY_Patrol_state, sfState);
            intent.putExtra(Constant.SP_KEY_Patrol_state_db, 19);
            intent.setAction(Constant.SP_KEY_Patrol_state);
            sendBroadcast(intent);
        }

    }


    private GPSUtils.LocationListener locationListener = new GPSUtils.LocationListener() {
        @Override
        public void onLocationChanged(CmccLocation cmccLocation) {
            if(InspectionProjectRegisterActivity.this.isFinishing())return;
            if(cmccLocation!=null){
                if(cmccLocation.getErrorCode()==0){
                    tv_lng.setText("经度："+cmccLocation.getLongitude());
                    tv_lat.setText("纬度："+cmccLocation.getLatitude());
                    tv_time.setText("定位时间："+formatUTC(cmccLocation.getTime(), "yyyy-MM-dd HH:mm:ss"));
                }else {
                    tv_lng.setText("错误码："+cmccLocation.getErrorCode()+"\n错误描述"+cmccLocation.getLocationDetail()+"\nGPS卫星数"+cmccLocation.getLocationQualityReport().getGPSSatellites());
                    tv_lat.setText("错误信息："+cmccLocation.getErrorInfo()+"\nGPS状态"+getGPSStatusString(cmccLocation.getLocationQualityReport().getGPSStatus()));
                    tv_time.setText("回调时间："+formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
                }

            }else {
                tv_lng.setText("经度：CmccLocation==null");
                tv_lat.setText("纬度：CmccLocation==null");
            }

        }
    };

    //内部类，实现BroadcastReceiver
    public class LocationReceiver extends BroadcastReceiver {
        //必须要重载的方法，用来监听是否有广播发送
        @Override
        public void onReceive(Context context, Intent intent) {
            String intentAction = intent.getAction();
            if (intentAction.equals(Constant.service_patrol)) {  //service里发送广播 更新UI
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        DaoSession daoSession1 = MyApplication.get().getDaoSession();
                        IPRegisterBeanDao ipRegisterBeanDao = daoSession1.getIPRegisterBeanDao();
                        List<IPRegisterBean> ipRegisterBeanList = ipRegisterBeanDao.loadAll();

                        InspectionProjectRegisterActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mList.clear();
                                mList.addAll(ipRegisterBeanList);
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }.start();

            }
        }
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
                PermissionUtils.permissionSkipSetting(InspectionProjectRegisterActivity.this);
            }
        });
        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GPSUtils.getInstance().removeLocationListener(locationListener);
        unregisterReceiver(locationReceiver);
    }

    /**
     * 获取GPS状态的字符串
     *
     * @param statusCode GPS状态码
     * @return
     */
    private String getGPSStatusString(int statusCode) {
        String str = "";
        switch (statusCode) {
            case CmccLocationQualityReport.GPS_STATUS_OK:
                str = "GPS状态正常";
                break;
            case CmccLocationQualityReport.GPS_STATUS_NOGPSPROVIDER:
                str = "手机中没有GPS Provider，无法进行GPS定位";
                break;
            case CmccLocationQualityReport.GPS_STATUS_OFF:
                str = "GPS关闭，建议开启GPS，提高定位质量";
                break;
            case CmccLocationQualityReport.GPS_STATUS_MODE_SAVING:
                str = "选择的定位模式中不包含GPS定位，建议选择包含GPS定位的模式，提高定位质量";
                break;
            case CmccLocationQualityReport.GPS_STATUS_NOGPSPERMISSION:
                str = "没有GPS定位权限，建议开启gps定位权限";
                break;
        }
        return str;
    }

    private static SimpleDateFormat sdf = null;
    public  static String formatUTC(long l, String strPattern) {
        if (TextUtils.isEmpty(strPattern)) {
            strPattern = "yyyy-MM-dd HH:mm:ss";
        }
        if (sdf == null) {
            try {
                sdf = new SimpleDateFormat(strPattern, Locale.CHINA);
            } catch (Throwable e) {
            }
        } else {
            sdf.applyPattern(strPattern);
        }
        return sdf == null ? "NULL" : sdf.format(l);
    }

}
