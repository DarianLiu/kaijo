package com.geek.kaijo.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cmmap.api.location.CmccLocation;
import com.cmmap.api.location.CmccLocationClient;
import com.cmmap.api.location.CmccLocationClientOption;
import com.cmmap.api.location.CmccLocationListener;
import com.geek.kaijo.R;
import com.geek.kaijo.Utils.GPSUtils;
import com.geek.kaijo.app.Constant;
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

    View popView;
    private PopupUtils popupUtils;
    private List<Inspection> inspectionList;
    private LoadingProgressDialog loadingDialog;
    private int radioCheckedPosition;

    private MessageHandler handler;

    private double mLat, mLng;
    private UserInfo userInfo;
    private InspectionAdapter adapter;
    private Inspection inspection;

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
        handler = new MessageHandler();
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
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        handler = null;
        GPSUtils.getInstance().removeLocationListener(locationListener);
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

    @OnClick({R.id.tv_thing_name, R.id.tv_map, R.id.btn_save_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_thing_name:
                mPresenter.findThingListBy("10");
                break;
            case R.id.tv_map:
                GPSUtils.getInstance().startLocation(locationListener);
                break;
            case R.id.btn_save_back:
                if (userInfo != null) {
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
            mLat = cmccLocation.getLatitude();
            mLng = cmccLocation.getLongitude();
            tv_location_lat.setText(String.valueOf(mLat));
            tv_location_lng.setText(String.valueOf(mLng));
            Intent intent = new Intent(InspectionAddActivity.this, MapActivity.class);
            intent.putExtra("lat", mLat);
            intent.putExtra("lng", mLng);
            InspectionAddActivity.this.startActivityForResult(intent, Constant.MAP_REQUEST_CODE);

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


    private class MessageHandler extends Handler {
        public MessageHandler() {
            super();
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x1233) {

            }
            super.handleMessage(msg);
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
}
