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
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.cmcc.api.fpp.bean.CmccLocation;
import com.cmcc.api.fpp.bean.LocationParam;
import com.cmcc.api.fpp.login.SecurityLogin;
import com.geek.kaijo.R;
import com.geek.kaijo.Utils.DateUtils;
import com.geek.kaijo.Utils.FileSizeUtil;
import com.geek.kaijo.Utils.PermissionUtils;
import com.geek.kaijo.app.Constant;
import com.geek.kaijo.di.component.DaggerReportComponent;
import com.geek.kaijo.di.module.ReportModule;
import com.geek.kaijo.mvp.contract.ReportContract;
import com.geek.kaijo.mvp.model.entity.CaseAttribute;
import com.geek.kaijo.mvp.model.entity.CaseInfo;
import com.geek.kaijo.mvp.model.entity.Grid;
import com.geek.kaijo.mvp.model.entity.Street;
import com.geek.kaijo.mvp.model.entity.UploadCaseFile;
import com.geek.kaijo.mvp.model.entity.UploadFile;
import com.geek.kaijo.mvp.model.event.LocationEvent;
import com.geek.kaijo.mvp.presenter.ReportPresenter;
import com.geek.kaijo.mvp.ui.adapter.MySpinnerAdapter;
import com.geek.kaijo.mvp.ui.adapter.UploadPhotoAdapter;
import com.geek.kaijo.mvp.ui.adapter.UploadVideoAdapter;
import com.geek.kaijo.view.LoadingProgressDialog;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.widget.CustomPopupWindow;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
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

    private double mLat = 41.072847, mLng = 122.827825;

    private RxPermissions rxPermissions;

    private LoadingProgressDialog loadingDialog;

    private int isWhich = 0;//1 上传图片  2 上传视频
    private List<UploadFile> uploadVideoList;
    private List<UploadFile> uploadPhotoList;
    private UploadPhotoAdapter adapter1;
    private UploadVideoAdapter adapterVideo;
    private List<UploadCaseFile> caseFileList;

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
        tvToolbarTitle.setText(entry_type == 0 ? "自行处理" : "案件上报");

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

        handler = new MessageHandler();
        initLocation();

        initRefreshLayout();

        if (mPresenter != null) {
            mPresenter.findAllStreetCommunity(0);
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
        raPictureList.setLayoutManager(new LinearLayoutManager(this));
        raPictureList.setHasFixedSize(true);

        uploadPhotoList = new ArrayList<>();

        //视频列表
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        LinearLayoutManager layoutManager_video_later = new LinearLayoutManager(this);
        raVideoList.addItemDecoration(divider);
        raVideoList.setLayoutManager(layoutManager_video_later);

        uploadVideoList = new ArrayList<>();

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
                .minimumCompressSize(100)
                .glideOverride(200, 200)
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
        //同时申请多个权限
        rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(granted -> {
            if (granted) {           // All requested permissions are granted
                startLocation();
            } else {
                showPermissionsDialog();
            }
        });
    }


    private void startLocation() {
        new Thread(() -> {
            Message msg = Message.obtain();
            msg.what = 0x1233;
            try {
                CmccLocation loc = mClient.locCapability();
//                mLat = loc.getLatitude();
//                mLng = loc.getLongitude();
                if (handler != null)
                    handler.sendMessage(msg);
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
                    mCaseChildCategory = "1";
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
                        mPresenter.addOrUpdateCaseInfo(caseTime, mStreetId, mCommunityId, mGridId,
                                String.valueOf(mLat), String.valueOf(mLng), "17", address, description,
                                mCaseAttributeId, mCasePrimaryCategory, mCaseSecondaryCategory,
                                mCaseChildCategory, handleType, whenType, caseProcessRecordID);
                    }
                } else if (type.equals(submit)) {//提交

                    caseFileList = new ArrayList<>();
                    if (uploadPhotoList != null) { //照片整改前
                        for (int i = 0; i < uploadPhotoList.size(); i++) {
                            UploadCaseFile caseFile = new UploadCaseFile();
                            caseFile.setCaseId(Integer.valueOf(mCaseAttributeId));
                            caseFile.setUrl(uploadPhotoList.get(i).getFileRelativePath());
                            caseFile.setCaseProcessRecordId(Integer.valueOf(caseProcessRecordID));
                            caseFile.setFileType(0); //照片
                            caseFile.setWhenType(1); //整改前
                            caseFile.setHandleType(2);
                            caseFileList.add(caseFile);
                        }
                    }
                    if (uploadVideoList != null) { //照片整改前
                        for (int i = 0; i < uploadVideoList.size(); i++) {
                            UploadCaseFile caseFile = new UploadCaseFile();
                            caseFile.setCaseId(Integer.valueOf(mCaseAttributeId));
                            caseFile.setUrl(uploadVideoList.get(i).getFileRelativePath());
                            caseFile.setCaseProcessRecordId(Integer.valueOf(caseProcessRecordID));
                            caseFile.setFileType(1); //视频
                            caseFile.setWhenType(1);
                            caseFile.setHandleType(2);
                            caseFileList.add(caseFile);
                        }
                    }
                    if (mPresenter != null) {
                        mPresenter.addCaseAttach(caseFileList);
                        if (checkParams(caseTime, address, description) && mPresenter != null) {
                            mPresenter.addOrUpdateCaseInfo(caseTime, mStreetId, mCommunityId, mGridId,
                                    String.valueOf(mLat), String.valueOf(mLng), "17", address, description,
                                    mCaseAttributeId, mCasePrimaryCategory, mCaseSecondaryCategory,
                                    mCaseChildCategory, handleType, whenType, caseProcessRecordID);
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


                tvLocationLatitude.setText(String.valueOf(mLat));
                tvLocationLongitude.setText(String.valueOf(mLng));

//                if (mLat == 0 || mLng == 0) {
//                    launchActivity(new Intent(ReportActivity.this, MapActivity.class));
//                } else {
                    Intent intent = new Intent(ReportActivity.this, MapActivity.class);
                    intent.putExtra("lat", mLat);
                    intent.putExtra("lng", mLng);
                    launchActivity(intent);
//                }
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

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    for (LocalMedia media : selectList) {
                        // 1.media.getPath(); 为原图path
                        // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                        // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                        String compressedPath = media.getPath();
                        Timber.d("视频地址：==" + compressedPath);
                        System.out.print("视频地址：==" + compressedPath);

                        switch (isWhich) {
                            case 1:
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
                                UploadFile uploadFile5 = new UploadFile();
                                uploadFile5.setFileName(compressedPath);
                                String size5 = FileSizeUtil.getAutoFileOrFilesSize(uploadFile5.getFileName());
                                uploadFile5.setFileSize(size5);
                                uploadVideoList.add(uploadFile5);
                                recyclerViewAdapter_video();
                                if (mPresenter != null) {
                                    mPresenter.uploadFile(compressedPath);
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
                if (uploadPhotoList.get(position).getIsSuccess() == 1) { //上传成功  显示删除
                    uploadPhotoList.remove(position);
                    adapter1.notifyDataSetChanged();
                } else if (uploadPhotoList.get(position).getIsSuccess() == 0) {

                } else { //上传失败 重新上传
                    if (mPresenter != null) {
                        mPresenter.uploadFile(uploadPhotoList.get(position).getFileName());
                    }
                }

            }
        });
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
                if (uploadVideoList.get(position).getIsSuccess() == 1) { //上传成功  显示删除
                    uploadVideoList.remove(position);
                    adapterVideo.notifyDataSetChanged();
                } else if (uploadVideoList.get(position).getIsSuccess() == 0) {

                } else { //上传失败 重新上传
                    if (mPresenter != null) {
                        mPresenter.uploadFile(uploadVideoList.get(position).getFileName());
                    }
                }
            }
        });
    }

    @Override
    public void uploadSuccess(UploadFile uploadPhoto) {
        if (uploadPhoto != null) {
            switch (isWhich) {
                case 1:
                    for (int i = 0; i < uploadPhotoList.size(); i++) {
                        if (uploadPhotoList.get(i).getFileName().equals(uploadPhoto.getFileName())) {
                            uploadPhotoList.get(i).setFileDomain(uploadPhoto.getFileDomain());
                            uploadPhotoList.get(i).setFileRelativePath(uploadPhoto.getFileRelativePath());
                            uploadPhotoList.get(i).setIsSuccess(uploadPhoto.getIsSuccess());
                            adapter1.notifyItemChanged(i);
                        }
                    }
                    break;
                case 2:
                    for (int i = 0; i < uploadVideoList.size(); i++) {
                        if (uploadVideoList.get(i).getFileName().equals(uploadPhoto.getFileName())) {
                            uploadVideoList.get(i).setFileDomain(uploadPhoto.getFileDomain());
                            uploadVideoList.get(i).setFileRelativePath(uploadPhoto.getFileRelativePath());
                            uploadVideoList.get(i).setIsSuccess(uploadPhoto.getIsSuccess());
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
                Intent intent = new Intent(this, UploadActivity.class);
                intent.putExtra("caseInfo", caseInfoEntity);
                launchActivity(intent);
                break;
            case 1:
                if (mPresenter != null) {
                    mPresenter.addCaseAttach(caseFileList);
                }
                break;
            default:
                break;
        }
    }
}
