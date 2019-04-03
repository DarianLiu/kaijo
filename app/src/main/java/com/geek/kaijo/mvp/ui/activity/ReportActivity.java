package com.geek.kaijo.mvp.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.cmcc.api.fpp.bean.CmccLocation;
import com.cmcc.api.fpp.bean.LocationParam;
import com.cmcc.api.fpp.login.SecurityLogin;
import com.geek.kaijo.R;
import com.geek.kaijo.Utils.DateUtils;
import com.geek.kaijo.Utils.PermissionUtils;
import com.geek.kaijo.app.Constant;
import com.geek.kaijo.di.component.DaggerReportComponent;
import com.geek.kaijo.di.module.ReportModule;
import com.geek.kaijo.mvp.contract.ReportContract;
import com.geek.kaijo.mvp.model.entity.CaseAttribute;
import com.geek.kaijo.mvp.model.entity.Grid;
import com.geek.kaijo.mvp.model.entity.Street;
import com.geek.kaijo.mvp.model.event.LocationEvent;
import com.geek.kaijo.mvp.presenter.ReportPresenter;
import com.geek.kaijo.mvp.ui.adapter.MySpinnerAdapter;
import com.geek.kaijo.view.LoadingProgressDialog;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.widget.CustomPopupWindow;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.simple.eventbus.Subscriber;
import org.xml.sax.SAXException;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * 上报页面（自行/非自行）
 */
