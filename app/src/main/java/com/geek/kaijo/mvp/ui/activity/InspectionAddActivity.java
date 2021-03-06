package com.geek.kaijo.mvp.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cmmap.api.location.CmccLocation;
import com.cmmap.api.location.CmccLocationClient;
import com.cmmap.api.location.CmccLocationClientOption;
import com.cmmap.api.location.CmccLocationListener;
import com.geek.kaijo.R;
import com.geek.kaijo.Utils.GPSUtils;
import com.geek.kaijo.Utils.PermissionUtils;
import com.geek.kaijo.app.Constant;
import com.geek.kaijo.app.service.LocalService;
import com.geek.kaijo.di.component.DaggerInspectionAddComponent;
import com.geek.kaijo.di.module.InspectionAddModule;
import com.geek.kaijo.mvp.contract.InspectionAddContract;
import com.geek.kaijo.mvp.model.entity.Inspection;
import com.geek.kaijo.mvp.model.entity.UserInfo;
import com.geek.kaijo.mvp.presenter.InspectionAddPresenter;
import com.geek.kaijo.mvp.ui.adapter.InspectionAdapter;
import com.geek.kaijo.view.FlowRadioGroup;
import com.geek.kaijo.view.LoadingProgressDialog;
import com.geek.kaijo.view.PopupUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.DataHelper;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * 添加巡查项
 */
