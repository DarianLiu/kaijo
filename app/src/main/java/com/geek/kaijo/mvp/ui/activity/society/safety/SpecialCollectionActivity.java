package com.geek.kaijo.mvp.ui.activity.society.safety;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cmmap.api.location.CmccLocation;
import com.geek.kaijo.R;
import com.geek.kaijo.Utils.FileSizeUtil;
import com.geek.kaijo.Utils.GPSUtils;
import com.geek.kaijo.app.Constant;
import com.geek.kaijo.app.api.RequestParamUtils;
import com.geek.kaijo.di.component.DaggerSpecialCollectionComponent;
import com.geek.kaijo.mvp.contract.SpecialCollectionContract;
import com.geek.kaijo.mvp.model.entity.Equipment;
import com.geek.kaijo.mvp.model.entity.GridItemContent;
import com.geek.kaijo.mvp.model.entity.Street;
import com.geek.kaijo.mvp.model.entity.ThingPositionInfo;
import com.geek.kaijo.mvp.model.entity.UploadFile;
import com.geek.kaijo.mvp.model.entity.UserInfo;
import com.geek.kaijo.mvp.presenter.SpecialCollectionPresenter;
import com.geek.kaijo.mvp.ui.activity.MapActivity;
import com.geek.kaijo.mvp.ui.adapter.MySpinnerAdapter;
import com.geek.kaijo.mvp.ui.adapter.UploadPhotoAdapter;
import com.geek.kaijo.mvp.ui.fragment.society.culture.CulturalRelicFragment;
import com.geek.kaijo.mvp.ui.fragment.society.fooddrug.DrugSafteFragment;
import com.geek.kaijo.mvp.ui.fragment.society.fooddrug.FoodSafteFragment;
import com.geek.kaijo.mvp.ui.fragment.society.safety.SpecialCollectionFragment;
import com.geek.kaijo.mvp.ui.fragment.society.safety.TyphoonFloodFragment;
import com.geek.kaijo.mvp.ui.fragment.society.safety.WHPFragment;
import com.geek.kaijo.mvp.ui.fragment.society.safety.ZaiJianGongDi;
import com.geek.kaijo.view.LoadingProgressDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.DataHelper;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.RequestBody;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class SpecialCollectionActivity extends BaseActivity<SpecialCollectionPresenter> implements SpecialCollectionContract.View {
    private static final int phot_requestCode = 1011;  //上传图片
    private static final int file_requestCode = 1012;  //上传检查记录

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.spinner_street)
    AppCompatSpinner spinner_street;//街道spinner
    private List<Street> mStreetList;//街道列表
    private MySpinnerAdapter<Street> mStreetAdapter;//街道adapter

    @BindView(R.id.spinner_community)
    AppCompatSpinner spinner_community;//社区spiner
    private List<Street> mCommunityList;//社区列表
    private MySpinnerAdapter<Street> mCommunityAdapter;//社区adapter
    private UserInfo userInfo;

    @BindView(R.id.btn_location_obtain)
    TextView btn_location_obtain;
    @BindView(R.id.submit)
    TextView submit;
    @BindView(R.id.tv_location_longitude)
    TextView tvLocationLongitude;
    @BindView(R.id.tv_location_latitude)
    TextView tvLocationLatitude;
    @BindView(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;

    @BindView(R.id.tv_photo_list)
    TextView tv_photo_list;
    @BindView(R.id.tv_video_list)
    TextView tv_video_list;
    @BindView(R.id.la_check_record)
    LinearLayout la_check_record;
    @BindView(R.id.btn_submit_check_record)
    TextView btn_submit_check_record;

//    @BindView(R.id.include_tezhongshebei)
//    LinearLayout include_tezhongshebei;
//    @BindView(R.id.include_zaijiangongdi)
//    LinearLayout include_zaijiangongdi;


    //上传图片
    @BindView(R.id.upload_picture_list)
    RecyclerView upload_picture_list;
    private List<UploadFile> uploadPhotoList;
    private UploadPhotoAdapter photoAdapter;

    //上传检查记录
    @BindView(R.id.check_record_list)
    RecyclerView check_record_list;
    private List<UploadFile> fileList;
    private UploadPhotoAdapter fileAdapter;

    private GridItemContent subMenu;
    private double lat;
    private double lng;

    private LoadingProgressDialog loadingDialog;

    //    @BindView(R.id.ly_parentView)
//    LinearLayout ly_parentView;
    Fragment fragment;

    /*森林防火*/
    private LinearLayout ly_senlinfanhuo;
    private EditText senl_et_name;

    /*冬季除雪*/
    private LinearLayout ly_dongjichuxue;
    private EditText dongjichuxue_et_address; //具体地址
    private AppCompatSpinner dongjichuxue_et_isPodao; //是否为破路

    /*文明祭祀*/
    private LinearLayout ly_wenmingjisi;
    private EditText jisi_et_address; //祭扫地点
    private EditText jisi_farenName; //责任人
    private EditText jisi_phone; //联系电话
    private EditText jisi_zerenquRemark; //请输入责任区扫描

    /*网吧*/
    private LinearLayout ly_wangba;
    private EditText et_wangba_name; //名称
    private EditText et_wangba_adress; //地址
    private EditText wangba_farenName; //法定代表人
    private EditText wangba__phone; //联系电话

    /*演出场所*/
    private LinearLayout ly_yanchu;
    private EditText et_yanchu_name; //名称
    private EditText et_yanchu_adress; //地址
    private EditText jingyingzheName; //经营者名称
    private EditText yanchu__phone; //联系电话

    private MyHandler myHandler;
    public List<String> checkList;

    public ThingPositionInfo thingPositionInfo;
    private String thingPositionId;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSpecialCollectionComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_special_collection; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        subMenu = (GridItemContent) getIntent().getSerializableExtra("Submenu");
        thingPositionInfo = (ThingPositionInfo) getIntent().getSerializableExtra("ThingPositionInfo");
        if (subMenu != null) {
            tvToolbarTitle.setText(subMenu.getName() + "采集");
//            Fragment fragment = new SpecialCollectionFragment();;
            if ("特种设备".equals(subMenu.getName())) {
                fragment = new SpecialCollectionFragment();
            } else if ("在建工地".equals(subMenu.getName())) {
                fragment = new ZaiJianGongDi();
            } else if ("危化品".equals(subMenu.getName())) {
                fragment = new WHPFragment();
            } else if ("食品".equals(subMenu.getName()) || "餐饮".equals(subMenu.getName())) {
                fragment = new FoodSafteFragment();
            } else if ("药品".equals(subMenu.getName())) {
                fragment = new DrugSafteFragment();
            } else if ("森林防火".equals(subMenu.getName())) {
//                fragment = new DrugSafteFragment();
                ly_senlinfanhuo = findViewById(R.id.ly_senlinfanhuo);
                ly_senlinfanhuo.setVisibility(View.VISIBLE);
                senl_et_name = findViewById(R.id.senl_et_name);
                if (thingPositionInfo != null) {
                    senl_et_name.setText(thingPositionInfo.getName());
                }
                la_check_record.setVisibility(View.GONE);
                btn_submit_check_record.setVisibility(View.GONE);
            } else if ("防台防汛".equals(subMenu.getName())) {
                fragment = new TyphoonFloodFragment();
                la_check_record.setVisibility(View.GONE);
                btn_submit_check_record.setVisibility(View.GONE);
            } else if ("冬季除雪".equals(subMenu.getName())) {
                ly_dongjichuxue = findViewById(R.id.ly_dongjichuxue);
                ly_dongjichuxue.setVisibility(View.VISIBLE);
                dongjichuxue_et_address = findViewById(R.id.dongjichuxue_et_address);
                dongjichuxue_et_isPodao = findViewById(R.id.dongjichuxue_et_isPodao);
                if (checkList == null) {
                    checkList = new ArrayList<>();
                    checkList.add("否");
                    checkList.add("是");
                }
                MySpinnerAdapter<String> mStreetAdapter = new MySpinnerAdapter<>(this, checkList);
                dongjichuxue_et_isPodao.setAdapter(mStreetAdapter);

                if (thingPositionInfo != null) {
                    dongjichuxue_et_address.setText(thingPositionInfo.getAddress());
                    if ("是".equals(thingPositionInfo.getIsPodao())) {
                        dongjichuxue_et_isPodao.setSelection(1);
                    }
                }
                la_check_record.setVisibility(View.GONE);
                btn_submit_check_record.setVisibility(View.GONE);
            } else if ("文明祭祀".equals(subMenu.getName())) {
                ly_wenmingjisi = findViewById(R.id.ly_wenmingjisi);
                ly_wenmingjisi.setVisibility(View.VISIBLE);
                jisi_et_address = findViewById(R.id.jisi_et_address);
                jisi_farenName = findViewById(R.id.jisi_farenName);
                jisi_phone = findViewById(R.id.jisi_phone);
                jisi_zerenquRemark = findViewById(R.id.jisi_zerenquRemark);

                if (thingPositionInfo != null) {
                    jisi_et_address.setText(thingPositionInfo.getAddress());
                    jisi_farenName.setText(thingPositionInfo.getFarenName());
                    jisi_phone.setText(thingPositionInfo.getContact());
                    jisi_zerenquRemark.setText(thingPositionInfo.getZerenquRemark());

                }
                la_check_record.setVisibility(View.GONE);
                btn_submit_check_record.setVisibility(View.GONE);
            } else if ("网吧".equals(subMenu.getName())) {
                ly_wangba = findViewById(R.id.ly_wangba);
                ly_wangba.setVisibility(View.VISIBLE);
                et_wangba_name = findViewById(R.id.et_wangba_name);
                et_wangba_adress = findViewById(R.id.et_wangba_adress);
                wangba_farenName = findViewById(R.id.wangba_farenName);
                wangba__phone = findViewById(R.id.wangba__phone);

                if (thingPositionInfo != null) {
                    et_wangba_name.setText(thingPositionInfo.getName());
                    et_wangba_adress.setText(thingPositionInfo.getAddress());
                    wangba_farenName.setText(thingPositionInfo.getFarenName());
                    wangba__phone.setText(thingPositionInfo.getContact());

                }
            } else if ("文物保护单位".equals(subMenu.getName())) {
                fragment = new CulturalRelicFragment();
            } else if ("演出场所".equals(subMenu.getName()) || "游艺娱乐".equals(subMenu.getName()) || "演艺娱乐".equals(subMenu.getName()) || "娱乐场所".equals(subMenu.getName())) {
                ly_yanchu = findViewById(R.id.ly_yanchu);
                ly_yanchu.setVisibility(View.VISIBLE);
                et_yanchu_name = findViewById(R.id.et_yanchu_name);
                et_yanchu_adress = findViewById(R.id.et_yanchu_adress);
                jingyingzheName = findViewById(R.id.jingyingzheName);
                yanchu__phone = findViewById(R.id.yanchu__phone);
                if (thingPositionInfo != null) {
                    et_yanchu_name.setText(thingPositionInfo.getName());
                    et_yanchu_adress.setText(thingPositionInfo.getAddress());
                    jingyingzheName.setText(thingPositionInfo.getJingyingzheName());
                    yanchu__phone.setText(thingPositionInfo.getContact());

                }
            }
            if (fragment != null) {

                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
            }
        }
        if (thingPositionInfo != null) {
            thingPositionId = String.valueOf(thingPositionInfo.getThingPositionId());
            Gson gson = new Gson();
            uploadPhotoList = gson.fromJson(thingPositionInfo.getPhotos(), new TypeToken<List<UploadFile>>() {
            }.getType());
            fileList = gson.fromJson(thingPositionInfo.getCheckRecord(), new TypeToken<List<UploadFile>>() {
            }.getType());
            lat = thingPositionInfo.getLat();
            lng = thingPositionInfo.getLng();
            tvLocationLongitude.setText(String.valueOf(lng));
            tvLocationLatitude.setText(String.valueOf(lat));


        }
        userInfo = DataHelper.getDeviceData(this, Constant.SP_KEY_USER_INFO);
        initSpinnerStreetGrid();

        smartRefresh.setEnableLoadMore(false);
        smartRefresh.setEnableRefresh(false);

        initRecyclerView();

        if (mPresenter != null) {
            mPresenter.findAllStreetCommunity(0);
        }
        myHandler = new MyHandler(this);
    }


    private void initRecyclerView() {
        //上传图片
        if (uploadPhotoList == null) {
            uploadPhotoList = new ArrayList<>();
        }
        photoAdapter = new UploadPhotoAdapter(this, uploadPhotoList);
        DividerItemDecoration divider_photo = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL); //分割线
        upload_picture_list.addItemDecoration(divider_photo);
        upload_picture_list.setLayoutManager(new LinearLayoutManager(this));
        upload_picture_list.setHasFixedSize(true);
        upload_picture_list.setAdapter(photoAdapter);
        photoAdapter.setOnItemOnClilcklisten(new UploadPhotoAdapter.OnItemOnClicklisten() {
            @Override
            public void onItemDeleteClick(View v, int position) {
                uploadPhotoList.remove(position);
                photoAdapter.notifyDataSetChanged();
                if (photoAdapter.getItemCount() != 0) {
                    tv_photo_list.setVisibility(View.GONE);
                } else {
                    tv_photo_list.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onItemAgainUploadClick(View v, int position) { //上传失败 重新上传
                if (mPresenter != null) {
                    mPresenter.uploadPhotoFile(uploadPhotoList.get(position).getFileName());
                }
            }
        });

        if (photoAdapter.getItemCount() != 0) {
            tv_photo_list.setVisibility(View.GONE);
        } else {
            tv_photo_list.setVisibility(View.VISIBLE);
        }

        //上传检查记录
        if (fileList == null) {
            fileList = new ArrayList<>();
        }
        fileAdapter = new UploadPhotoAdapter(this, fileList);
        DividerItemDecoration divider_file = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL); //分割线
        check_record_list.addItemDecoration(divider_file);
        check_record_list.setLayoutManager(new LinearLayoutManager(this));
        check_record_list.setHasFixedSize(true);
        check_record_list.setAdapter(fileAdapter);
        fileAdapter.setOnItemOnClilcklisten(new UploadPhotoAdapter.OnItemOnClicklisten() {
            @Override
            public void onItemDeleteClick(View v, int position) {
                fileList.remove(position);
                fileAdapter.notifyDataSetChanged();
                if (fileAdapter.getItemCount() != 0) {
                    tv_video_list.setVisibility(View.GONE);
                } else {
                    tv_video_list.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onItemAgainUploadClick(View v, int position) { //上传失败 重新上传
                if (mPresenter != null) {
                    mPresenter.uploadFile(fileList.get(position).getFileName());
                }
            }
        });

        if (fileAdapter.getItemCount() != 0) {
            tv_video_list.setVisibility(View.GONE);
        } else {
            tv_video_list.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 初始化街道社区网格Spinner
     */
    private void initSpinnerStreetGrid() {
        mStreetList = new ArrayList<>();
        mStreetAdapter = new MySpinnerAdapter<>(this, mStreetList);
        spinner_street.setAdapter(mStreetAdapter);
        spinner_street.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long checkId) {
                spinner_community.setSelection(0);
                mCommunityList.clear();
                mCommunityList.addAll(mStreetList.get(position).getChildList());
                if (userInfo != null && !TextUtils.isEmpty(userInfo.getStreetName())) {
                    if (mCommunityList != null) {
                        for (int i = 0; i < mCommunityList.size(); i++) {
                            if (userInfo.getCommunityName().equals(mCommunityList.get(i).getName())) {
                                spinner_community.setSelection(i, true);
                                break;
                            }
                        }
                    }
                }
                mCommunityAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mCommunityList = new ArrayList<>();
        mCommunityAdapter = new MySpinnerAdapter<>(this, mCommunityList);
        spinner_community.setAdapter(mCommunityAdapter);
        spinner_community.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    public void httpStreetCommunitySuccess(List<Street> result) {
        mStreetList.clear();
        mStreetList.addAll(result.get(0).getChildList());
        if (userInfo != null && !TextUtils.isEmpty(userInfo.getStreetName())) {
            if (mStreetList != null) {
                for (int i = 0; i < mStreetList.size(); i++) {
                    if (userInfo.getStreetName().equals(mStreetList.get(i).getName())) {
                        spinner_street.setSelection(i, true);
                        break;
                    }
                }
            }
        }
        mStreetAdapter.notifyDataSetChanged();
    }

    @Override
    public void httpInsertInfoSuccess() {
        setResult(1);
        finish();
    }

    @Override
    public void uploadFileSuccess(UploadFile uploadPhoto) {
        for (int i = 0; i < fileList.size(); i++) {
            if (fileList.get(i).getFileName().equals(uploadPhoto.getFileName())) {
                fileList.get(i).setFileDomain(uploadPhoto.getFileDomain());
                fileList.get(i).setFileRelativePath(uploadPhoto.getFileRelativePath());
                fileList.get(i).setIsSuccess(uploadPhoto.getIsSuccess());
                fileAdapter.notifyItemChanged(i);
            }
        }
        if (fileAdapter.getItemCount() != 0) {
            tv_video_list.setVisibility(View.GONE);
        } else {
            tv_video_list.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void uploadPhotoSuccess(UploadFile uploadPhoto) {
        for (int i = 0; i < uploadPhotoList.size(); i++) {
            if (uploadPhotoList.get(i).getFileName().equals(uploadPhoto.getFileName())) {
                uploadPhotoList.get(i).setFileDomain(uploadPhoto.getFileDomain());
                uploadPhotoList.get(i).setFileRelativePath(uploadPhoto.getFileRelativePath());
                uploadPhotoList.get(i).setIsSuccess(uploadPhoto.getIsSuccess());
                photoAdapter.notifyItemChanged(i);
            }
        }
        if (photoAdapter.getItemCount() != 0) {
            tv_photo_list.setVisibility(View.GONE);
        } else {
            tv_photo_list.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void uploadPhotoError() {
        if(uploadPhotoList!=null){
            for (int i = 0; i < uploadPhotoList.size(); i++) {
                if (TextUtils.isEmpty(uploadPhotoList.get(i).getUrl())) {
                    uploadPhotoList.get(i).setIsSuccess(2);
                    photoAdapter.notifyItemChanged(i);
                }
            }
        }
        if(fileList!=null){
            for (int i = 0; i < fileList.size(); i++) {
                if (TextUtils.isEmpty(fileList.get(i).getUrl())) {
                    fileList.get(i).setIsSuccess(2);
                    fileAdapter.notifyItemChanged(i);
                }
            }
        }
    }


    @OnClick({R.id.btn_location_obtain, R.id.btn_submit_pic, R.id.btn_submit_check_record, R.id.submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_location_obtain:  //定位
                showLoading();
                GPSUtils.getInstance().setOnLocationListener(locationListener);
                myHandler.sendEmptyMessageDelayed(1, Constant.location_loadTime);
                break;
            case R.id.btn_submit_pic:
//                isWhich = 1;
                pictureSelector(phot_requestCode);
                break;
            case R.id.btn_submit_check_record:
//                isWhich = 2;
                pictureSelector(file_requestCode);
                break;
            case R.id.submit:
                if (mPresenter != null) {
                    if (checkParams()) {
                        String streetId = String.valueOf(mStreetList.get(spinner_street.getSelectedItemPosition()).getId());
                        String communityId = String.valueOf(mCommunityList.get(spinner_community.getSelectedItemPosition()).getId());
//                        String photos = new Gson().toJsonTree(uploadPhotoList).toString();
//                        String checkRecord = new Gson().toJsonTree(fileList).toString();
                        RequestBody requestBody = null;
                        if ("特种设备".equals(subMenu.getName())) {
                            if (fragment instanceof SpecialCollectionFragment) {
                                SpecialCollectionFragment equipmentFragment = (SpecialCollectionFragment) fragment;
                                if (!equipmentFragment.checkParams()) {
                                    return;
                                }
                                List<Equipment> equipmentList = new ArrayList<>();
                                for (int i = 0; i < equipmentFragment.ly_addView.getChildCount(); i++) {
                                    View equipmentView = equipmentFragment.ly_addView.getChildAt(i);
                                    EditText et_category = equipmentView.findViewById(R.id.et_category);
                                    EditText et_name = equipmentView.findViewById(R.id.et_name);
                                    EditText et_code = equipmentView.findViewById(R.id.et_code);
                                    AppCompatSpinner spinner_check = equipmentView.findViewById(R.id.spinner_check);
                                    if (TextUtils.isEmpty(et_category.getText().toString())) {
                                        showMessage("请输入特种设备种类");
                                        return;
                                    } else if (TextUtils.isEmpty(et_name.getText().toString())) {
                                        showMessage("请输入特种设备名称");
                                        return;
                                    }
                                    Equipment equipment = new Equipment();
                                    equipment.setCategory(et_category.getText().toString());
                                    equipment.setName(et_name.getText().toString());
                                    equipment.setRegisterCode(et_code.getText().toString());
                                    equipment.setCheckIsValid(equipmentFragment.checkList.get(spinner_check.getSelectedItemPosition()));
                                    equipmentList.add(equipment);
                                }
                                String tezhongshebei = new Gson().toJsonTree(equipmentList).toString();
                                requestBody = RequestParamUtils.thingInsertInfo(thingPositionId, String.valueOf(subMenu.getThingId()), streetId, communityId, String.valueOf(userInfo.getGridId()), String.valueOf(lat), String.valueOf(lng), uploadPhotoList, fileList, equipmentFragment.et_name.getText().toString(),
                                        tezhongshebei, equipmentFragment.et_farenName.getText().toString(), equipmentFragment.et_address.getText().toString());

                            }

                        } else if ("在建工地".equals(subMenu.getName())) {
                            if (fragment instanceof ZaiJianGongDi) {
                                ZaiJianGongDi mfragment = (ZaiJianGongDi) fragment;
                                if (!mfragment.checkParams()) {
                                    return;
                                }
                                requestBody = RequestParamUtils.thingInsertInfo_gd(thingPositionId, String.valueOf(subMenu.getThingId()), streetId, communityId, String.valueOf(userInfo.getGridId()), String.valueOf(lat), String.valueOf(lng), uploadPhotoList, fileList,
                                        mfragment.gd_et_mark.getText().toString(), mfragment.gd_et_name.getText().toString(), mfragment.gd_et_address.getText().toString(), mfragment.gd_et_shigongdanwei.getText().toString(),
                                        mfragment.checkList.get(mfragment.gd_spinner_check.getSelectedItemPosition()), mfragment.gd_et_price.getText().toString(), mfragment.gd_et_jingduzhuangtai.getText().toString(),
                                        mfragment.gd_jianzhumianji.getText().toString(), mfragment.gd_et_kaigongnriqi.getText().toString(), mfragment.gd_et_jungongriqi.getText().toString(), mfragment.gd_et_jianshedanwei.getText().toString(),
                                        mfragment.gd_et_jianlidanwei.getText().toString());
                            }
                        } else if (fragment instanceof WHPFragment) { //危化品
                            WHPFragment mfragment = (WHPFragment) fragment;
                            if (!mfragment.checkParams()) {
                                return;
                            }
                            requestBody = RequestParamUtils.thingInsertInfo_whp(thingPositionId, String.valueOf(subMenu.getThingId()), streetId, communityId, String.valueOf(userInfo.getGridId()), String.valueOf(lat), String.valueOf(lng), uploadPhotoList, fileList,
                                    mfragment.et_name.getText().toString(), mfragment.et_address.getText().toString(), mfragment.et_other.getText().toString(), mfragment.et_category.getText().toString());
                        } else if (fragment instanceof FoodSafteFragment) { //食品
                            FoodSafteFragment mfragment = (FoodSafteFragment) fragment;
                            if (!mfragment.checkParams()) {
                                return;
                            }
                            requestBody = RequestParamUtils.thingInsertInfo_food(thingPositionId, String.valueOf(subMenu.getThingId()), streetId, communityId, String.valueOf(userInfo.getGridId()), String.valueOf(lat), String.valueOf(lng), uploadPhotoList, fileList,
                                    mfragment.et_name.getText().toString(), mfragment.et_farenName.getText().toString(), mfragment.et_jjxz.getText().toString(), mfragment.et_jycs.getText().toString(),
                                    mfragment.et_xkzh.getText().toString(), mfragment.et_ztyt.getText().toString(), mfragment.checkList.get(mfragment.spinner_street.getSelectedItemPosition()), mfragment.tv_time.getText().toString(),
                                    mfragment.et_category.getText().toString(), mfragment.et_jgjg.getText().toString(), mfragment.et_fxdj.getText().toString(), mfragment.et_Telephone.getText().toString(), mfragment.et_phone.getText().toString());
                        } else if (fragment instanceof DrugSafteFragment) { //药品
                            DrugSafteFragment mfragment = (DrugSafteFragment) fragment;
                            if (!mfragment.checkParams()) {
                                return;
                            }
                            requestBody = RequestParamUtils.thingInsertInfo_drug(thingPositionId, String.valueOf(subMenu.getThingId()), streetId, communityId, String.valueOf(userInfo.getGridId()), String.valueOf(lat), String.valueOf(lng), uploadPhotoList, fileList,
                                    mfragment.et_companyName.getText().toString(), mfragment.et_register_name.getText().toString(), mfragment.et_farenName.getText().toString(), mfragment.et_yaoshi.getText().toString(),
                                    mfragment.et_xukezhanghao.getText().toString(), mfragment.et_xukezhengTime.getText().toString(), mfragment.et_youxiaoqiTime.getText().toString(), mfragment.et_jingyinfanshi.getText().toString(),
                                    mfragment.et_jingyinfanwei.getText().toString(), mfragment.et_Telephone.getText().toString(), mfragment.et_phone.getText().toString());

                        } else if ("森林防火".equals(subMenu.getName())) {
                            if (TextUtils.isEmpty(senl_et_name.getText().toString())) {
                                showMessage("请输入点位名称");
                                return;
                            }
                            requestBody = RequestParamUtils.thingInsertInfo_senl(thingPositionId, String.valueOf(subMenu.getThingId()), streetId, communityId, String.valueOf(userInfo.getGridId()), String.valueOf(lat), String.valueOf(lng), uploadPhotoList, fileList,
                                    senl_et_name.getText().toString().trim());

                        } else if (fragment instanceof TyphoonFloodFragment) { //防台防汛
                            TyphoonFloodFragment mfragment = (TyphoonFloodFragment) fragment;
                            if (!mfragment.checkParams()) {
                                return;
                            }
                            requestBody = RequestParamUtils.thingInsertInfo_ftfx(thingPositionId, String.valueOf(subMenu.getThingId()), streetId, communityId, String.valueOf(userInfo.getGridId()), String.valueOf(lat), String.valueOf(lng), uploadPhotoList, fileList,
                                    mfragment.checkList.get(mfragment.spinner_category.getSelectedItemPosition()), mfragment.et_state.getText().toString(), mfragment.et_address.getText().toString(), mfragment.et_danweiName.getText().toString(),
                                    mfragment.et_farenName.getText().toString(), mfragment.et_leading_TlPhone.getText().toString(), mfragment.et_leading_phone.getText().toString(), mfragment.et_jiedaozerenName.getText().toString(),
                                    mfragment.jiedaozerenMobile.getText().toString());

                        } else if ("冬季除雪".equals(subMenu.getName())) {
                            if (TextUtils.isEmpty(dongjichuxue_et_address.getText().toString())) {
                                showMessage("请输入具体地址");
                                return;
                            }
                            requestBody = RequestParamUtils.thingInsertInfo_dongjichuxue(thingPositionId, String.valueOf(subMenu.getThingId()), streetId, communityId, String.valueOf(userInfo.getGridId()), String.valueOf(lat), String.valueOf(lng), uploadPhotoList, fileList,
                                    dongjichuxue_et_address.getText().toString().trim(), checkList.get(dongjichuxue_et_isPodao.getSelectedItemPosition()));

                        } else if ("文明祭祀".equals(subMenu.getName())) {
                            if (TextUtils.isEmpty(jisi_et_address.getText().toString())) {
                                showMessage("请输入祭祀地点");
                                return;
                            }
                            requestBody = RequestParamUtils.thingInsertInfo_jisi(thingPositionId, String.valueOf(subMenu.getThingId()), streetId, communityId, String.valueOf(userInfo.getGridId()), String.valueOf(lat), String.valueOf(lng), uploadPhotoList, fileList,
                                    jisi_et_address.getText().toString().trim(), jisi_farenName.getText().toString().trim(), jisi_phone.getText().toString().trim(), jisi_zerenquRemark.getText().toString().trim());

                        } else if ("网吧".equals(subMenu.getName())) {
                            if (TextUtils.isEmpty(et_wangba_name.getText().toString())) {
                                showMessage("请输入名称");
                                return;
                            } else if (TextUtils.isEmpty(et_wangba_adress.getText().toString())) {
                                showMessage("请输入地址");
                                return;
                            }
                            requestBody = RequestParamUtils.thingInsertInfo_wangba(thingPositionId, String.valueOf(subMenu.getThingId()), streetId, communityId, String.valueOf(userInfo.getGridId()), String.valueOf(lat), String.valueOf(lng), uploadPhotoList, fileList,
                                    et_wangba_name.getText().toString().trim(), et_wangba_adress.getText().toString().trim(), wangba_farenName.getText().toString().trim(), wangba__phone.getText().toString().trim());

                        } else if (fragment instanceof CulturalRelicFragment) { //文物保护单位
                            CulturalRelicFragment mfragment = (CulturalRelicFragment) fragment;
                            if (!mfragment.checkParams()) {
                                return;
                            }
                            requestBody = RequestParamUtils.thingInsertInfo_wenwu(thingPositionId, String.valueOf(subMenu.getThingId()), streetId, communityId, String.valueOf(userInfo.getGridId()), String.valueOf(lat), String.valueOf(lng), uploadPhotoList, fileList,
                                    mfragment.et_name.getText().toString().trim(), mfragment.et_danweiName.getText().toString().trim(), mfragment.et_farenName.getText().toString().trim(),
                                    mfragment.et_chanquanDanweiName.getText().toString().trim(), mfragment.et_phone.getText().toString().trim(), mfragment.et_guanlishiyongDanweiName.getText().toString().trim(),
                                    mfragment.et_guanlishiyongFarenName.getText().toString().trim(), mfragment.et_guanlishiyongLianxiName.getText().toString().trim(), mfragment.et_guanlishiyongContact.getText().toString().trim());

                        } else if ("演出场所".equals(subMenu.getName()) || "游艺娱乐".equals(subMenu.getName()) || "演艺娱乐".equals(subMenu.getName()) || "娱乐场所".equals(subMenu.getName())) {
                            if (TextUtils.isEmpty(et_yanchu_name.getText().toString())) {
                                showMessage("请输入名称");
                                return;
                            } else if (TextUtils.isEmpty(et_yanchu_adress.getText().toString())) {
                                showMessage("请输入地址");
                                return;
                            } else if (TextUtils.isEmpty(jingyingzheName.getText().toString())) {
                                showMessage("请输入经营者名称");
                                return;
                            }
                            requestBody = RequestParamUtils.thingInsertInfo_yanchu(thingPositionId, String.valueOf(subMenu.getThingId()), streetId, communityId, String.valueOf(userInfo.getGridId()), String.valueOf(lat), String.valueOf(lng), uploadPhotoList, fileList,
                                    et_yanchu_name.getText().toString().trim(), et_yanchu_adress.getText().toString().trim(), jingyingzheName.getText().toString().trim(), yanchu__phone.getText().toString().trim());

                        }

                        if (requestBody != null) {
                            if (thingPositionInfo == null) {
                                mPresenter.insertInfo(requestBody);
                            } else {
                                mPresenter.updateInfo(requestBody);
                            }

                        }
                    }

                }
                break;
        }
    }


    /**
     * 头像选择 PictureSelector
     */
    private void pictureSelector(int requestCode) {
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
                .forResult(requestCode);
    }

    private boolean checkParams() {
        if(mStreetList.size()==0 || spinner_street.getSelectedItemPosition()<0){
            showMessage("请选择街道");
            return false;
        }else if(mCommunityList.size()==0 || spinner_community.getSelectedItemPosition()<0){
            showMessage("请选择社区");
            return false;
        }else if (lat == 0 || lng == 0) {
            showMessage("请定位位置");
            return false;
        } else {
            return true;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            switch (requestCode) {
                case phot_requestCode:  //上传图片
                    for (LocalMedia media : selectList) {
                        // 1.media.getPath(); 为原图path
                        // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                        // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                        String compressedPath = media.getCompressPath();
                        UploadFile uploadPhoto1 = new UploadFile();
                        uploadPhoto1.setFileName(compressedPath);
                        String size1 = FileSizeUtil.getAutoFileOrFilesSize(uploadPhoto1.getFileName());
                        uploadPhoto1.setFileSize(size1);
                        uploadPhotoList.add(uploadPhoto1);
                        photoAdapter.notifyDataSetChanged();
                        if (photoAdapter.getItemCount() != 0) {
                            tv_photo_list.setVisibility(View.GONE);
                        } else {
                            tv_photo_list.setVisibility(View.VISIBLE);
                        }

                        if (mPresenter != null) {
                            mPresenter.uploadPhotoFile(compressedPath);
                        }
                    }
                    break;
                case file_requestCode:  //上传检查记录
                    for (LocalMedia media : selectList) {
                        // 1.media.getPath(); 为原图path
                        // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                        // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                        String compressedPath = media.getCompressPath();
                        UploadFile uploadPhoto1 = new UploadFile();
                        uploadPhoto1.setFileName(compressedPath);
                        String size1 = FileSizeUtil.getAutoFileOrFilesSize(uploadPhoto1.getFileName());
                        uploadPhoto1.setFileSize(size1);
                        fileList.add(uploadPhoto1);
                        fileAdapter.notifyDataSetChanged();
                        if (fileAdapter.getItemCount() != 0) {
                            tv_video_list.setVisibility(View.GONE);
                        } else {
                            tv_video_list.setVisibility(View.VISIBLE);
                        }
                        if (mPresenter != null) {
                            mPresenter.uploadFile(compressedPath);
                        }
                    }
                    break;
            }
        } else if (requestCode == Constant.MAP_REQUEST_CODE && resultCode == Constant.MAP_REQUEST_CODE) {
            if (data != null) {
                lng = data.getDoubleExtra("lng", 0);
                lat = data.getDoubleExtra("lat", 0);

                tvLocationLatitude.setText(String.valueOf(lat));
                tvLocationLongitude.setText(String.valueOf(lng));
            }
        }
    }

    private GPSUtils.LocationListener locationListener = new GPSUtils.LocationListener() {
        @Override
        public void onLocationChanged(CmccLocation cmccLocation) {
            if (SpecialCollectionActivity.this.isFinishing()) return;
            if (myHandler != null) {
                myHandler.removeMessages(1);
            }
            hideLoading();
            if (cmccLocation != null) {
                lat = cmccLocation.getLatitude();
                lng = cmccLocation.getLongitude();
                if (lat > 0 && lng > 0) {
                    tvLocationLatitude.setText(String.valueOf(lat));
                    tvLocationLongitude.setText(String.valueOf(lng));
                    Intent intent = new Intent(SpecialCollectionActivity.this, MapActivity.class);
                    intent.putExtra("lat", lat);
                    intent.putExtra("lng", lng);
                    SpecialCollectionActivity.this.startActivityForResult(intent, Constant.MAP_REQUEST_CODE);
                } else {
                    showNormalDialog();
                }
            } else {
                showNormalDialog();
            }

            GPSUtils.getInstance().removeLocationListener(locationListener);
        }
    };

    private static class MyHandler extends Handler {
        private final WeakReference<SpecialCollectionActivity> weakTrainModelActivity;

        public MyHandler(SpecialCollectionActivity activity) {
            weakTrainModelActivity = new WeakReference<SpecialCollectionActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SpecialCollectionActivity weakReference;
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

    private void showNormalDialog() {
        if (this.isFinishing()) return;
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(this);
        normalDialog.setTitle("定位");
        normalDialog.setMessage("手机定位失败，获得中心点坐标");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(SpecialCollectionActivity.this, MapActivity.class);
                        intent.putExtra("lat", lat);
                        intent.putExtra("lng", lng);
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        GPSUtils.getInstance().removeLocationListener(locationListener);
        if (myHandler != null) {
            myHandler.removeCallbacksAndMessages(null);
        }
    }
}