public class ReportActivity extends BaseActivity<ReportPresenter> implements ReportContract.View {

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_case_time)
    TextView tvCaseTime;
    @BindView(R.id.spinner_case_street)
    AppCompatSpinner spinnerCaseStreet;
    @BindView(R.id.spinner_case_community)
    AppCompatSpinner spinnerCaseCommunity;
    @BindView(R.id.spinner_case_grid)
    AppCompatSpinner spinnerCaseGrid;
    @BindView(R.id.smartRefresh)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.et_case_address)
    EditText etCaseAddress;
    @BindView(R.id.et_case_problem_description)
    EditText etCaseProblemDescription;
    @BindView(R.id.spinner_case_attribute)
    AppCompatSpinner spinnerCaseAttribute;
    @BindView(R.id.spinner_category_large)
    AppCompatSpinner spinnerCategoryLarge;
    @BindView(R.id.spinner_category_small)
    AppCompatSpinner spinnerCategorySmall;
    @BindView(R.id.spinner_category_sub)
    AppCompatSpinner spinnerCategorySub;
    @BindView(R.id.tv_location_longitude)
    TextView tvLocationLongitude;
    @BindView(R.id.tv_location_latitude)
    TextView tvLocationLatitude;
    @BindView(R.id.btn_location_obtain)
    TextView btnLocationObtain;
    @BindView(R.id.btn_next)
    TextView btnNext;
    @BindView(R.id.btn_cancel)
    TextView btnCancel;

    @BindDrawable(R.drawable.shape_date_picker_bg)
    Drawable popupWindowBg;

    private int entry_type;

    private MessageHandler handler;
    private LocationParam locParam = null;//移动定位
    private SecurityLogin mClient;

    private CustomPopupWindow mTimePickerPopupWindow;//时间选择弹出框

    private TimePickerListener mTimePickerListener;

    private Street mStreet;//占位数据
    private Grid mGrid;//占位数据

    private List<Street> mStreetList;//街道列表
    private MySpinnerAdapter<Street> mStreetAdapter;//街道
    private List<Street> mCommunityList;//社区列表
    private MySpinnerAdapter<Street> mCommunityAdapter;//社区
    private List<Grid> mGridList;//网格列表
    private MySpinnerAdapter<Grid> mGridAdapter;//网格


    private CaseAttribute mCaseAttribute;//占位数据
    private List<CaseAttribute> mCategoryLarge;//大类
    private MySpinnerAdapter<CaseAttribute> mCategoryLargeAdapter;//大类

    private MySpinnerAdapter<CaseAttribute> mCategorySmallAdapter;//小类
    private List<CaseAttribute> mCategorySmall;//小类

    private MySpinnerAdapter<CaseAttribute> mCategorySubAdapter;//子类
    private List<CaseAttribute> mCategorySub;//子类

    //相关参数全局变量
    private String mStreetId;
    private String mCommunityId;
    private String mGridId;
    private String mCaseAttributeId;
    private String mCasePrimaryCategory;
    private String mCaseSecondaryCategory;
    private String mCaseChildCategory;

    private double mLat = 0.123, mLng = 15.320;

    private RxPermissions rxPermissions;

    private LoadingProgressDialog loadingDialog;

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
        if (mTimePickerPopupWindow != null && mTimePickerPopupWindow.isShowing()) {
            mTimePickerPopupWindow.dismiss();
            mTimePickerPopupWindow = null;
        }
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        handler = null;
        locParam = null;
    }


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerReportComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .reportModule(new ReportModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_report; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    private interface TimePickerListener {
        void updateTime(String time);
    }

    @Override
    public void finishRefresh() {
        if (refreshLayout != null)
            refreshLayout.finishRefresh();
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        entry_type = getIntent().getIntExtra("entry_type", 0);
        tvToolbarTitle.setText(entry_type == 0 ? "自行处理" : "非自行处理");

        handler = new MessageHandler();
        initLocation();

        initRefreshLayout();

        if (mPresenter != null) {
            mPresenter.findAllStreetCommunity();
        }

        //更新案发时间
        mTimePickerListener = time -> tvCaseTime.setText(time);

        initTimePopupWindow();

        mStreet = new Street();
        mStreet.setName("请选择");

        mGrid = new Grid();
        mGrid.setName("请选择");

        mCaseAttribute = new CaseAttribute();
        mCaseAttribute.setText("请选择");

        initSpinnerStreetGrid();
        initSpinnerCaseAttribute();
        initSpinnerCategory();

    }

    private void initRefreshLayout() {
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            if (mPresenter != null)
                mPresenter.findAllStreetCommunity();
        });
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
        if (rxPermissions == null) {
            rxPermissions = new RxPermissions(this);
        }
        rxPermissions.requestEach(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            startLocation();
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
//                            Log.d(TAG, permission.name + " is denied. More info should be provided.");
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
//                            Log.d(TAG, permission.name + " is denied.");
                            showPermissionsDialog();

                        }
                    }
                });
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
                Timber.d("=====location: " + loc.getCode() + "  " + loc.getErrorCode() + "  " + loc.getErrRange() + " " + loc.getsdkErrCode());
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * 初始化时间选择弹出框
     */
    private void initTimePopupWindow() {
        tvCaseTime.setText(DateUtils.getDateToStringNonSecond(DateUtils.getCurrentTimeMillis(), DateUtils.dateString1));

        View timePickerView = View.inflate(this, R.layout.view_pop_time_picker, null);

        mTimePickerPopupWindow = CustomPopupWindow.builder()
                .parentView(tvCaseTime).contentView(timePickerView)
                .isOutsideTouch(false)
                .isWrap(false)
                .customListener(contentView -> {
                    String[] mYear = new String[1];
                    String[] mMonth = new String[1];
                    String[] mDay = new String[1];
                    String[] mHour = new String[1];
                    String[] mMinute = new String[1];
                    mYear[0] = String.valueOf(DateUtils.getCurrentYear());
                    mMonth[0] = String.valueOf(DateUtils.getCurrentMonth());
                    mDay[0] = String.valueOf(DateUtils.getCurrentDate());
                    mHour[0] = String.valueOf(DateUtils.getCurrentDateHour());
                    mMinute[0] = String.valueOf(DateUtils.getCurrentDateMinute());

                    TextView tvCurrentDate = contentView.findViewById(R.id.tv_current_time);
                    tvCurrentDate.setText(DateUtils.getDateToStringNonSecond(DateUtils.getCurrentTimeMillis(), DateUtils.dateString1));

                    DatePicker datePicker = contentView.findViewById(R.id.datePicker);
                    datePicker.setMinDate(DateUtils.getCurrentYearTimeStamp());
                    datePicker.updateDate(Integer.parseInt(mYear[0]), Integer.parseInt(mMonth[0]), Integer.parseInt(mDay[0]));
                    datePicker.init(DateUtils.getCurrentYear(), DateUtils.getCurrentMonth(),
                            DateUtils.getCurrentDate(), (view, year, monthOfYear, dayOfMonth) -> {
                                mYear[0] = String.valueOf(year);
                                mMonth[0] = monthOfYear < 9 ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);
                                mDay[0] = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);

                                tvCurrentDate.setText(MessageFormat.format("{0}-{1}-{2} {3}:{4}",
                                        mYear[0], mMonth[0], mDay[0], mHour[0], mMinute[0]));
                            }
                    );

                    TimePicker timePicker = contentView.findViewById(R.id.timePicker);
                    timePicker.setIs24HourView(true);
                    timePicker.setCurrentHour(Integer.parseInt(mHour[0]));
                    timePicker.setCurrentMinute(Integer.parseInt(mMinute[0]));
                    timePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> {
                        mHour[0] = hourOfDay < 10 ? "0" + hourOfDay : String.valueOf(hourOfDay);
                        mMinute[0] = minute < 10 ? "0" + minute : String.valueOf(minute);

                        tvCurrentDate.setText(MessageFormat.format("{0}-{1}-{2} {3}:{4}", mYear[0], mMonth[0], mDay[0], mHour[0], mMinute[0]));
                    });

                    TextView tvSure = contentView.findViewById(R.id.tv_sure);
                    TextView tvToday = contentView.findViewById(R.id.tv_today);
                    TextView tvCancel = contentView.findViewById(R.id.tv_cancel);

                    tvSure.setOnClickListener(v -> {
                        if (mTimePickerListener != null) {
                            mTimePickerListener.updateTime(tvCurrentDate.getText().toString());
                        }
                        mTimePickerPopupWindow.dismiss();
                    });

                    tvToday.setOnClickListener(v ->
                            datePicker.updateDate(DateUtils.getCurrentYear(), DateUtils.getCurrentMonth(), DateUtils.getCurrentDate()));

                    tvCancel.setOnClickListener(v -> mTimePickerPopupWindow.dismiss());

                })
                .isWrap(true).build();
    }

    /**
     * 初始化案件属性Spinner
     */
    private void initSpinnerCaseAttribute() {
        String[] caseAttributeArray = getResources().getStringArray(R.array.array_case_attribute);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, R.layout.spinner_list_item, caseAttributeArray);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_select_singlechoice);
        spinnerCaseAttribute.setAdapter(arrayAdapter);
        spinnerCaseAttribute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Timber.d("=======position" + position);
                mCaseAttributeId = String.valueOf(position);

                spinnerCategoryLarge.setSelection(0);
                mCategoryLarge.clear();
                mCategoryLarge.add(mCaseAttribute);
                mCategoryLargeAdapter.notifyDataSetChanged();

                spinnerCategorySmall.setSelection(0);
                mCategorySmall.clear();
                mCategorySmall.add(mCaseAttribute);
                mCategorySmallAdapter.notifyDataSetChanged();

                spinnerCategorySub.setSelection(0);
                mCategorySub.clear();
                mCategorySub.add(mCaseAttribute);
                mCategorySubAdapter.notifyDataSetChanged();

                if (position != 0 && mPresenter != null) {
                    mPresenter.findCaseCategoryListByAttribute(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 初始化街道社区网格Spinner
     */
    private void initSpinnerStreetGrid() {
        mStreetList = new ArrayList<>();
        mStreetList.add(mStreet);
        mStreetAdapter = new MySpinnerAdapter<>(this, mStreetList);
        spinnerCaseStreet.setAdapter(mStreetAdapter);
        spinnerCaseStreet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                spinnerCaseCommunity.setSelection(0);
                mCommunityList.clear();
                mCommunityList.add(mStreet);
                if (position > 0) {
                    mStreetId = mStreetList.get(position).getId();
                    mCommunityList.addAll(mStreetList.get(position).getChildList());
                } else {
                    mStreetId = "";
                }
                mCommunityAdapter.notifyDataSetChanged();

                spinnerCaseGrid.setSelection(0);
                mGridList.clear();
                mGridList.add(mGrid);
                mGridAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mCommunityList = new ArrayList<>();
        mCommunityList.add(mStreet);
        mCommunityAdapter = new MySpinnerAdapter<>(this, mCommunityList);
        spinnerCaseCommunity.setAdapter(mCommunityAdapter);
        spinnerCaseCommunity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerCaseGrid.setSelection(0);
                mGridList.clear();
                mGridList.add(mGrid);
                mGridAdapter.notifyDataSetChanged();
                if (position > 0) {
                    mCommunityId = mCommunityList.get(position).getId();
                    if (mPresenter != null) {
                        mPresenter.findGridListByCommunityId(mCommunityList.get(position).getId());
                    }
                } else {
                    mCommunityId = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mGridList = new ArrayList<>();
        mGridList.add(mGrid);
        mGridAdapter = new MySpinnerAdapter<>(this, mGridList);
        spinnerCaseGrid.setAdapter(mGridAdapter);
        spinnerCaseGrid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    mGridId = mGridList.get(position).getId();
                } else {
                    mGridId = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 初始化案件属性Spinner
     */
    private void initSpinnerCategory() {
        mCategoryLarge = new ArrayList<>();
        mCategoryLarge.add(mCaseAttribute);
        mCategoryLargeAdapter = new MySpinnerAdapter<>(this, mCategoryLarge);
        spinnerCategoryLarge.setAdapter(mCategoryLargeAdapter);
        spinnerCategoryLarge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerCategorySmall.setSelection(0);
                mCategorySmall.clear();
                mCategorySmall.add(mCaseAttribute);
                if (position > 0) {
                    mCasePrimaryCategory = mCategoryLarge.get(position).getCategoryId();
                    mCategorySmall.addAll(mCategoryLarge.get(position).getChildList());
                } else {
                    mCasePrimaryCategory = "";
                }
                mCategorySmallAdapter.notifyDataSetChanged();

                spinnerCategorySub.setSelection(0);
                mCategorySub.clear();
                mCategorySub.add(mCaseAttribute);
                mCategorySubAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mCategorySmall = new ArrayList<>();
        mCategorySmall.add(mCaseAttribute);
        mCategorySmallAdapter = new MySpinnerAdapter<>(this, mCategorySmall);
        spinnerCategorySmall.setAdapter(mCategorySmallAdapter);
        spinnerCategorySmall.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerCategorySub.setSelection(0);
                mCategorySub.clear();
                mCategorySub.add(mCaseAttribute);
                if (position > 0) {
                    mCaseSecondaryCategory = mCategorySmall.get(position).getCategoryId();
                    mCategorySub.addAll(mCategorySmall.get(position).getChildList());
                } else {
                    mCaseSecondaryCategory = "";
                }
                mCategorySubAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mCategorySub = new ArrayList<>();
        mCategorySub.add(mCaseAttribute);
        mCategorySubAdapter = new MySpinnerAdapter<>(this, mCategorySub);
        spinnerCategorySub.setAdapter(mCategorySubAdapter);
        spinnerCategorySub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    mCaseChildCategory = mCategorySub.get(position).getCategoryId();
                } else {
                    mCaseChildCategory = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
        startActivityForResult(intent, 1);
//        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @OnClick({R.id.tv_case_time, R.id.btn_location_obtain, R.id.btn_next, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_case_time://选择时间
                mTimePickerPopupWindow.show();
                break;
            case R.id.btn_location_obtain://获取坐标
                checkPermissionAndAction();
                break;
            case R.id.btn_next://下一步
                String caseTime = tvCaseTime.getText().toString();
                String address = etCaseAddress.getText().toString();
                String description = etCaseProblemDescription.getText().toString();
                if (checkParams(caseTime, address, description) && mPresenter != null) {

                    String handleType = "1";//直接处理传1 ，非直接处理传2
                    String whenType = "1";//直接处理( 整改前的写1  整改后写2),  非直接处理 whenType 1
                    String caseProcessRecordID = "19";// 直接处理 caseProcessRecordID  19,  非直接处理 caseProcessRecordID  11
                    if (entry_type == 0) {
                        //自行处理
                        handleType = "1";
                        whenType = "1";
                        caseProcessRecordID = "19";
                    } else if (entry_type == 1) {
                        //非自行处理
                        handleType = "2";
                        whenType = "1";
                        caseProcessRecordID = "11";
                    }

                    mPresenter.addOrUpdateCaseInfo(caseTime, mStreetId, mCommunityId, mGridId,
                            String.valueOf(mLat), String.valueOf(mLng), "17", address, description,
                            mCaseAttributeId, mCasePrimaryCategory, mCaseSecondaryCategory,
                            mCaseChildCategory, handleType, whenType, caseProcessRecordID);
                }
                break;
            case R.id.btn_cancel://取消
                killMyself();
                break;
        }
    }

    private boolean checkParams(String caseTime, String address, String description) {
        if (TextUtils.isEmpty(caseTime)) {
            showMessage("请选择案发时间");
            return false;
        } else if (TextUtils.isEmpty(mStreetId)) {
            showMessage("请选择案件所属街道");
            return false;
        } else if (TextUtils.isEmpty(mCommunityId)) {
            showMessage("请选择案件所属社区");
            return false;
        } else if (TextUtils.isEmpty(mGridId)) {
            showMessage("请选择案件所属网格");
            return false;
        } else if (mLat == 0 || mLng == 0) {
            showMessage("请定位案件位置");
            return false;
        } else if (TextUtils.isEmpty(address)) {
            showMessage("请输入案件地址");
            return false;
        } else if (TextUtils.isEmpty(description)) {
            showMessage("请输入问题描述");
            return false;
        } else if (TextUtils.isEmpty(mCaseAttributeId)) {
            showMessage("请选择案件属性");
            return false;
        } else if (TextUtils.isEmpty(mCasePrimaryCategory)) {
            showMessage("请选择案件大类");
            return false;
        } else if (TextUtils.isEmpty(mCaseSecondaryCategory)) {
            showMessage("请选择案件小类");
            return false;
        } else if (TextUtils.isEmpty(mCaseChildCategory)) {
            showMessage("请选择案件子类");
            return false;
        } else {
            return true;
        }
    }

    /**
     * 设置案件属性列表
     *
     * @param attributeList 案件属性列表
     */
    @Override
    public void setCaseAttributeList(List<CaseAttribute> attributeList) {
        mCategoryLarge.clear();
        mCategoryLarge.add(mCaseAttribute);
        mCategoryLarge.addAll(attributeList);
        mCategoryLargeAdapter.notifyDataSetChanged();
    }

    /**
     * 设置所有社区街道列表
     *
     * @param list 社区街道列表
     */
    @Override
    public void setAllStreetCommunity(List<Street> list) {
        mStreetList.clear();
        mStreetList.add(mStreet);
        mStreetList.addAll(list.get(0).getChildList());
        mStreetAdapter.notifyDataSetChanged();
    }

    /**
     * 设置所有社区网格列表
     *
     * @param list 网格列表
     */
    @Override
    public void setGridList(List<Grid> list) {
        mGridList.clear();
        mGridList.add(mGrid);
        mGridList.addAll(list);
        mGridAdapter.notifyDataSetChanged();
    }

    @SuppressLint("HandlerLeak")
    private class MessageHandler extends Handler {
        public MessageHandler() {
            super();
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x1233) {
                mClient.pause();
                if (mLat == 0 || mLng == 0) {
                    new AlertDialog.Builder(ReportActivity.this)
                            .setTitle("手机定位失败")
                            .setMessage("手机定位失败,获取中心点坐标")
                            .setPositiveButton("确定", (dialog, which) -> {
                                launchActivity(new Intent(ReportActivity.this, MapActivity.class));
                                dialog.dismiss();
                            }).create().show();
                }
                showMessage("经纬度: " + mLat + " " + mLng);
            }
            super.handleMessage(msg);
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
                PermissionUtils.permissionSkipSetting(ReportActivity.this);
            }
        });
        builder.show();
    }

    @Subscriber(tag = "location")
    private void receivedLocation(LocationEvent event) {
        mLat = event.getLat();
        mLng = event.getLng();

        tvLocationLatitude.setText(String.valueOf(event.getLat()));
        tvLocationLongitude.setText(String.valueOf(event.getLng()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            finish();
        }
    }
}
