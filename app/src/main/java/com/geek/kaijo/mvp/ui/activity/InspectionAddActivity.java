package com.geek.kaijo.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cmcc.api.fpp.bean.CmccLocation;
import com.cmcc.api.fpp.bean.LocationParam;
import com.cmcc.api.fpp.login.SecurityLogin;
import com.geek.kaijo.R;
import com.geek.kaijo.app.Constant;
import com.geek.kaijo.di.component.DaggerInspectionAddComponent;
import com.geek.kaijo.di.module.InspectionAddModule;
import com.geek.kaijo.mvp.contract.InspectionAddContract;
import com.geek.kaijo.mvp.model.entity.Inspection;
import com.geek.kaijo.mvp.model.entity.UserInfo;
import com.geek.kaijo.mvp.presenter.InspectionAddPresenter;
import com.geek.kaijo.mvp.ui.adapter.CaseAdapter;
import com.geek.kaijo.mvp.ui.adapter.InspectionPopAdapter;
import com.geek.kaijo.view.FlowRadioGroup;
import com.geek.kaijo.view.LoadingProgressDialog;
import com.geek.kaijo.view.PopupUtils;
import com.geek.kaijo.view.RadioButtonVertical;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.DataHelper;

import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class InspectionAddActivity extends BaseActivity<InspectionAddPresenter> implements InspectionAddContract.View {

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
    View popView;
    private PopupUtils popupUtils;
    private List<Inspection> inspectionList;
    private LoadingProgressDialog loadingDialog;
    private int radioCheckedPosition;

    private MessageHandler handler;
    private LocationParam locParam = null;//移动定位
    private SecurityLogin mClient;

    private double mLat, mLng;
    private UserInfo userInfo;

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
        popupUtils = new PopupUtils();
        inspectionList = new ArrayList<>();
        handler = new MessageHandler();
        initLocation();
        userInfo = DataHelper.getDeviceData(this, Constant.SP_KEY_USER_INFO);
    }
    @Override
    protected void onStart() {
        mClient.start();
        super.onStart();
    }

    @Override
    protected void onPause() {
        mClient.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mClient.restart();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mClient.stop();
        super.onDestroy();
        if(handler!=null){
            handler.removeCallbacksAndMessages(null);
        }
        handler = null;
        locParam = null;
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

    @OnClick({R.id.tv_thing_name,R.id.tv_map,R.id.btn_save_back})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.tv_thing_name:
                mPresenter.findThingListBy("10");
                break;
            case R.id.tv_map:
                startLocation();
                break;
            case R.id.btn_save_back:
                if(userInfo!=null){
                    Inspection inspection = inspectionList.get(radioCheckedPosition);
                    mPresenter.addOrUpdateThingPosition(inspection.getThingId(),inspection.getName(),mLat,mLng,userInfo.getStreetId(),userInfo.getCommunityId(),userInfo.getGridId(),userInfo.getUserId());
                }
                break;
        }
    }
    FlowRadioGroup flowRadioGroup;

    @Override
    public void httpInspectionSuccess(List<Inspection> inspectionList) {
        if(this.isFinishing())return;
        if(inspectionList!=null && inspectionList.size()>0){
            this.inspectionList = inspectionList;
            showPop();
        }
    }

    @Override
    public void httpAddInspectionSuccess(Inspection inspection) {
        if(this.isFinishing())return;
        setResult(1);
        finish();
    }

    private void showPop(){
        if (popView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            popView = layoutInflater.inflate(R.layout.case_inspection_managet_itempop, null);
        }
        popupUtils.showPopWindowFromViewToUp(this, tv_thing_name, popView, inspectionList, new OnPopupWindowListener(),true);
    }

    class OnPopupWindowListener implements PopupUtils.PopupWindowListener{


        @Override
        public <T> void onInitView(View view, final T t) {
            TextView tv_toolbar_title = view.findViewById(R.id.tv_toolbar_title);
            tv_toolbar_title.setText("巡查项选择");
//            RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
//            recyclerView.setLayoutManager(new LinearLayoutManager(InspectionAddActivity.this));
//            recyclerView.setHasFixedSize(true);

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
            if(flowRadioGroup==null){
                flowRadioGroup = view.findViewById(R.id.flowRadioGroup);
            }else {
                flowRadioGroup.removeAllViews();
            }


            for(int i=0;i<inspectionList.size();i++){
//                RadioButtonVertical radioButton = new RadioButtonVertical(InspectionAddActivity.this);
                RadioButton radioButton = new RadioButton(InspectionAddActivity.this);
                RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
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
                radioButton.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(android.R.drawable.btn_radio),null,null);
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
                    RadioButton radiobutton = (RadioButton)view.findViewById(radioGroup.getCheckedRadioButtonId());
                    for(int i=0;i<inspectionList.size();i++){
                        if(inspectionList.get(i).getName().equals(radiobutton.getText().toString())){
                            radioCheckedPosition = i;
                            break;
                        }
                    }
                    popupUtils.dismiss();
                    tv_thing_name.setText(inspectionList.get(radioCheckedPosition).getName() );
                }
            });

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
    private void startLocation() {
        new Thread(() -> {
            Message msg = Message.obtain();
            msg.what = 0x1233;
            try {
                CmccLocation loc = mClient.locCapability();
                mLat = loc.getLatitude();
                mLng = loc.getLongitude();
                if (handler != null)
                    handler.sendMessage(msg);
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
        }).start();
    }
    private class MessageHandler extends Handler {
        public MessageHandler() {
            super();
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x1233) {
                mClient.pause();


                tv_location_lng.setText(String.valueOf(mLat));
                tv_location_lat.setText(String.valueOf(mLng));

//                if (mLat == 0 || mLng == 0) {
//                    launchActivity(new Intent(ReportActivity.this, MapActivity.class));
//                } else {
                Intent intent = new Intent(InspectionAddActivity.this, MapActivity.class);
                intent.putExtra("lat", mLat);
                intent.putExtra("lng", mLng);
                launchActivity(intent);
//                }

            }
            super.handleMessage(msg);
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
}
