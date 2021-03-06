package com.geek.kaijo.mvp.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cmmap.api.location.CmccLocation;
import com.cmmap.api.location.CmccLocationClient;
import com.cmmap.api.location.CmccLocationClientOption;
import com.cmmap.api.location.CmccLocationListener;
import com.geek.kaijo.R;
import com.geek.kaijo.Utils.DateUtils;
import com.geek.kaijo.Utils.FileSizeUtil;
import com.geek.kaijo.Utils.GPSUtils;
import com.geek.kaijo.Utils.PermissionUtils;
import com.geek.kaijo.app.Constant;
import com.geek.kaijo.app.MyApplication;
import com.geek.kaijo.app.service.LocalService;
import com.geek.kaijo.di.component.DaggerReportComponent;
import com.geek.kaijo.di.module.ReportModule;
import com.geek.kaijo.mvp.contract.ReportContract;
import com.geek.kaijo.mvp.model.entity.CaseAttribute;
import com.geek.kaijo.mvp.model.entity.CaseInfo;
import com.geek.kaijo.mvp.model.entity.Grid;
import com.geek.kaijo.mvp.model.entity.Street;
import com.geek.kaijo.mvp.model.entity.UploadCaseFile;
import com.geek.kaijo.mvp.model.entity.UploadFile;
import com.geek.kaijo.mvp.model.entity.UserInfo;
import com.geek.kaijo.mvp.model.event.LocationEvent;
import com.geek.kaijo.mvp.presenter.ReportPresenter;
import com.geek.kaijo.mvp.ui.adapter.MySpinnerAdapter;
import com.geek.kaijo.mvp.ui.adapter.UploadPhotoAdapter;
import com.geek.kaijo.mvp.ui.adapter.UploadVideoAdapter;
import com.geek.kaijo.view.LoadingProgressDialog;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.DataHelper;
import com.jess.arms.utils.LogUtils;
import com.jess.arms.widget.CustomPopupWindow;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.simple.eventbus.Subscriber;
import org.xml.sax.SAXException;