public class InspectionAddActivity extends BaseActivity<InspectionAddPresenter> implements InspectionAddContract.View {

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_thing_name)
    TextView tv_thing_name;
    @BindView(R.id.tv_map)
    TextView tv_map;
    @BindView(R.id.tv_location_lng)
    TextView tv_location_lng;
    @BindView(R.id.tv_location_lat)
    TextView tv_location_lat;
    @BindView(R.id.btn_save_back)
    TextView btn_save_back;
    @BindView(R.id.et_thing_remark)
    TextView et_thing_remark;
    @BindView(R.id.btn_back)
    TextView btn_back;

    View popView;
    private PopupUtils popupUtils;
    private List<Inspection> inspectionList;
    private LoadingProgressDialog loadingDialog;
    private int radioCheckedPosition;

    private MyHandler myHandler;

    private double mLat, mLng;
    private UserInfo userInfo;
    private InspectionAdapter adapter;
    private Inspection inspection;
    private RxPermissions rxPermissions;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerInspectionAddComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .inspectionAddModule(new InspectionAddModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_inspection_add; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        tvToolbarTitle.setText("添加巡查项");
        popupUtils = new PopupUtils();
        inspectionList = new ArrayList<>();
        myHandler = new MyHandler(this);
        userInfo = DataHelper.getDeviceData(this, Constant.SP_KEY_USER_INFO);
        inspection = (Inspection) getIntent().getSerializableExtra("Inspection");
        if (inspection != null) {
            tv_thing_name.setText(inspection.getName());
            tv_location_lng.setText(inspection.getLng() + "");
            tv_location_lat.setText(inspection.getLat() + "");
            et_thing_remark.setText(inspection.getRemark());
        }
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
                GPSUtils.getInstance().startLocation();
//                startService(new Intent(InspectionAddActivity.this, LocalService.class));
            } else {
                showPermissionsDialog();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GPSUtils.getInstance().removeLocationListener(locationListener);
        if (myHandler != null) {
            myHandler.removeCallbacksAndMessages(null);
        }
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

    @OnClick({R.id.tv_thing_name, R.id.tv_map, R.id.btn_save_back,R.id.btn_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_thing_name:
                mPresenter.findThingListBy("10");
                break;
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_map:
                boolean isOpen = GPSUtils.isOPen(this);//判断GPS是否打开
                if (!isOpen) {
                    GPSUtils.showGPSDialog(this);
                    return;
                }
                showLoading();
                GPSUtils.getInstance().setOnLocationListener(locationListener);
                myHandler.sendEmptyMessageDelayed(1,5000);
                break;
            case R.id.btn_save_back:
                if (userInfo != null) {
                    if(TextUtils.isEmpty(tv_thing_name.getText().toString().trim())){
                        Toast.makeText(this,"请选择巡查项",Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(mLat==0 || mLng==0){
                        Toast.makeText(this,"请选择位置",Toast.LENGTH_LONG).show();
                        return;
                    }
                    Inspection inspection = inspectionList.get(radioCheckedPosition);
                    mPresenter.addOrUpdateThingPosition(inspection.getThingId(), inspection.getName(), mLat, mLng, userInfo.getStreetId(), userInfo.getCommunityId(), userInfo.getGridId(), userInfo.getUserId());
                }
                break;

        }
    }

    private GPSUtils.LocationListener locationListener = new GPSUtils.LocationListener() {
        @Override
        public void onLocationChanged(CmccLocation cmccLocation) {
            if(InspectionAddActivity.this.isFinishing())return;
            if(myHandler!=null){
                myHandler.removeMessages(1);
            }
            hideLoading();
            if(cmccLocation!=null){
                mLat = cmccLocation.getLatitude();
                mLng = cmccLocation.getLongitude();
                if(mLat>0 && mLng>0){
                    tv_location_lat.setText(String.valueOf(mLat));
                    tv_location_lng.setText(String.valueOf(mLng));
                    Intent intent = new Intent(InspectionAddActivity.this, MapActivity.class);
                    intent.putExtra("lat", mLat);
                    intent.putExtra("lng", mLng);
                    InspectionAddActivity.this.startActivityForResult(intent, Constant.MAP_REQUEST_CODE);
                }else {
                    showNormalDialog();
                }

            }else {
                showNormalDialog();
            }
            GPSUtils.getInstance().removeLocationListener(locationListener);
        }
    };

    FlowRadioGroup flowRadioGroup;

    @Override
    public void httpInspectionSuccess(List<Inspection> inspectionList) {
        if (this.isFinishing()) return;
        if (inspectionList != null && inspectionList.size() > 0) {
            this.inspectionList = inspectionList;
            showPop();
        }
    }

    @Override
    public void httpAddInspectionSuccess(Inspection inspection) {
        if (this.isFinishing()) return;
        setResult(1);
        finish();
    }

    private void showPop() {
        if (popView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            popView = layoutInflater.inflate(R.layout.case_inspection_managet_itempop, null);
        }
        popupUtils.showPopWindowFromViewToUp(this, tv_thing_name, popView, inspectionList, new OnPopupWindowListener(), true);
    }

    class OnPopupWindowListener implements PopupUtils.PopupWindowListener {


        @Override
        public <T> void onInitView(View view, final T t) {
            TextView tv_toolbar_title = view.findViewById(R.id.tv_toolbar_title);
            tv_toolbar_title.setText("巡查项选择");

            RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
//            recyclerView.setLayoutManager(new LinearLayoutManager(InspectionAddActivity.this));
//            recyclerView.setHasFixedSize(true);

            recyclerView.setLayoutManager(new GridLayoutManager(InspectionAddActivity.this, 10));
//            recyclerView.addItemDecoration(new GridSpacingItemDecoration(10, 4, false));
            adapter = new InspectionAdapter(InspectionAddActivity.this, inspectionList);
//            adapter.setHasStableIds(true);  //edit 焦点错乱
            recyclerView.setAdapter(adapter);
            adapter.setOnItemOnClilcklisten(new InspectionAdapter.OnItemOnClicklisten() {
                @Override
                public void onItemClick(View v, int position) {
                    for (int i = 0; i < inspectionList.size(); i++) {
                        if (i == position) {
                            inspectionList.get(i).radioState = 1;
                        } else {
                            inspectionList.get(i).radioState = 0;
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            });

            TextView tv_toolbar_title_right_text = view.findViewById(R.id.tv_toolbar_title_right_text);
            tv_toolbar_title_right_text.setText("确定");
            tv_toolbar_title_right_text.setVisibility(View.GONE);
            tv_toolbar_title_right_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i = 0; i < inspectionList.size(); i++) {
                        if (inspectionList.get(i).radioState == 1) {
                            popupUtils.dismiss();
                            tv_thing_name.setText(inspectionList.get(i).getName());
                            break;
                        }
                    }

//                    tv_thing_name.setText(inspectionList.get(radioCheckedPosition).getName() );
                }
            });

//            mAdapter = new InspectionPopAdapter(mCaseList);
//            mAdapter.setOnItemClickListener((view, viewType, data, position) -> {
//                if(isCaseSearch){  //案件查询
//
//                }else {
//
//                }
//
//            });
//            recyclerView.setAdapter(mAdapter);
            if (flowRadioGroup == null) {
                flowRadioGroup = view.findViewById(R.id.flowRadioGroup);
            } else {
                flowRadioGroup.removeAllViews();
            }


            for (int i = 0; i < inspectionList.size(); i++) {
//                RadioButtonVertical radioButton = new RadioButtonVertical(InspectionAddActivity.this);
                RadioButton radioButton = new RadioButton(InspectionAddActivity.this);
                RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(20, 10, 20, 10);
                radioButton.setLayoutParams(layoutParams);
                radioButton.setText(inspectionList.get(i).getName());
                radioButton.setTextColor(getResources().getColor(R.color.tab_text_color));
                radioButton.setButtonDrawable(null);
                radioButton.setEms(1);
                radioButton.setGravity(Gravity.CENTER);
//                radioButton.setTextSize(getResources().getDimension(R.dimen.t10));
                radioButton.setTextSize(12);
//                radioButton.setPadding(10,10,10,10);
//@android:drawable/btn_radio
//                radioButton.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.book),null,null);
//                radioButton.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(android.R.drawable.btn_radio),null,null);
                radioButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.selector_radio_button), null, null);
//                radioButton.setCompoundDrawablePadding(10);
//                radioButton.setLayoutDirection(RadioButton.LAYOUT_DIRECTION_INHERIT);
//                radioButton.setTextDirection(RadioButton.TEXT_DIRECTION_INHERIT);
//                android:layoutDirection="rtl"
//                android:textDirection="ltr"
//                radioButton.setTextDirection(RadioButton.TEXT_DIRECTION_LOCALE);

                flowRadioGroup.addView(radioButton);
            }

            flowRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                    RadioButton radiobutton = (RadioButton) view.findViewById(radioGroup.getCheckedRadioButtonId());
                    for (int i = 0; i < inspectionList.size(); i++) {
                        if (inspectionList.get(i).getName().equals(radiobutton.getText().toString())) {
                            radioCheckedPosition = i;
                            break;
                        }
                    }

                }
            });

            TextView btn_ok = view.findViewById(R.id.btn_ok);
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupUtils.dismiss();
                    tv_thing_name.setText(inspectionList.get(radioCheckedPosition).getName());
                }
            });
            TextView btn_cancel = view.findViewById(R.id.btn_cancel);
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupUtils.dismiss();
                }
            });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.MAP_REQUEST_CODE && resultCode == Constant.MAP_REQUEST_CODE) {
            if (data != null) {
                mLng = data.getDoubleExtra("lng", 0);
                mLat = data.getDoubleExtra("lat", 0);

                tv_location_lng.setText(String.valueOf(mLng));
                tv_location_lat.setText(String.valueOf(mLat));
            }
        }
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
                PermissionUtils.permissionSkipSetting(InspectionAddActivity.this);
            }
        });
        builder.show();
    }

    private static class MyHandler extends Handler {
        private final WeakReference<InspectionAddActivity> weakTrainModelActivity;

        public MyHandler(InspectionAddActivity activity) {
            weakTrainModelActivity = new WeakReference<InspectionAddActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            InspectionAddActivity weakReference;
            if (weakTrainModelActivity.get() == null) {
                return;
            } else {
                weakReference = weakTrainModelActivity.get();
            }
            switch (msg.what) {
                case 1:
                    weakReference.hideLoading();
                    weakReference.showNormalDialog();
                    GPSUtils.getInstance().removeLocationListener(weakReference.locationListener);
                    break;
            }
        }
    }

    private void showNormalDialog(){
        if(this.isFinishing())return;
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(this);
        normalDialog.setTitle("定位");
        normalDialog.setMessage("手机定位失败，获得中心点坐标");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(InspectionAddActivity.this, MapActivity.class);
                        intent.putExtra("lat", mLat);
                        intent.putExtra("lng", mLng);
                        startActivityForResult(intent, Constant.MAP_REQUEST_CODE);
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        // 显示
        normalDialog.show();
    }

}
