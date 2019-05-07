//package com.geek.kaijo.mvp.ui.activity.society.safety;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v7.widget.AppCompatSpinner;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.TimePicker;
//
//import com.geek.kaijo.R;
//import com.geek.kaijo.Utils.DateUtils;
//import com.geek.kaijo.Utils.FileSizeUtil;
//import com.geek.kaijo.app.Constant;
//import com.geek.kaijo.app.api.RequestParamUtils;
//import com.geek.kaijo.di.component.DaggerSpecialCollectionComponent;
//import com.geek.kaijo.mvp.contract.SpecialCollectionContract;
//import com.geek.kaijo.mvp.model.entity.Equipment;
//import com.geek.kaijo.mvp.model.entity.GridItemContent;
//import com.geek.kaijo.mvp.model.entity.Street;
//import com.geek.kaijo.mvp.model.entity.UploadFile;
//import com.geek.kaijo.mvp.model.entity.UserInfo;
//import com.geek.kaijo.mvp.presenter.SpecialCollectionPresenter;
//import com.geek.kaijo.mvp.ui.activity.MapActivity;
//import com.geek.kaijo.mvp.ui.adapter.MySpinnerAdapter;
//import com.geek.kaijo.mvp.ui.adapter.UploadPhotoAdapter;
//import com.geek.kaijo.view.LoadingProgressDialog;
//import com.google.gson.Gson;
//import com.jess.arms.base.BaseActivity;
//import com.jess.arms.di.component.AppComponent;
//import com.jess.arms.utils.ArmsUtils;
//import com.jess.arms.utils.DataHelper;
//import com.jess.arms.widget.CustomPopupWindow;
//import com.luck.picture.lib.PictureSelector;
//import com.luck.picture.lib.config.PictureConfig;
//import com.luck.picture.lib.config.PictureMimeType;
//import com.luck.picture.lib.entity.LocalMedia;
//import com.scwang.smartrefresh.layout.SmartRefreshLayout;
//
//import java.text.MessageFormat;
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.OnClick;
//import okhttp3.RequestBody;
//
//import static com.jess.arms.utils.Preconditions.checkNotNull;
//
///**
// * 特种设备采集
// */
//public class EquipmentActivity extends BaseActivity<SpecialCollectionPresenter> implements SpecialCollectionContract.View {
//    private static final int phot_requestCode = 1011;  //上传图片
//    private static final int file_requestCode = 1012;  //上传检查记录
//
//    @BindView(R.id.tv_toolbar_title)
//    TextView tvToolbarTitle;
//    @BindView(R.id.spinner_street)
//    AppCompatSpinner spinner_street;//街道spinner
//    private List<Street> mStreetList;//街道列表
//    private MySpinnerAdapter<Street> mStreetAdapter;//街道adapter
//
//    @BindView(R.id.spinner_community)
//    AppCompatSpinner spinner_community;//社区spiner
//    private List<Street> mCommunityList;//社区列表
//    private MySpinnerAdapter<Street> mCommunityAdapter;//社区adapter
//    private UserInfo userInfo;
//
//    @BindView(R.id.btn_location_obtain)
//    TextView btn_location_obtain;
//
//
//    @BindView(R.id.tv_add_equipment)
//    TextView tv_add_equipment;
//    @BindView(R.id.ly_addView)
//    LinearLayout ly_addView;
//    @BindView(R.id.submit)
//    TextView submit;
//    @BindView(R.id.et_name)
//    EditText et_name;
//    @BindView(R.id.et_farenName)
//    EditText et_farenName;
//    @BindView(R.id.et_address)
//    EditText et_address;
//
//
//    @BindView(R.id.tv_location_longitude)
//    TextView tvLocationLongitude;
//    @BindView(R.id.tv_location_latitude)
//    TextView tvLocationLatitude;
//    @BindView(R.id.smartRefresh)
//    SmartRefreshLayout smartRefresh;
//
//    @BindView(R.id.include_tezhongshebei)
//    LinearLayout include_tezhongshebei;
//    @BindView(R.id.include_zaijiangongdi)
//    LinearLayout include_zaijiangongdi;
//
//
//    //上传图片
//    @BindView(R.id.upload_picture_list)
//    RecyclerView upload_picture_list;
//    private List<UploadFile> uploadPhotoList;
//    private UploadPhotoAdapter photoAdapter;
//
//    //上传检查记录
//    @BindView(R.id.check_record_list)
//    RecyclerView check_record_list;
//    private List<UploadFile> fileList;
//    private UploadPhotoAdapter fileAdapter;
//
//    private GridItemContent subMenu;
//    private double lat;
//    private double lng;
//
//    private List<String> checkList;
//    private LoadingProgressDialog loadingDialog;
//
//    @BindView(R.id.ly_parentView)
//    LinearLayout ly_parentView;
//
//
//    //在建工地
//    @BindView(R.id.gd_et_mark)
//    EditText gd_et_mark; //受理书标号
//    @BindView(R.id.gd_et_name)
//    EditText gd_et_name; //工程名称
//    @BindView(R.id.gd_et_address)
//    EditText gd_et_address; //工程地址
//    @BindView(R.id.gd_et_shigongdanwei)
//    EditText gd_et_shigongdanwei; //施工单位
//    @BindView(R.id.gd_spinner_check)
//    AppCompatSpinner gd_spinner_check; //是否有施工许可证
//    @BindView(R.id.gd_et_price)
//    EditText gd_et_price; //工程总造价
//    @BindView(R.id.gd_et_jingduzhuangtai)
//    EditText gd_et_jingduzhuangtai; //形象进度/状态
//    @BindView(R.id.gd_jianzhumianji)
//    EditText gd_jianzhumianji; //建筑面积（万平）
//    @BindView(R.id.gd_et_kaigongnriqi)
//    TextView gd_et_kaigongnriqi; //开工日期
//    @BindView(R.id.gd_et_jungongriqi)
//    TextView gd_et_jungongriqi; //竣工日期
//    @BindView(R.id.gd_et_jianshedanwei)
//    EditText gd_et_jianshedanwei; //建设单位
//    @BindView(R.id.gd_et_jianlidanwei)
//    EditText gd_et_jianlidanwei; //监理单位
//
////    private int isWhich = 0;//1 上传图片  2 上传视频
//    private CustomPopupWindow mTimePickerPopupWindow;//时间选择弹出框
//    private CustomPopupWindow mTimePickerPopupWindow_jungong;//时间选择弹出框
//    private TimePickerListener mTimePickerListener;
//
//    @Override
//    public void setupActivityComponent(@NonNull AppComponent appComponent) {
//        DaggerSpecialCollectionComponent //如找不到该类,请编译一下项目
//                .builder()
//                .appComponent(appComponent)
//                .view(this)
//                .build()
//                .inject(this);
//    }
//
//    @Override
//    public int initView(@Nullable Bundle savedInstanceState) {
//        return R.layout.activity_special_collection; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
//    }
//
//    @Override
//    public void initData(@Nullable Bundle savedInstanceState) {
//        subMenu = (GridItemContent) getIntent().getSerializableExtra("Submenu");
//        if (subMenu != null) {
//            tvToolbarTitle.setText(subMenu.getName() + "采集");
//            if("特种设备".equals(subMenu.getName())){
//                include_tezhongshebei.setVisibility(View.VISIBLE);
//            }else if("在建工地".equals(subMenu.getName())){
//                include_zaijiangongdi.setVisibility(View.VISIBLE);
//                checkList = new ArrayList<>();
//                checkList.add("否");
//                checkList.add("是");
//                MySpinnerAdapter<String> mStreetAdapter = new MySpinnerAdapter<>(this, checkList);
//                gd_spinner_check.setAdapter(mStreetAdapter);
//
//                //更新开工时间
//                mTimePickerListener = time -> gd_et_kaigongnriqi.setText(time);
//                initTimePopupWindow();
//                //更新开工时间
//                mTimePickerListener = time -> gd_et_jungongriqi.setText(time);
//                initTimePopupWindow_gungong();
//            }else if("危化品".equals(subMenu.getName())){
//
//            }
//
//        }
//
//        userInfo = DataHelper.getDeviceData(this, Constant.SP_KEY_USER_INFO);
//        initSpinnerStreetGrid();
//
//        smartRefresh.setEnableLoadMore(false);
//        smartRefresh.setEnableRefresh(false);
//
//        initRecyclerView();
//
//        if (mPresenter != null) {
//            mPresenter.findAllStreetCommunity(0);
//        }
//    }
//
//    private interface TimePickerListener {
//        void updateTime(String time);
//    }
//
//    private void initRecyclerView() {
//        //上传图片
//        uploadPhotoList = new ArrayList<>();
//        photoAdapter = new UploadPhotoAdapter(this, uploadPhotoList);
//        upload_picture_list.setLayoutManager(new LinearLayoutManager(this));
//        upload_picture_list.setHasFixedSize(true);
//        upload_picture_list.setAdapter(photoAdapter);
//        photoAdapter.setOnItemOnClilcklisten(new UploadPhotoAdapter.OnItemOnClicklisten() {
//            @Override
//            public void onItemDeleteClick(View v, int position) {
//                if (uploadPhotoList.get(position).getIsSuccess() == 1) { //上传成功  显示删除
//                    uploadPhotoList.remove(position);
//                    photoAdapter.notifyDataSetChanged();
//                } else if (uploadPhotoList.get(position).getIsSuccess() == 0) {
//
//                } else { //上传失败 重新上传
//                    if (mPresenter != null) {
//                        mPresenter.uploadFile(uploadPhotoList.get(position).getFileName());
//                    }
//                }
//            }
//        });
//
//        //上传检查记录
//        fileList = new ArrayList<>();
//        fileAdapter = new UploadPhotoAdapter(this, fileList);
//        check_record_list.setLayoutManager(new LinearLayoutManager(this));
//        check_record_list.setHasFixedSize(true);
//        check_record_list.setAdapter(fileAdapter);
//        fileAdapter.setOnItemOnClilcklisten(new UploadPhotoAdapter.OnItemOnClicklisten() {
//            @Override
//            public void onItemDeleteClick(View v, int position) {
//                if (fileList.get(position).getIsSuccess() == 1) { //上传成功  显示删除
//                    fileList.remove(position);
//                    fileAdapter.notifyDataSetChanged();
//                } else if (fileList.get(position).getIsSuccess() == 0) {
//
//                } else { //上传失败 重新上传
//                    if (mPresenter != null) {
//                        mPresenter.uploadFile(fileList.get(position).getFileName());
//                    }
//                }
//            }
//        });
//    }
//
//    /**
//     * 初始化街道社区网格Spinner
//     */
//    private void initSpinnerStreetGrid() {
//        mStreetList = new ArrayList<>();
//        mStreetAdapter = new MySpinnerAdapter<>(this, mStreetList);
//        spinner_street.setAdapter(mStreetAdapter);
//        spinner_street.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long checkId) {
//                spinner_community.setSelection(0);
//                mCommunityList.clear();
//                mCommunityList.addAll(mStreetList.get(position).getChildList());
//                if (userInfo != null && !TextUtils.isEmpty(userInfo.getStreetName())) {
//                    if (mCommunityList != null) {
//                        for (int i = 0; i < mCommunityList.size(); i++) {
//                            if (userInfo.getCommunityName().equals(mCommunityList.get(i).getName())) {
//                                spinner_community.setSelection(i, true);
//                                break;
//                            }
//                        }
//                    }
//                }
//                mCommunityAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        mCommunityList = new ArrayList<>();
//        mCommunityAdapter = new MySpinnerAdapter<>(this, mCommunityList);
//        spinner_community.setAdapter(mCommunityAdapter);
//        spinner_community.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
////                spinnerCaseGrid.setSelection(0);
////                mGridList.clear();
////                mGridList.add(mGrid);
////                mGridAdapter.notifyDataSetChanged();
////                if (position > 0) {
////                    mCommunityId = mCommunityList.get(position).getId();
////                    if (mPresenter != null) {
////                        mPresenter.findGridListByCommunityId(mCommunityList.get(position).getId());
////                    }
////                } else {
////                    mCommunityId = "";
////                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//    }
//
//    @Override
//    public void showLoading() {
//        if (loadingDialog == null)
//            loadingDialog = new LoadingProgressDialog.Builder(this)
//                    .setCancelable(true)
//                    .setCancelOutside(true).create();
//        if (!loadingDialog.isShowing())
//            loadingDialog.show();
//    }
//
//    @Override
//    public void hideLoading() {
//        if (loadingDialog != null && loadingDialog.isShowing())
//            loadingDialog.dismiss();
//    }
//
//    @Override
//    public void showMessage(@NonNull String message) {
//        checkNotNull(message);
//        ArmsUtils.snackbarText(message);
//    }
//
//    @Override
//    public void launchActivity(@NonNull Intent intent) {
//        checkNotNull(intent);
//        ArmsUtils.startActivity(intent);
//    }
//
//    @Override
//    public void killMyself() {
//        finish();
//    }
//
//    @Override
//    public void httpStreetCommunitySuccess(List<Street> result) {
//        mStreetList.clear();
//        mStreetList.addAll(result.get(0).getChildList());
//        if (userInfo != null && !TextUtils.isEmpty(userInfo.getStreetName())) {
//            if (mStreetList != null) {
//                for (int i = 0; i < mStreetList.size(); i++) {
//                    if (userInfo.getStreetName().equals(mStreetList.get(i).getName())) {
//                        spinner_street.setSelection(i, true);
//                        break;
//                    }
//                }
//            }
//        }
//        mStreetAdapter.notifyDataSetChanged();
//    }
//
//    @Override
//    public void httpInsertInfoSuccess() {
//        setResult(1);
//        finish();
//    }
//
//    @Override
//    public void uploadFileSuccess(UploadFile uploadPhoto) {
//        for (int i = 0; i < fileList.size(); i++) {
//            if (fileList.get(i).getFileName().equals(uploadPhoto.getFileName())) {
//                fileList.get(i).setFileDomain(uploadPhoto.getFileDomain());
//                fileList.get(i).setFileRelativePath(uploadPhoto.getFileRelativePath());
//                fileList.get(i).setIsSuccess(uploadPhoto.getIsSuccess());
//                fileAdapter.notifyItemChanged(i);
//            }
//        }
//    }
//
//    @Override
//    public void uploadPhotoSuccess(UploadFile uploadPhoto) {
//        for (int i = 0; i < uploadPhotoList.size(); i++) {
//            if (uploadPhotoList.get(i).getFileName().equals(uploadPhoto.getFileName())) {
//                uploadPhotoList.get(i).setFileDomain(uploadPhoto.getFileDomain());
//                uploadPhotoList.get(i).setFileRelativePath(uploadPhoto.getFileRelativePath());
//                uploadPhotoList.get(i).setIsSuccess(uploadPhoto.getIsSuccess());
//                photoAdapter.notifyItemChanged(i);
//            }
//        }
//    }
//
//
//    @OnClick({R.id.btn_location_obtain,R.id.tv_add_equipment, R.id.btn_submit_pic, R.id.btn_submit_check_record, R.id.submit,R.id.gd_et_kaigongnriqi,R.id.gd_et_jungongriqi})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.btn_location_obtain:  //定位
//                Intent intent = new Intent(this, MapActivity.class);
//                intent.putExtra("lat", lat);
//                intent.putExtra("lng", lng);
//                this.startActivityForResult(intent,Constant.MAP_REQUEST_CODE);
//                break;
//            case R.id.tv_add_equipment:  //删除
//                addComponentView();
//                break;
//            case R.id.gd_et_kaigongnriqi:
//                mTimePickerPopupWindow.show();
//                break;
//                case R.id.gd_et_jungongriqi:
//                    mTimePickerPopupWindow_jungong.show();
//                break;
//
//            case R.id.btn_submit_pic:
////                isWhich = 1;
//                pictureSelector(phot_requestCode);
//                break;
//            case R.id.btn_submit_check_record:
////                isWhich = 2;
//                pictureSelector(file_requestCode);
//                break;
//            case R.id.submit:
//                if (mPresenter != null) {
//                    if (checkParams()) {
//                        String streetId = String.valueOf(mStreetList.get(spinner_street.getSelectedItemPosition()).getId());
//                        String communityId = String.valueOf(mCommunityList.get(spinner_community.getSelectedItemPosition()).getId());
//                        String photos = new Gson().toJsonTree(uploadPhotoList).toString();
//                        String checkRecord = new Gson().toJsonTree(fileList).toString();
//                        RequestBody requestBody = null;
//                        if("特种设备".equals(subMenu.getName())){
//                            List<Equipment> equipmentList = new ArrayList<>();
//                            for (int i = 0; i < ly_addView.getChildCount(); i++) {
//                                View equipmentView = ly_addView.getChildAt(0);
//                                EditText et_category = equipmentView.findViewById(R.id.et_category);
//                                EditText et_name = equipmentView.findViewById(R.id.et_name);
//                                EditText et_code = equipmentView.findViewById(R.id.et_code);
//                                AppCompatSpinner spinner_check = equipmentView.findViewById(R.id.spinner_check);
//                                if (TextUtils.isEmpty(et_category.getText().toString())) {
//                                    showMessage("请输入特种设备种类");
//                                    return;
//                                } else if (TextUtils.isEmpty(et_name.getText().toString())) {
//                                    showMessage("请输入特种设备名称");
//                                    return;
//                                }
//                                Equipment equipment = new Equipment();
//                                equipment.setCategory(et_category.getText().toString());
//                                equipment.setName(et_name.getText().toString());
//                                equipment.setRegisterCode(et_code.getText().toString());
//                                equipment.setCheckIsValid(checkList.get(spinner_check.getSelectedItemPosition()));
//                                equipmentList.add(equipment);
//                            }
//                            String tezhongshebei = new Gson().toJsonTree(equipmentList).toString();
//                            requestBody = RequestParamUtils.thingInsertInfo(streetId,communityId, String.valueOf(userInfo.getGridId()), String.valueOf(lat), String.valueOf(lng), photos, checkRecord, et_name.getText().toString(),
//                                    tezhongshebei, et_farenName.getText().toString(), et_address.getText().toString());
//
//                            mPresenter.insertInfo(requestBody);
//                        }else if("在建工地".equals(subMenu.getName())){
//                            requestBody = RequestParamUtils.thingInsertInfo_gd(streetId,communityId, String.valueOf(userInfo.getGridId()), String.valueOf(lat), String.valueOf(lng), photos, checkRecord,
//                                    gd_et_mark.getText().toString(),gd_et_name.getText().toString(),gd_et_address.getText().toString(),gd_et_shigongdanwei.getText().toString(),
//                                    checkList.get(gd_spinner_check.getSelectedItemPosition()), gd_et_price.getText().toString(), gd_et_jingduzhuangtai.getText().toString(),
//                                    gd_jianzhumianji.getText().toString(),gd_et_kaigongnriqi.getText().toString(),gd_et_jungongriqi.getText().toString(),gd_et_jianshedanwei.getText().toString(),
//                                    gd_et_jianlidanwei.getText().toString());
//                            mPresenter.insertInfo(requestBody);
//                        }
//                        if(requestBody!=null){
//                            mPresenter.insertInfo(requestBody);
//                        }
//                    }
//
//                }
//                break;
//        }
//    }
//
//    private void addComponentView() {
//        if (checkList == null) {
//            checkList = new ArrayList<>();
//            checkList.add("否");
//            checkList.add("是");
//        }
//
//        View view = getLayoutInflater().inflate(R.layout.component_include_add, null);
//        AppCompatSpinner spinner_check = view.findViewById(R.id.spinner_check);
//        MySpinnerAdapter<String> mStreetAdapter = new MySpinnerAdapter<>(this, checkList);
//        spinner_check.setAdapter(mStreetAdapter);
//
//
//        TextView tv_delete = view.findViewById(R.id.tv_delete);
//        tv_delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ly_addView.removeView((View) view.getParent().getParent());
//            }
//        });
//        ly_addView.addView(view);
//    }
//
//
//    /**
//     * 头像选择 PictureSelector
//     */
//    private void pictureSelector(int requestCode) {
//        PictureSelector.create(this)
//                .openGallery(PictureMimeType.ofImage())
//                .imageSpanCount(4)
//                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
//                .previewImage(false)
//                .isCamera(true)
//                .enableCrop(false)
//                .compress(true)
//                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
//                .minimumCompressSize(100)
//                .withAspectRatio(1, 1)
//                .showCropFrame(true)
//                .rotateEnabled(true)
//                .isDragFrame(true)
//                .forResult(requestCode);
//    }
//
//    private boolean checkParams() {
//        if (subMenu != null) {
//            if("特种设备".equals(subMenu.getName())){
//                if (TextUtils.isEmpty(et_name.getText().toString())) {
//                    showMessage("请输入单位名称");
//                    return false;
//                } else if (TextUtils.isEmpty(et_address.getText().toString())) {
//                    showMessage("请输入地址");
//                    return false;
//                } else if (lat == 0 || lng == 0) {
//                    showMessage("请定位位置");
//                    return false;
//                } else {
//                    return true;
//                }
//            }else if("在建工地".equals(subMenu.getName())){
//                if (TextUtils.isEmpty(gd_et_name.getText().toString())) {
//                    showMessage("请输入工程名称");
//                    return false;
//                } else if (TextUtils.isEmpty(gd_et_address.getText().toString())) {
//                    showMessage("请输入工程地址");
//                    return false;
//                } else if (TextUtils.isEmpty(gd_et_shigongdanwei.getText().toString())) {
//                    showMessage("请输入施工单位");
//                    return false;
//                } else if (lat == 0 || lng == 0) {
//                    showMessage("请定位位置");
//                    return false;
//                } else {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
//            switch (requestCode) {
//                case phot_requestCode:  //上传图片
//                    for (LocalMedia media : selectList) {
//                        // 1.media.getPath(); 为原图path
//                        // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
//                        // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
//                        String compressedPath = media.getCompressPath();
//                        UploadFile uploadPhoto1 = new UploadFile();
//                        uploadPhoto1.setFileName(compressedPath);
//                        String size1 = FileSizeUtil.getAutoFileOrFilesSize(uploadPhoto1.getFileName());
//                        uploadPhoto1.setFileSize(size1);
//                        uploadPhotoList.add(uploadPhoto1);
//                        photoAdapter.notifyDataSetChanged();
//                        if (mPresenter != null) {
//                            mPresenter.uploadPhotoFile(compressedPath);
//                        }
//                    }
//                    break;
//                case file_requestCode:  //上传检查记录
//                    for (LocalMedia media : selectList) {
//                        // 1.media.getPath(); 为原图path
//                        // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
//                        // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
//                        String compressedPath = media.getCompressPath();
//                        UploadFile uploadPhoto1 = new UploadFile();
//                        uploadPhoto1.setFileName(compressedPath);
//                        String size1 = FileSizeUtil.getAutoFileOrFilesSize(uploadPhoto1.getFileName());
//                        uploadPhoto1.setFileSize(size1);
//                        fileList.add(uploadPhoto1);
//                        fileAdapter.notifyDataSetChanged();
//                        if (mPresenter != null) {
//                            mPresenter.uploadFile(compressedPath);
//                        }
//                    }
//                    break;
//            }
//        }else if(requestCode==Constant.MAP_REQUEST_CODE && resultCode==Constant.MAP_REQUEST_CODE){
//            if(data!=null){
//                lng = data.getDoubleExtra("lng",0);
//                lat = data.getDoubleExtra("lat",0);
//
//                tvLocationLatitude.setText(String.valueOf(lat));
//                tvLocationLongitude.setText(String.valueOf(lng));
//            }
//        }
//    }
//
//    /**
//     * 初始化时间选择弹出框
//     * 开工日期
//     */
//    private void initTimePopupWindow() {
//        gd_et_kaigongnriqi.setText(DateUtils.getDateToStringNonSecond(DateUtils.getCurrentTimeMillis(), DateUtils.dateString1));
//
//        View timePickerView = View.inflate(this, R.layout.view_pop_time_picker, null);
//
//        mTimePickerPopupWindow = CustomPopupWindow.builder()
//                .parentView(gd_et_kaigongnriqi).contentView(timePickerView)
//                .isOutsideTouch(false)
//                .isWrap(false)
//                .customListener(contentView -> {
//                    String[] mYear = new String[1];
//                    String[] mMonth = new String[1];
//                    String[] mDay = new String[1];
//                    String[] mHour = new String[1];
//                    String[] mMinute = new String[1];
//                    mYear[0] = String.valueOf(DateUtils.getCurrentYear());
//                    mMonth[0] = String.valueOf(DateUtils.getCurrentMonth());
//                    mDay[0] = String.valueOf(DateUtils.getCurrentDate());
//                    mHour[0] = String.valueOf(DateUtils.getCurrentDateHour());
//                    mMinute[0] = String.valueOf(DateUtils.getCurrentDateMinute());
//
//                    TextView tvCurrentDate = contentView.findViewById(R.id.tv_current_time);
//                    tvCurrentDate.setText(DateUtils.getDateToStringNonSecond(DateUtils.getCurrentTimeMillis(), DateUtils.dateString1));
//
//                    DatePicker datePicker = contentView.findViewById(R.id.datePicker);
//                    datePicker.setMinDate(DateUtils.get1970YearTimeStamp());
//                    datePicker.updateDate(Integer.parseInt(mYear[0]), Integer.parseInt(mMonth[0]), Integer.parseInt(mDay[0]));
//                    datePicker.init(DateUtils.getCurrentYear(), DateUtils.getCurrentMonth(),
//                            DateUtils.getCurrentDate(), (view, year, monthOfYear, dayOfMonth) -> {
//                                mYear[0] = String.valueOf(year);
//                                mMonth[0] = monthOfYear < 9 ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);
//                                mDay[0] = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
//
//                                tvCurrentDate.setText(MessageFormat.format("{0}-{1}-{2} {3}:{4}",
//                                        mYear[0], mMonth[0], mDay[0], mHour[0], mMinute[0]));
//                            }
//                    );
//
//                    TimePicker timePicker = contentView.findViewById(R.id.timePicker);
//                    timePicker.setIs24HourView(true);
//                    timePicker.setCurrentHour(Integer.parseInt(mHour[0]));
//                    timePicker.setCurrentMinute(Integer.parseInt(mMinute[0]));
//                    timePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> {
//                        mHour[0] = hourOfDay < 10 ? "0" + hourOfDay : String.valueOf(hourOfDay);
//                        mMinute[0] = minute < 10 ? "0" + minute : String.valueOf(minute);
//
//                        tvCurrentDate.setText(MessageFormat.format("{0}-{1}-{2} {3}:{4}", mYear[0], mMonth[0], mDay[0], mHour[0], mMinute[0]));
//                    });
//
//                    TextView tvSure = contentView.findViewById(R.id.tv_sure);
//                    TextView tvToday = contentView.findViewById(R.id.tv_today);
//                    TextView tvCancel = contentView.findViewById(R.id.tv_cancel);
//
//                    tvSure.setOnClickListener(v -> {
//                        if (mTimePickerListener != null) {
//                            mTimePickerListener.updateTime(tvCurrentDate.getText().toString());
//                        }
//                        mTimePickerPopupWindow.dismiss();
//                    });
//
//                    tvToday.setOnClickListener(v ->
//                            datePicker.updateDate(DateUtils.getCurrentYear(), DateUtils.getCurrentMonth(), DateUtils.getCurrentDate()));
//
//                    tvCancel.setOnClickListener(v -> mTimePickerPopupWindow.dismiss());
//
//                })
//                .isWrap(true).build();
//    }
//
//    /**
//     * 初始化时间选择弹出框
//     * 开工日期
//     */
//    private void initTimePopupWindow_gungong() {
//        gd_et_jungongriqi.setText(DateUtils.getDateToStringNonSecond(DateUtils.getCurrentTimeMillis(), DateUtils.dateString1));
//
//        View timePickerView = View.inflate(this, R.layout.view_pop_time_picker, null);
//
//        mTimePickerPopupWindow_jungong = CustomPopupWindow.builder()
//                .parentView(gd_et_kaigongnriqi).contentView(timePickerView)
//                .isOutsideTouch(false)
//                .isWrap(false)
//                .customListener(contentView -> {
//                    String[] mYear = new String[1];
//                    String[] mMonth = new String[1];
//                    String[] mDay = new String[1];
//                    String[] mHour = new String[1];
//                    String[] mMinute = new String[1];
//                    mYear[0] = String.valueOf(DateUtils.getCurrentYear());
//                    mMonth[0] = String.valueOf(DateUtils.getCurrentMonth());
//                    mDay[0] = String.valueOf(DateUtils.getCurrentDate());
//                    mHour[0] = String.valueOf(DateUtils.getCurrentDateHour());
//                    mMinute[0] = String.valueOf(DateUtils.getCurrentDateMinute());
//
//                    TextView tvCurrentDate = contentView.findViewById(R.id.tv_current_time);
//                    tvCurrentDate.setText(DateUtils.getDateToStringNonSecond(DateUtils.getCurrentTimeMillis(), DateUtils.dateString1));
//
//                    DatePicker datePicker = contentView.findViewById(R.id.datePicker);
//                    datePicker.setMinDate(DateUtils.get1970YearTimeStamp());
//                    datePicker.updateDate(Integer.parseInt(mYear[0]), Integer.parseInt(mMonth[0]), Integer.parseInt(mDay[0]));
//                    datePicker.init(DateUtils.getCurrentYear(), DateUtils.getCurrentMonth(),
//                            DateUtils.getCurrentDate(), (view, year, monthOfYear, dayOfMonth) -> {
//                                mYear[0] = String.valueOf(year);
//                                mMonth[0] = monthOfYear < 9 ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);
//                                mDay[0] = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
//
//                                tvCurrentDate.setText(MessageFormat.format("{0}-{1}-{2} {3}:{4}",
//                                        mYear[0], mMonth[0], mDay[0], mHour[0], mMinute[0]));
//                            }
//                    );
//
//                    TimePicker timePicker = contentView.findViewById(R.id.timePicker);
//                    timePicker.setIs24HourView(true);
//                    timePicker.setCurrentHour(Integer.parseInt(mHour[0]));
//                    timePicker.setCurrentMinute(Integer.parseInt(mMinute[0]));
//                    timePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> {
//                        mHour[0] = hourOfDay < 10 ? "0" + hourOfDay : String.valueOf(hourOfDay);
//                        mMinute[0] = minute < 10 ? "0" + minute : String.valueOf(minute);
//
//                        tvCurrentDate.setText(MessageFormat.format("{0}-{1}-{2} {3}:{4}", mYear[0], mMonth[0], mDay[0], mHour[0], mMinute[0]));
//                    });
//
//                    TextView tvSure = contentView.findViewById(R.id.tv_sure);
//                    TextView tvToday = contentView.findViewById(R.id.tv_today);
//                    TextView tvCancel = contentView.findViewById(R.id.tv_cancel);
//
//                    tvSure.setOnClickListener(v -> {
//                        if (mTimePickerListener != null) {
//                            mTimePickerListener.updateTime(tvCurrentDate.getText().toString());
//                        }
//                        mTimePickerPopupWindow_jungong.dismiss();
//                    });
//
//                    tvToday.setOnClickListener(v ->
//                            datePicker.updateDate(DateUtils.getCurrentYear(), DateUtils.getCurrentMonth(), DateUtils.getCurrentDate()));
//
//                    tvCancel.setOnClickListener(v -> mTimePickerPopupWindow_jungong.dismiss());
//
//                })
//                .isWrap(true).build();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (mTimePickerPopupWindow != null && mTimePickerPopupWindow.isShowing()) {
//            mTimePickerPopupWindow.dismiss();
//            mTimePickerPopupWindow = null;
//        }
//        if (mTimePickerPopupWindow_jungong != null && mTimePickerPopupWindow_jungong.isShowing()) {
//            mTimePickerPopupWindow_jungong.dismiss();
//            mTimePickerPopupWindow_jungong = null;
//        }
//    }
//}