import java.lang.ref.WeakReference;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.OnClick;
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
    @BindView(R.id.ra_pic)
    TextView raPic;
    @BindView(R.id.ra_picture_list)
    RecyclerView raPictureList;
    @BindView(R.id.ra_video)
    TextView raVideo;
    @BindView(R.id.ra_video_list)
    RecyclerView raVideoList;
    @BindView(R.id.rp_bottom_layout)
    LinearLayout raBottomLayout;

    @BindView(R.id.tv_photo_list)
    TextView tv_photo_list;
    @BindView(R.id.tv_video_list)
    TextView tv_video_list;

    private int entry_type;

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

    private double mLat, mLng;

    private RxPermissions rxPermissions;

    private LoadingProgressDialog loadingDialog;

    private int isWhich = 0;//1 上传图片  2 上传视频
    private List<UploadFile> uploadVideoList;
    private List<UploadFile> uploadPhotoList;
    private UploadPhotoAdapter adapter1;
    private UploadVideoAdapter adapterVideo;
    private List<UploadCaseFile> caseFileList;
    private UserInfo userInfo;

    private List<Street> streetList;
    private CaseInfo caseInfo;
    private MyHandler myHandler;


    @Override
    protected void onDestroy() {
        if (mTimePickerPopupWindow != null && mTimePickerPopupWindow.isShowing()) {
            mTimePickerPopupWindow.dismiss();
            mTimePickerPopupWindow = null;
        }
        super.onDestroy();
        GPSUtils.getInstance().removeLocationListener(locationListener);
        if (myHandler != null) {
            myHandler.removeCallbacksAndMessages(null);
        }
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
        tvToolbarTitle.setText(entry_type == 0 ? "自行处理" : "案件上报");
        userInfo = DataHelper.getDeviceData(this, Constant.SP_KEY_USER_INFO);
        myHandler = new MyHandler(this);

        switch (entry_type) {
            case 0:
                raBottomLayout.setVisibility(View.GONE);
                btnNext.setText(getResources().getString(R.string.btn_next_step));
                break;
            case 1:
                raBottomLayout.setVisibility(View.VISIBLE);
                btnNext.setText(getResources().getString(R.string.btn_submit));
                break;
            default:
                break;
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

        //照片列表
        DividerItemDecoration divider_photo = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL); //分割线
        raPictureList.addItemDecoration(divider_photo);
        raPictureList.setLayoutManager(new LinearLayoutManager(this));

        uploadPhotoList = new ArrayList<>();

        //视频列表
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        LinearLayoutManager layoutManager_video_later = new LinearLayoutManager(this);
        raVideoList.addItemDecoration(divider);
        raVideoList.setLayoutManager(layoutManager_video_later);

        uploadVideoList = new ArrayList<>();

        initRefreshLayout();

        caseInfo = (CaseInfo) getIntent().getSerializableExtra("caseInfo");   //从暂存跳转
        if (caseInfo != null) {
            etCaseAddress.setText(caseInfo.getAddress());
            etCaseProblemDescription.setText(caseInfo.getDescription());
            if(!TextUtils.isEmpty(caseInfo.getLat()) && !TextUtils.isEmpty(caseInfo.getLng())){
                mLat = Double.parseDouble(caseInfo.getLat());
                mLng = Double.parseDouble(caseInfo.getLng());
            }
            tvLocationLongitude.setText(mLng + "");
            tvLocationLatitude.setText(mLat + "");

            if (caseInfo.getCaseAttribute().equals("1")) { //事件
                spinnerCaseAttribute.setSelection(2, true);
            } else if (caseInfo.getCaseAttribute().equals("2")) { //部件
                spinnerCaseAttribute.setSelection(1, true);
            }
        }

        if (mPresenter != null) {
            mPresenter.findAllStreetCommunity(0);
        }

    }


    /**
     * 头像选择 PictureSelector
     */
    private void pictureSelector() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .imageSpanCount(4)
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(false)
                .isCamera(true)
                .enableCrop(false)
                .compress(true)
                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .minimumCompressSize(100)
                .withAspectRatio(1, 1)
                .showCropFrame(true)
                .rotateEnabled(true)
                .isDragFrame(true)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    /**
     * 视频选择
     */
    private void videoSelector() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofVideo())
                .selectionMode(PictureConfig.SINGLE)
                .previewVideo(true)
                .compress(true)
                .videoQuality(0)
                .videoMaxSecond(60)
                .videoMinSecond(1)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    private void initRefreshLayout() {
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            if (mPresenter != null)
                mPresenter.findAllStreetCommunity(0);
        });
    }

    @SuppressLint("CheckResult")
    private void checkPermissionAndAction() {
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
            if (granted) {
                boolean isOpen = GPSUtils.isOPen(this);//判断GPS是否打开
                if (!isOpen) {
                    GPSUtils.showGPSDialog(this);
                    return;
                }
                //启动定位
                showLoading();
                GPSUtils.getInstance().setOnLocationListener(locationListener);
                GPSUtils.getInstance().startLocation();
                myHandler.sendEmptyMessageDelayed(1,Constant.location_loadTime);
            } else {
                showPermissionsDialog();
            }
        });
    }

    private GPSUtils.LocationListener locationListener = new GPSUtils.LocationListener() {
        @Override
        public void onLocationChanged(CmccLocation cmccLocation) {
            if(ReportActivity.this.isFinishing())return;
            if(myHandler!=null){
                myHandler.removeMessages(1);
            }
            hideLoading();
            if(cmccLocation!=null){
                mLat = cmccLocation.getLatitude();
                mLng = cmccLocation.getLongitude();
                if(mLat>0 &&mLng>0){
                    tvLocationLatitude.setText(String.valueOf(mLat));
                    tvLocationLongitude.setText(String.valueOf(mLng));
                    Intent intent = new Intent(ReportActivity.this, MapActivity.class);
                    intent.putExtra("lat", mLat);
                    intent.putExtra("lng", mLng);
                    ReportActivity.this.startActivityForResult(intent, Constant.MAP_REQUEST_CODE);
                }else {
                    showNormalDialog();
                }
            }else {
                showNormalDialog();
            }
            GPSUtils.getInstance().removeLocationListener(locationListener);
        }
    };


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
                if (position == 1) {
                    mCaseAttributeId = "2"; //部件2
                } else if (position == 2) {
                    mCaseAttributeId = "1"; //事件1
                } else {
                    mCaseAttributeId = "0";
                }
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
                    mPresenter.findCaseCategoryListByAttribute(Integer.parseInt(mCaseAttributeId));
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
            public void onItemSelected(AdapterView<?> parent, View view, int position, long checkId) {

                spinnerCaseCommunity.setSelection(0);
                mCommunityList.clear();
                mCommunityList.add(mStreet);
                if (position > 0) {
                    mStreetId = mStreetList.get(position).getId();
                    mCommunityList.addAll(mStreetList.get(position).getChildList());

                    if (userInfo != null && !TextUtils.isEmpty(userInfo.getStreetName())) {
                        if (mCommunityList != null) {
                            if (caseInfo != null) { //从暂存跳转
                                String id = String.valueOf(caseInfo.getCommunityId());
                                for (int i = 0; i < mCommunityList.size(); i++) {
                                    if (id.equals(mCommunityList.get(i).getId())) {
                                        spinnerCaseCommunity.setSelection(i, true);
                                        break;
                                    }
                                }
                            } else {
                                for (int i = 0; i < mCommunityList.size(); i++) {
                                    if (userInfo.getCommunityName().equals(mCommunityList.get(i).getName())) {
                                        spinnerCaseCommunity.setSelection(i, true);
                                        break;
                                    }
                                }
                            }

                        }
                    }
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
            public void onItemSelected(AdapterView<?> parent, View view, int position, long checkId) {
                spinnerCategorySmall.setSelection(0);
                mCategorySmall.clear();
                mCategorySmall.add(mCaseAttribute);
                if (position > 0) {
                    mCasePrimaryCategory = mCategoryLarge.get(position).getCategoryId();
                    mCategorySmall.addAll(mCategoryLarge.get(position).getChildList());
                } else {
                    mCasePrimaryCategory = "";
                }

                if (caseInfo != null) { //从暂存跳转
                    String id = String.valueOf(caseInfo.getCaseSecondaryCategory());
                    for (int i = 0; i < mCategorySmall.size(); i++) {
                        if (id.equals(mCategorySmall.get(i).getCategoryId())) {
                            spinnerCategorySmall.setSelection(i, true);
                            break;
                        }
                    }
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
            public void onItemSelected(AdapterView<?> parent, View view, int position, long checkId) {
                spinnerCategorySub.setSelection(0);
                mCategorySub.clear();
                mCategorySub.add(mCaseAttribute);
                if (position > 0) {
                    mCaseSecondaryCategory = mCategorySmall.get(position).getCategoryId();
                    mCategorySub.addAll(mCategorySmall.get(position).getChildList());
                } else {
                    mCaseSecondaryCategory = "";
                }

                if (caseInfo != null) { //从暂存跳转
                    String id = String.valueOf(caseInfo.getCaseChildCategory());
                    for (int i = 0; i < mCategorySub.size(); i++) {
                        if (id.equals(mCategorySub.get(i).getCategoryId())) {
                            spinnerCategorySub.setSelection(i, true);
                            break;
                        }
                    }
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

    @OnClick({R.id.tv_case_time, R.id.btn_location_obtain, R.id.btn_next, R.id.btn_cancel, R.id.ra_pic, R.id.ra_video})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_case_time://选择时间
                mTimePickerPopupWindow.show();
                break;
            case R.id.ra_pic://上传图片
                isWhich = 1;
                pictureSelector();
                break;
            case R.id.ra_video://上传视频
                isWhich = 2;
                videoSelector();
                break;
            case R.id.btn_location_obtain://获取坐标
                checkPermissionAndAction();
                break;
            case R.id.btn_next://下一步
                String type = btnNext.getText().toString();
                String next = getResources().getString(R.string.btn_next_step);
                String submit = getResources().getString(R.string.btn_submit);

                String caseTime = tvCaseTime.getText().toString();
                String address = etCaseAddress.getText().toString();
                String description = etCaseProblemDescription.getText().toString();

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

                if (type.equals(next)) {//下一步
                    if (checkParams(caseTime, address, description) && mPresenter != null) {

                        if(caseInfo==null){
                            caseInfo = new CaseInfo();
                            caseInfo.setId(System.currentTimeMillis());
                        }
                        caseInfo.setUserId(userInfo.getUserId());
                        caseInfo.setAcceptDate(caseTime);
                        caseInfo.setStreetId(mStreetId);
                        caseInfo.setCommunityId(mCommunityId);
                        caseInfo.setGridId(mGridId);
                        caseInfo.setLat(String.valueOf(mLat));
                        caseInfo.setLng(String.valueOf(mLng));
                        caseInfo.setSource("17");
                        caseInfo.setAddress(address);
                        caseInfo.setDescription(description);
                        caseInfo.setCaseAttribute(mCaseAttributeId);
                        caseInfo.setCasePrimaryCategory(mCasePrimaryCategory);
                        caseInfo.setCaseSecondaryCategory(mCaseSecondaryCategory);
                        caseInfo.setCaseChildCategory(mCaseChildCategory);

                        Intent intent = new Intent(this, UploadActivity.class);
                        intent.putExtra("caseInfo", caseInfo);
                        startActivityForResult(intent,1003);

                    }
                } else if (type.equals(submit)) {//提交

                    if (mPresenter != null) {
                        List<UploadFile> uploadFileList = new ArrayList<>();
                        for(int i=0;i<uploadPhotoList.size();i++){
                            if(!TextUtils.isEmpty(uploadPhotoList.get(i).getUrl())){
                                uploadFileList.add(uploadPhotoList.get(i));
                            }
                        }
                        for(int i=0;i<uploadVideoList.size();i++){
                            if(!TextUtils.isEmpty(uploadVideoList.get(i).getUrl())){
                                uploadFileList.add(uploadVideoList.get(i));
                            }
                        }
                        if (checkParams(caseTime, address, description) && mPresenter != null && userInfo != null) {
                            mPresenter.addOrUpdateCaseInfo(userInfo.getUserId(), caseTime, mStreetId, mCommunityId, mGridId,
                                    String.valueOf(mLat), String.valueOf(mLng), "17", address, description,
                                    mCaseAttributeId, mCasePrimaryCategory, mCaseSecondaryCategory,
                                    mCaseChildCategory, handleType, whenType, caseProcessRecordID, uploadFileList);
                        }
                    }
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
        if (caseInfo != null) { //从暂存跳转
            String id = String.valueOf(caseInfo.getCasePrimaryCategory());
            for (int i = 0; i < mCategoryLarge.size(); i++) {
                if (id.equals(mCategoryLarge.get(i).getCategoryId())) {
                    spinnerCategoryLarge.setSelection(i, true);
                    break;
                }
            }
        }
        mCategoryLargeAdapter.notifyDataSetChanged();
    }

    /**
     * 设置所有社区街道列表
     *
     * @param list 社区街道列表
     */
    @Override
    public void setAllStreetCommunity(List<Street> list) {
        showStreet(list);
    }

    private void showStreet(List<Street> list) {
        mStreetList.clear();
        mStreetList.add(mStreet);
        mStreetList.addAll(list.get(0).getChildList());
        if (userInfo != null && !TextUtils.isEmpty(userInfo.getStreetName())) {
            if (mStreetList != null) {
                if (caseInfo != null) { //从暂存跳转
                    String id = String.valueOf(caseInfo.getStreetId());
                    for (int i = 0; i < mStreetList.size(); i++) {
                        if (id.equals(mStreetList.get(i).getId())) {
                            spinnerCaseStreet.setSelection(i, true);
                            break;
                        }
                    }
                } else {
                    for (int i = 0; i < mStreetList.size(); i++) {
                        if (userInfo.getStreetName().equals(mStreetList.get(i).getName())) {
                            spinnerCaseStreet.setSelection(i, true);
                            break;
                        }
                    }
                }

            }
        }
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
        if (userInfo != null && !TextUtils.isEmpty(userInfo.getStreetName())) {
            if (mGridList != null) {
                if (caseInfo != null) { //从暂存跳转
                    String id = String.valueOf(caseInfo.getGridId());
                    for (int i = 0; i < mGridList.size(); i++) {
                        if (id.equals(mGridList.get(i).getId())) {
                            spinnerCaseGrid.setSelection(i, true);
                            break;
                        }
                    }
                } else {
                    for (int i = 0; i < mGridList.size(); i++) {
                        if (userInfo.getGridName().equals(mGridList.get(i).getName())) {
                            spinnerCaseGrid.setSelection(i, true);
                            break;
                        }
                    }
                }

            }
        }
        mGridAdapter.notifyDataSetChanged();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1003 && resultCode == 1003) {
            setResult(1);
            finish();
        } else if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    for (LocalMedia media : selectList) {
                        // 1.media.getPath(); 为原图path
                        // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                        // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                        switch (isWhich) {
                            case 1:
                                String compressedPath = media.getCompressPath();
                                UploadFile uploadPhoto1 = new UploadFile();
                                uploadPhoto1.setFileName(compressedPath);
                                String size1 = FileSizeUtil.getAutoFileOrFilesSize(uploadPhoto1.getFileName());
                                uploadPhoto1.setFileSize(size1);
                                uploadPhotoList.add(uploadPhoto1);
                                recyclerViewAdapter1();
                                if (mPresenter != null) {
                                    mPresenter.uploadFile(compressedPath);
                                }
                                break;
                            case 2:
                                String path = media.getPath();
                                UploadFile uploadFile5 = new UploadFile();
                                uploadFile5.setFileName(path);
                                String size5 = FileSizeUtil.getAutoFileOrFilesSize(uploadFile5.getFileName());
                                uploadFile5.setFileSize(size5);
                                uploadVideoList.add(uploadFile5);
                                recyclerViewAdapter_video();
                                if (mPresenter != null) {
                                    mPresenter.uploadFile(path);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                    break;
                default:
                    break;
            }
        } else if (requestCode == Constant.MAP_REQUEST_CODE && resultCode == Constant.MAP_REQUEST_CODE) {
            if (data != null) {
                mLng = data.getDoubleExtra("lng", 0);
                mLat = data.getDoubleExtra("lat", 0);

                tvLocationLatitude.setText(String.valueOf(mLat));
                tvLocationLongitude.setText(String.valueOf(mLng));
            }
        }
    }


    //图片列表
    private void recyclerViewAdapter1() {
        if (adapter1 == null) {
            adapter1 = new UploadPhotoAdapter(this, uploadPhotoList);
            raPictureList.setAdapter(adapter1);
        } else {
            adapter1.notifyChanged(uploadPhotoList);
        }
        adapter1.setOnItemOnClilcklisten(new UploadPhotoAdapter.OnItemOnClicklisten() {
            @Override
            public void onItemDeleteClick(View v, int position) {
                uploadPhotoList.remove(position);
                adapter1.notifyDataSetChanged();
                if (adapter1.getItemCount() != 0) {
                    tv_photo_list.setVisibility(View.GONE);
                } else {
                    tv_photo_list.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onItemAgainUploadClick(View v, int position) {
                if (mPresenter != null) {
                    mPresenter.uploadFile(uploadPhotoList.get(position).getFileName());
                    isWhich = 1;
                }
            }
        });

        if (adapter1.getItemCount() != 0) {
            tv_photo_list.setVisibility(View.GONE);
        } else {
            tv_photo_list.setVisibility(View.VISIBLE);
        }
    }

    //视频
    private void recyclerViewAdapter_video() {
        if (adapterVideo == null) {
            adapterVideo = new UploadVideoAdapter(this, uploadVideoList);
            raVideoList.setAdapter(adapterVideo);
        } else {
            adapterVideo.notifyDataSetChanged();
        }
        adapterVideo.setOnItemOnClilcklisten(new UploadVideoAdapter.OnItemOnClicklisten() {
            @Override
            public void onItemDeleteClick(View v, int position) {
                uploadVideoList.remove(position);
                adapterVideo.notifyDataSetChanged();
                if (adapterVideo.getItemCount() != 0) {
                    tv_video_list.setVisibility(View.GONE);
                } else {
                    tv_video_list.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onItemAgainUploadClick(View v, int position) { //上传失败 重新上传
                if (mPresenter != null) {
                    mPresenter.uploadFile(uploadVideoList.get(position).getFileName());
                    isWhich = 2;
                }
            }
        });
        if (adapterVideo.getItemCount() != 0) {
            tv_video_list.setVisibility(View.GONE);
        } else {
            tv_video_list.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void uploadSuccess(UploadFile uploadPhoto) {
        if (uploadPhoto != null) {
            switch (isWhich) {
                case 1: //上传图片
                    for (int i = 0; i < uploadPhotoList.size(); i++) {
                        if (uploadPhotoList.get(i).getFileName().equals(uploadPhoto.getFileName())) {
                            uploadPhotoList.get(i).setFileDomain(uploadPhoto.getFileDomain());
                            uploadPhotoList.get(i).setFileRelativePath(uploadPhoto.getFileRelativePath());
                            uploadPhotoList.get(i).setIsSuccess(uploadPhoto.getIsSuccess());
                            if (entry_type == 0) {
                                //自行处理
                                uploadPhotoList.get(i).whenType = 1;
                            } else if (entry_type == 1) {
                                //非自行处理
                                uploadPhotoList.get(i).whenType = 1;
                            }
                            uploadPhotoList.get(i).fileType = 0;
                            adapter1.notifyItemChanged(i);
                        }
                    }
                    break;
                case 2: //上传视频
                    for (int i = 0; i < uploadVideoList.size(); i++) {
                        if (uploadVideoList.get(i).getFileName().equals(uploadPhoto.getFileName())) {
                            uploadVideoList.get(i).setFileDomain(uploadPhoto.getFileDomain());
                            uploadVideoList.get(i).setFileRelativePath(uploadPhoto.getFileRelativePath());
                            uploadVideoList.get(i).setIsSuccess(uploadPhoto.getIsSuccess());
                            uploadVideoList.get(i).whenType = 1;
                            uploadVideoList.get(i).fileType = 1;
                            adapterVideo.notifyItemChanged(i);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 案件上传成功
     *
     * @param caseInfoEntity
     */
    @Override
    public void uploadCaseInfoSuccess(CaseInfo caseInfoEntity) {
        switch (entry_type) {
            case 0:
                if (caseInfo != null) {
                    caseInfoEntity.setId(caseInfo.getId());
                    caseInfoEntity.setFileListGson(caseInfo.getFileListGson());
                    caseInfoEntity.setHandleResult(caseInfo.getHandleResult());
                }
                Intent intent = new Intent(this, UploadActivity.class);
                intent.putExtra("caseInfo", caseInfoEntity);
                launchActivity(intent);
                break;
            case 1:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void uploadPhotoError() {
        if(uploadPhotoList!=null){
            for (int i = 0; i < uploadPhotoList.size(); i++) {
                if (TextUtils.isEmpty(uploadPhotoList.get(i).getUrl())) {
                    uploadPhotoList.get(i).setIsSuccess(2);
                    adapter1.notifyItemChanged(i);
                }
            }
        }
        if(uploadVideoList!=null){
            for (int i = 0; i < uploadVideoList.size(); i++) {
                if (TextUtils.isEmpty(uploadVideoList.get(i).getUrl())) {
                    uploadVideoList.get(i).setIsSuccess(2);
                    adapterVideo.notifyItemChanged(i);
                }
            }
        }
    }

    private static class MyHandler extends Handler {
        private final WeakReference<ReportActivity> weakTrainModelActivity;

        public MyHandler(ReportActivity activity) {
            weakTrainModelActivity = new WeakReference<ReportActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ReportActivity weakReference;
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
                        Intent intent = new Intent(ReportActivity.this, MapActivity.class);
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
