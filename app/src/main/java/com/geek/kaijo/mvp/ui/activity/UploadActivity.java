package com.geek.kaijo.mvp.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.geek.kaijo.R;
import com.geek.kaijo.Utils.BitmapUtil;
import com.geek.kaijo.Utils.FileSizeUtil;
import com.geek.kaijo.Utils.PermissionUtils;
import com.geek.kaijo.Utils.PictureHelper;
import com.geek.kaijo.app.AppLifecyclesImpl;
import com.geek.kaijo.app.MyApplication;
import com.geek.kaijo.di.component.DaggerUploadComponent;
import com.geek.kaijo.di.module.UploadModule;
import com.geek.kaijo.mvp.contract.UploadContract;
import com.geek.kaijo.mvp.model.entity.CaseInfo;
import com.geek.kaijo.mvp.model.entity.UploadCaseFile;
import com.geek.kaijo.mvp.model.entity.UploadFile;
import com.geek.kaijo.mvp.presenter.UploadPresenter;
import com.geek.kaijo.mvp.ui.adapter.UploadPhotoAdapter;
import com.geek.kaijo.mvp.ui.adapter.UploadVideoAdapter;
import com.geek.kaijo.view.CommProgressDailog;
import com.geek.kaijo.view.SelectDialog;
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
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import dao.CaseInfoDao;
import dao.DaoMaster;
import dao.DaoSession;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class UploadActivity extends BaseActivity<UploadPresenter> implements UploadContract.View {
    public static final int imag_width = 200;
    public static final int imag_height = 250;
    private static final int REQUEST_CODE_ALBUM = 1; //打开相册4.4以前
    private static final int REQUEST_CODE_ALBUM_PHOTO = 2; //打开相册4.4以后
    private static final int RESULT_CAMERA = 3;  //打开相机
    public static final int RESULT_PHOTO = 4;  //跳转到图片显示界面
    public static final int RESULT_VIDEO = 5;  //视频拍摄,整改前
    public static final int RESULT_VIDEO_later = 6;  //视频拍摄,整改后S

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.recyclerView_later)
    RecyclerView recyclerView_later;

    @BindView(R.id.tv_photo_list)
    TextView tv_photo_list;
    @BindView(R.id.tv_photo_list_later)
    TextView tv_photo_list_later;
    @BindView(R.id.tv_video_list)
    TextView tv_video_list;
    @BindView(R.id.tv_video_list_later)
    TextView tv_video_list_later;

    private RxPermissions rxPermissions;
    private UploadPhotoAdapter adapter; //照片整改前
    private UploadPhotoAdapter adapter_later;//照片整改后
    private UploadVideoAdapter adapter_video;//视频整改前
    private UploadVideoAdapter adapter_video_later;//视频整改后
    private List<UploadFile> uploadPhotoList;  //整改前
    private List<UploadFile> uploadPhotoList_later;  //整改后
    private boolean isBeforePhoto; //照片整改前

    private int isWhich = 0;//1 视频整改前  2 视频整改后   3.图片整改前  4，图片整改后
    private List<UploadFile> videoList;  //视频拍摄  整改前
    private List<UploadFile> videoList_later;  //视频拍摄  整改后

    //视频

    @BindView(R.id.recyclerView_video)
    RecyclerView recyclerView_video;
    @BindView(R.id.recyclerView_video_later)
    RecyclerView recyclerView_video_later;

    private int caseId;
    private int entry_type;
    private CaseInfo caseInfo;
    private boolean pauseCase; //暂存


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerUploadComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .uploadModule(new UploadModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_upload_test; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        entry_type = getIntent().getIntExtra("entry_type", 0);
        pauseCase = getIntent().getBooleanExtra("pauseCase",false);

        //屏蔽7.0中 拍照使用 Uri.fromFile爆出的FileUriExposureException
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= 24) {
            builder.detectFileUriExposure();
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.recyclerview_divider));
        recyclerView.addItemDecoration(divider);
        recyclerView.setLayoutManager(layoutManager);
        LinearLayoutManager layoutManager_later = new LinearLayoutManager(this);
        recyclerView_later.addItemDecoration(divider);
        recyclerView_later.setLayoutManager(layoutManager_later);

        LinearLayoutManager layoutManager_video = new LinearLayoutManager(this);
        recyclerView_video.addItemDecoration(divider);
        recyclerView_video.setLayoutManager(layoutManager_video);
        LinearLayoutManager layoutManager_video_later = new LinearLayoutManager(this);
        recyclerView_video_later.addItemDecoration(divider);
        recyclerView_video_later.setLayoutManager(layoutManager_video_later);

        caseInfo = (CaseInfo) getIntent().getSerializableExtra("caseInfo");
        if (caseInfo != null && !TextUtils.isEmpty(caseInfo.getCaseId())) {
            caseId = Integer.parseInt(caseInfo.getCaseId());
            String fileListGson = caseInfo.getFileListGson();
            if(!TextUtils.isEmpty(fileListGson)){
                Gson gson = new Gson();
                List<UploadCaseFile> uploadCaseFileList = gson.fromJson(fileListGson, new TypeToken<List<UploadCaseFile>>() {}.getType());
                if(uploadCaseFileList!=null && uploadCaseFileList.size()>0){
                    for(int i=0;i<uploadCaseFileList.size();i++){
                        if(uploadCaseFileList.get(i).getFileType()==0){ //照片
                            if(uploadCaseFileList.get(i).getWhenType()==1){ //整改前
                                if(uploadPhotoList==null){
                                    uploadPhotoList = new ArrayList<>();
                                }
                                UploadFile uploadFile = new UploadFile();
                                uploadFile.whenType = uploadCaseFileList.get(i).getWhenType();
                                uploadFile.caseProcessRecordId = uploadCaseFileList.get(i).getCaseProcessRecordId();
                                uploadFile.fileType = uploadCaseFileList.get(i).getFileType();
                                uploadFile.setFileName(uploadCaseFileList.get(i).getFileName());
                                uploadFile.setUrl(uploadCaseFileList.get(i).getUrl());
                                uploadPhotoList.add(uploadFile);
                            }else if(uploadCaseFileList.get(i).getWhenType()==2){ //整改后
                                if(uploadPhotoList_later==null){
                                    uploadPhotoList_later = new ArrayList<>();
                                }
                                UploadFile uploadFile = new UploadFile();
                                uploadFile.whenType = uploadCaseFileList.get(i).getWhenType();
                                uploadFile.caseProcessRecordId = uploadCaseFileList.get(i).getCaseProcessRecordId();
                                uploadFile.fileType = uploadCaseFileList.get(i).getFileType();
                                uploadFile.setUrl(uploadCaseFileList.get(i).getUrl());
                                uploadFile.setFileName(uploadCaseFileList.get(i).getFileName());
                                uploadPhotoList_later.add(uploadFile);
                            }
                        }else if(uploadCaseFileList.get(i).getFileType()==1){ //视频
                            if(uploadCaseFileList.get(i).getWhenType()==1){ //整改前
                                if(videoList==null){
                                    videoList = new ArrayList<>();
                                }
                                UploadFile uploadFile = new UploadFile();
                                uploadFile.whenType = uploadCaseFileList.get(i).getWhenType();
                                uploadFile.caseProcessRecordId = uploadCaseFileList.get(i).getCaseProcessRecordId();
                                uploadFile.fileType = uploadCaseFileList.get(i).getFileType();
                                uploadFile.setUrl(uploadCaseFileList.get(i).getUrl());
                                uploadFile.setFileName(uploadCaseFileList.get(i).getFileName());
                                videoList.add(uploadFile);
                            }else if(uploadCaseFileList.get(i).getWhenType()==2){ //整改后
                                if(videoList_later==null){
                                    videoList_later = new ArrayList<>();
                                }
                                UploadFile uploadFile = new UploadFile();
                                uploadFile.whenType = uploadCaseFileList.get(i).getWhenType();
                                uploadFile.caseProcessRecordId = uploadCaseFileList.get(i).getCaseProcessRecordId();
                                uploadFile.fileType = uploadCaseFileList.get(i).getFileType();
                                uploadFile.setUrl(uploadCaseFileList.get(i).getUrl());
                                uploadFile.setFileName(uploadCaseFileList.get(i).getFileName());
                                videoList_later.add(uploadFile);
                            }
                        }
                    }
                    recyclerViewAdapter();
                    recyclerViewAdapter_later();
                    recyclerViewAdapter_video();
                    recyclerViewAdapter_video_later();
                }
            }
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

    //整改前
    private void recyclerViewAdapter() {
        if (adapter == null) {
            adapter = new UploadPhotoAdapter(this, uploadPhotoList);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        adapter.setOnItemOnClilcklisten(new UploadPhotoAdapter.OnItemOnClicklisten() {
            @Override
            public void onItemDeleteClick(View v, int position) {
                if (uploadPhotoList.get(position).getIsSuccess() == 1) { //上传成功  显示删除
                    uploadPhotoList.remove(position);
                    adapter.notifyDataSetChanged();
                } else if (uploadPhotoList.get(position).getIsSuccess() == 0) {

                } else { //上传失败 重新上传
                    if (mPresenter != null) {
                        mPresenter.uploadFile(uploadPhotoList.get(position).getFileName());
                    }
                }
                if (adapter.getItemCount() != 0) {
                    tv_photo_list.setVisibility(View.GONE);
                } else {
                    tv_photo_list.setVisibility(View.VISIBLE);
                }

            }
        });
        if (adapter.getItemCount() != 0) {
            tv_photo_list.setVisibility(View.GONE);
        } else {
            tv_photo_list.setVisibility(View.VISIBLE);
        }

    }

    //整改后
    private void recyclerViewAdapter_later() {
        if (adapter_later == null) {
            adapter_later = new UploadPhotoAdapter(this, uploadPhotoList_later);
            recyclerView_later.setAdapter(adapter_later);
        } else {
            adapter_later.notifyDataSetChanged();
        }
        adapter_later.setOnItemOnClilcklisten(new UploadPhotoAdapter.OnItemOnClicklisten() {
            @Override
            public void onItemDeleteClick(View v, int position) {
                if (uploadPhotoList_later.get(position).getIsSuccess() == 1) { //上传成功  显示删除
                    uploadPhotoList_later.remove(position);
                    adapter_later.notifyDataSetChanged();
                } else if (uploadPhotoList_later.get(position).getIsSuccess() == 0) {

                } else { //上传失败 重新上传
                    if (mPresenter != null) {
                        mPresenter.uploadFile(uploadPhotoList_later.get(position).getFileName());
                    }
                }
                if (adapter.getItemCount() != 0) {
                    tv_photo_list.setVisibility(View.GONE);
                } else {
                    tv_photo_list.setVisibility(View.VISIBLE);
                }

            }
        });
        if (adapter_later.getItemCount() != 0) {
            tv_photo_list_later.setVisibility(View.GONE);
        } else {
            tv_photo_list_later.setVisibility(View.VISIBLE);
        }

    }

    //视频整改前
    private void recyclerViewAdapter_video() {
        if (adapter_video == null) {
            adapter_video = new UploadVideoAdapter(this, videoList);
            recyclerView_video.setAdapter(adapter_video);
        } else {
            adapter_video.notifyDataSetChanged();
        }
        adapter_video.setOnItemOnClilcklisten(new UploadVideoAdapter.OnItemOnClicklisten() {
            @Override
            public void onItemDeleteClick(View v, int position) {
                if (videoList.get(position).getIsSuccess() == 1) { //上传成功  显示删除
                    videoList.remove(position);
                    adapter_video.notifyDataSetChanged();
                } else if (videoList.get(position).getIsSuccess() == 0) {

                } else { //上传失败 重新上传
                    if (mPresenter != null) {
                        mPresenter.uploadFile(videoList.get(position).getFileName());
                    }
                }
                if (adapter.getItemCount() != 0) {
                    tv_photo_list.setVisibility(View.GONE);
                } else {
                    tv_photo_list.setVisibility(View.VISIBLE);
                }

            }
        });
        if (adapter_video.getItemCount() != 0) {
            tv_video_list.setVisibility(View.GONE);
        } else {
            tv_video_list.setVisibility(View.VISIBLE);
        }

    }

    //视频整改后
    private void recyclerViewAdapter_video_later() {
        if (adapter_video_later == null) {
            adapter_video_later = new UploadVideoAdapter(this, videoList_later);
            recyclerView_video_later.setAdapter(adapter_video_later);
        } else {
            adapter_video_later.notifyDataSetChanged();
        }
        adapter_video_later.setOnItemOnClilcklisten(new UploadVideoAdapter.OnItemOnClicklisten() {
            @Override
            public void onItemDeleteClick(View v, int position) {
                if (videoList_later.get(position).getIsSuccess() == 1) { //上传成功  显示删除
                    videoList_later.remove(position);
                    adapter_video_later.notifyDataSetChanged();
                } else if (videoList_later.get(position).getIsSuccess() == 0) {

                } else { //上传失败 重新上传
                    if (mPresenter != null) {
                        mPresenter.uploadFile(videoList_later.get(position).getFileName());
                    }
                }
                if (adapter.getItemCount() != 0) {
                    tv_photo_list.setVisibility(View.GONE);
                } else {
                    tv_photo_list.setVisibility(View.VISIBLE);
                }

            }
        });
        if (adapter_video_later.getItemCount() != 0) {
            tv_video_list_later.setVisibility(View.GONE);
        } else {
            tv_video_list_later.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void showLoading() {
        getDailog().showProgressDailog(this, null);
    }

    CommProgressDailog dialogUtils;

    private CommProgressDailog getDailog() {
        if (dialogUtils == null) {
            dialogUtils = new CommProgressDailog();
        }
        return dialogUtils;
    }

    @Override
    public void hideLoading() {
        getDailog().cancelProgressDailog();
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
        Toast.makeText(this, "上报案件成功", Toast.LENGTH_LONG).show();
        setResult(1);
        finish();
    }

    @OnClick({R.id.btn_image, R.id.btn_image_later, R.id.btn_video, R.id.btn_video_later, R.id.tv_ok, R.id.tv_previous_step, R.id.tv_cancel, R.id.tv_pause_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_image:
                isBeforePhoto = true;
                isWhich = 3;
                checkPermissionAndAction();
                break;
            case R.id.btn_image_later: //整改后
                isBeforePhoto = false;
                isWhich = 4;
                checkPermissionAndAction();
                break;
            case R.id.btn_video:
//                startVideo();
//                intent = new Intent(this, VideoRecordActivity.class);
//                startActivityForResult(intent, RESULT_VIDEO);
                isWhich = 1;
                checkPermissionAndVideo();
                break;
            case R.id.btn_video_later:
//                startVideo();
//                intent = new Intent(this, VideoRecordActivity.class);
//                startActivityForResult(intent, RESULT_VIDEO_later);
                isWhich = 2;
                checkPermissionAndVideo();
                break;
            case R.id.tv_ok:
                if (mPresenter != null) {
                    mPresenter.addCaseAttach(getUploadCaseFile());
                }
                break;
            case R.id.tv_previous_step:
                if(pauseCase){ //从暂存跳转过来
                    Intent intent = new Intent(this,ReportActivity.class);
                    intent.putExtra("caseInfo",caseInfo);
                    startActivity(intent);
                }
                finish();
                break;
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_pause_save:
                List<UploadCaseFile> uploadCaseFileList = getUploadCaseFile();
                if(uploadCaseFileList!=null){
                    Gson gson = new Gson();
                    String uploadCaseFileGson = gson.toJsonTree(uploadCaseFileList).getAsJsonArray().toString();
                    caseInfo.setFileListGson(uploadCaseFileGson);
                }
                DaoSession daoSession1 = MyApplication.get().getDaoSession();
                CaseInfoDao caseInfoDao = daoSession1.getCaseInfoDao();
                caseInfoDao.insertOrReplaceInTx(caseInfo);
//                Intent intent3 = new Intent(UploadActivity.this, TemporaryActivity.class);
//                launchActivity(intent3);
                setResult(1);
                this.finish();
                break;
        }
    }

    private List<UploadCaseFile> getUploadCaseFile(){
        List<UploadCaseFile> caseFileList = new ArrayList<>();

        int handleType = 1;//直接处理传1 ，非直接处理传2
//                int whenType = 1;//直接处理( 整改前的写1  整改后写2),  非直接处理 whenType 1
        int caseProcessRecordID = 19;// 直接处理 caseProcessRecordID  19,  非直接处理 caseProcessRecordID  11
        if (entry_type == 0) {
            //自行处理
            handleType = 1;
            caseProcessRecordID = 19;
        } else if (entry_type == 1) {
            //非自行处理
            handleType = 2;
            caseProcessRecordID = 11;
        }

        if (uploadPhotoList != null) { //照片整改前
            for (int i = 0; i < uploadPhotoList.size(); i++) {
                UploadCaseFile caseFile = new UploadCaseFile();
                caseFile.setCaseId(caseId);
                caseFile.setUrl(uploadPhotoList.get(i).getFileRelativePath());
                caseFile.setCaseProcessRecordId(caseProcessRecordID);
                caseFile.setFileType(0); //照片
                caseFile.setWhenType(1); //整改前
                caseFile.setHandleType(handleType);
//                caseFile.setFileName(uploadPhotoList.get(i).getFileName());
                caseFileList.add(caseFile);
            }
        }
        if (uploadPhotoList_later != null) { //照片整改前
            for (int i = 0; i < uploadPhotoList_later.size(); i++) {
                UploadCaseFile caseFile = new UploadCaseFile();
                caseFile.setCaseId(caseId);
                caseFile.setUrl(uploadPhotoList_later.get(i).getFileRelativePath());
                caseFile.setCaseProcessRecordId(caseProcessRecordID);
                caseFile.setFileType(0); //照片
                caseFile.setWhenType(2);//整改后
                caseFile.setHandleType(handleType);
//                caseFile.setFileName(uploadPhotoList_later.get(i).getFileName());
                caseFileList.add(caseFile);
            }
        }
        if (videoList != null) { //照片整改前
            for (int i = 0; i < videoList.size(); i++) {
                UploadCaseFile caseFile = new UploadCaseFile();
                caseFile.setCaseId(caseId);
                caseFile.setUrl(videoList.get(i).getFileRelativePath());
                caseFile.setCaseProcessRecordId(0);
                caseFile.setFileType(1); //视频
                caseFile.setWhenType(1);//整改后
                caseFile.setHandleType(handleType);
//                caseFile.setFileName(videoList.get(i).getFileName());
                caseFileList.add(caseFile);
            }
        }
        if (videoList_later != null) { //照片整改前
            for (int i = 0; i < videoList_later.size(); i++) {
                UploadCaseFile caseFile = new UploadCaseFile();
                caseFile.setCaseId(caseId);
                caseFile.setUrl(videoList_later.get(i).getFileRelativePath());
                caseFile.setCaseProcessRecordId(caseProcessRecordID);
                caseFile.setFileType(1); //视频
                caseFile.setWhenType(2);//整改后
                caseFile.setHandleType(handleType);
//                caseFile.setFileName(videoList_later.get(i).getFileName());
                caseFileList.add(caseFile);
            }
        }
        return caseFileList;
    }

    /**
     * 视频拍摄
     */
    private void startVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        File file = new File(Environment.getExternalStorageDirectory(), "haha.3gp");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        //设置保存视频文件的质量
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, RESULT_VIDEO);
    }

    /**
     * 视频选择
     */
    private void videoSelector() {
        PictureSelector.create(this)
                .openCamera(PictureMimeType.ofVideo())
                .previewVideo(true)
                .videoQuality(0)
                .videoMaxSecond(60)
                .videoMinSecond(1)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    private void showDialog() {

        pictureSelector();

//        List<String> names = new ArrayList<>();
//        names.add("拍照");
//        names.add("相册");
//        showDialog(new SelectDialog.SelectDialogListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent;
//                switch (position) {
//                    case 0: // 直接调起相机
//                        //启动相机程序
//                        openCamera();
//                        break;
//                    case 1:
////                        intent = new Intent(Intent.ACTION_PICK);
////                        intent.setType("image/*");
////                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
////                            startActivityForResult(intent, REQUEST_CODE_ALBUM_PHOTO);
////                        } else {
////                            startActivityForResult(intent, REQUEST_CODE_ALBUM);
////                        }
//
//                        intent = new Intent(UploadActivity.this, PhotoActivityActivity.class);
//                        startActivityForResult(intent, RESULT_PHOTO);
//                        break;
//                    default:
//                        break;
//                }
//            }
//        }, names);
    }

    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {
        SelectDialog dialog = new SelectDialog(this, R.style.transparentFrameWindowStyle, listener, names);
        if (!this.isFinishing()) {
            dialog.show();
        }
        return dialog;
    }


    private Uri imageUri;

    private void openCamera() {
//        File outputImage = new File(Environment.getExternalStorageDirectory(), "output_image.jpg");
        File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
                outputImage.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageUri = Uri.fromFile(outputImage);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, RESULT_CAMERA);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    for (LocalMedia media : selectList) {
                        // 1.media.getPath(); 为原图path
                        // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                        // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                        String path = media.getPath();
                        String compressedPath = media.getCompressPath();
//                        showMessage("picture==" + compressedPath);
                        List<UploadFile> selectPhotoList = new ArrayList<>();
                        switch (isWhich) {
                            case 1:
                                UploadFile uploadFile = new UploadFile();
                                uploadFile.setFileName(path);
                                String size1 = FileSizeUtil.getAutoFileOrFilesSize(uploadFile.getFileName());
                                uploadFile.setFileSize(size1);
                                if (videoList == null) {
                                    videoList = new ArrayList<>();
                                }
                                videoList.add(uploadFile);
                                recyclerViewAdapter_video();
                                if (mPresenter != null) {
                                    mPresenter.uploadFile(path);
                                }
                                break;
                            case 2:
                                UploadFile uploadFile2 = new UploadFile();
                                uploadFile2.setFileName(path);
                                String size = FileSizeUtil.getAutoFileOrFilesSize(uploadFile2.getFileName());
                                uploadFile2.setFileSize(size);
                                if (videoList_later == null) {
                                    videoList_later = new ArrayList<>();
                                }
                                videoList_later.add(uploadFile2);
                                recyclerViewAdapter_video_later();
                                if (mPresenter != null) {
                                    mPresenter.uploadFile(path);
                                }
                                break;
                            case 3:
                                UploadFile uploadFile3 = new UploadFile();
                                uploadFile3.setFileName(compressedPath);
                                String size3 = FileSizeUtil.getAutoFileOrFilesSize(uploadFile3.getFileName());
                                uploadFile3.setFileSize(size3);
                                selectPhotoList.add(uploadFile3);
                                compressImageUploadList(selectPhotoList, uploadPhotoList);
                                break;
                            case 4:
                                UploadFile uploadFile4 = new UploadFile();
                                uploadFile4.setFileName(compressedPath);
                                String size4 = FileSizeUtil.getAutoFileOrFilesSize(uploadFile4.getFileName());
                                uploadFile4.setFileSize(size4);
                                selectPhotoList.add(uploadFile4);
                                compressImageUploadList(selectPhotoList, uploadPhotoList_later);
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
//        switch (requestCode) {
//            case REQUEST_CODE_ALBUM:  //4.4 以前
//                if (data == null) return;
//
//                if (data.getData() == null) return;
//
//                String path = data.getData().getPath();
//                compressImageUpload(path);
//                break;
//            case REQUEST_CODE_ALBUM_PHOTO: //4.4以后
//                if (data == null) return;
//                Uri selectedImage = data.getData();
//                if (selectedImage != null) {
//                    String picturePath = PictureHelper.getPath(this, selectedImage);
//                    compressImageUpload(picturePath);
//                }
//                break;
//            case RESULT_CAMERA: //相机
//                if (imageUri != null) {
//                    compressImageUpload(imageUri.getPath());
//                }
//
////                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
////                    img_.setImageBitmap(bitmap);
//                break;
//            case RESULT_PHOTO: //选择多张图片返回
//                if (data != null && data.hasExtra("selectPhotoList")) {
//                    List<UploadFile> selectPhotoList = data.getParcelableArrayListExtra("selectPhotoList");
//                    if (selectPhotoList != null) {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                        builder.setTitle("图片上传");
//                        builder.setMessage("上传将会产生流量");
//                        builder.setNegativeButton("取消", null);
//                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int position) {
////                                mPresenter.uploadFileList(uploadPhotoList);
//                                if (isBeforePhoto) {  //整改前
//                                    compressImageUploadList(selectPhotoList, uploadPhotoList);
//                                } else {//整改后
//                                    compressImageUploadList(selectPhotoList, uploadPhotoList_later);
//                                }
//                            }
//                        });
//                        builder.show();
//
//                    }
//                }
//            case RESULT_VIDEO: //视频录制
//                if (data != null) {
////                   Uri uri =  data.getData();
////                   if(uri!=null && !TextUtils.isEmpty(uri.getPath())){
////                       mPresenter.uploadFile(uri.getPath());
////                   }
//
//                    UploadFile uploadFile = data.getParcelableExtra("UploadFile");
//                    if (uploadFile != null) {
//                        String size = FileSizeUtil.getAutoFileOrFilesSize(uploadFile.getFileName());
//                        uploadFile.setFileSize(size);
//                        if (videoList == null) {
//                            videoList = new ArrayList<>();
//                        }
//                        videoList.add(uploadFile);
//                        recyclerViewAdapter_video();
//                    }
//                }
//                break;
//            case RESULT_VIDEO_later:
//
//                if (data != null) {
//                    UploadFile uploadFile = data.getParcelableExtra("UploadFile");
//                    if (uploadFile != null) {
//                        String size = FileSizeUtil.getAutoFileOrFilesSize(uploadFile.getFileName());
//                        uploadFile.setFileSize(size);
//                        if (videoList_later == null) {
//                            videoList_later = new ArrayList<>();
//                        }
//                        videoList_later.add(uploadFile);
//                        recyclerViewAdapter_video_later();
//                    }
//                }
//
//                break;
//
//        }
    }

    /**
     * 压缩单张图片并上传
     */
    private void compressImageUpload(String path) {
        if (!TextUtils.isEmpty(path)) {
            path = BitmapUtil.compressImage(path);
            if (!TextUtils.isEmpty(path)) {

                UploadFile uploadPhoto = new UploadFile();
                uploadPhoto.setFileName(imageUri.getPath());
                String size = FileSizeUtil.getAutoFileOrFilesSize(uploadPhoto.getFileName());
                uploadPhoto.setFileSize(size);
                if (isBeforePhoto) {  //整改前
                    if (uploadPhotoList == null) {
                        uploadPhotoList = new ArrayList<>();
                    }
                    uploadPhotoList.add(uploadPhoto);
                    recyclerViewAdapter();
                } else {//整改后
                    if (uploadPhotoList_later == null) {
                        uploadPhotoList_later = new ArrayList<>();
                    }
                    uploadPhotoList_later.add(uploadPhoto);
                    recyclerViewAdapter_later();
                }

                if (mPresenter != null) {
                    mPresenter.uploadFile(path);
                }
            }
        }
    }

    /**
     * 压缩多张图片并上传
     */
    private void compressImageUploadList(List<UploadFile> selectPhotoList, List<UploadFile> uploadList) {
        if (uploadList == null || uploadList.size() == 0) {
            uploadList = selectPhotoList;
            for (int i = 0; i < uploadList.size(); i++) {
                BitmapUtil.compressImage(uploadList.get(i).getFileName()); //压缩
                String size = FileSizeUtil.getAutoFileOrFilesSize(uploadList.get(i).getFileName());
                uploadList.get(i).setFileSize(size);
            }
        } else {
            for (int i = 0; i < selectPhotoList.size(); i++) {
                boolean flag = true;
                for (int j = 0; j < uploadList.size(); j++) {
                    if (selectPhotoList.get(i).getFileName().equals(uploadList.get(j).getFileName())) {  //同一张图片
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    UploadFile uploadPhoto = selectPhotoList.get(i);
                    BitmapUtil.compressImage(selectPhotoList.get(i).getFileName()); //压缩
                    String size = FileSizeUtil.getAutoFileOrFilesSize(uploadPhoto.getFileName());
                    uploadPhoto.setFileSize(size);
                    uploadList.add(uploadPhoto);
                }
            }
        }
        if (isBeforePhoto) {  //整改前
            uploadPhotoList = uploadList;
            recyclerViewAdapter();
        } else {//整改后
            uploadPhotoList_later = uploadList;
            recyclerViewAdapter_later();
        }

        for (int i = 0; i < uploadList.size(); i++) {
            if (!TextUtils.isEmpty(uploadList.get(i).getFileName())) {
//                uploadPhotoList.get(i).setFileName(BitmapUtil.compressImage(uploadPhotoList.get(i).getFileName()));
                if (mPresenter != null) {
                    mPresenter.uploadFile(uploadList.get(i).getFileName());
                }
//                mPresenter.uploadFileList(uploadPhotoList);
            }
        }
    }

    @SuppressLint("CheckResult")
    private void checkPermissionAndAction() {
        if (rxPermissions == null) {
            rxPermissions = new RxPermissions(this);
        }
        rxPermissions.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            showDialog();
//                            showSelectDialog();
//                            Intent intent = new Intent(UploadActivity.this,PhotoActivityActivity.class);
//                            startActivityForResult(intent,RESULT_PHOTO);

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

    @SuppressLint("CheckResult")
    private void checkPermissionAndVideo() {
        if (rxPermissions == null) {
            rxPermissions = new RxPermissions(this);
        }
        rxPermissions.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            videoSelector();

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
        builder.setMessage("上传图片需要文件权限");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PermissionUtils.permissionSkipSetting(UploadActivity.this);
            }
        });
        builder.show();
    }

    @Override
    public void uploadSuccess(UploadFile uploadPhoto) {
        if (uploadPhoto != null) {
            switch (isWhich) {
                case 1:
                    for (int i = 0; i < videoList.size(); i++) {
                        if (videoList.get(i).getFileName().equals(uploadPhoto.getFileName())) {
                            videoList.get(i).setFileDomain(uploadPhoto.getFileDomain());
                            videoList.get(i).setFileRelativePath(uploadPhoto.getFileRelativePath());
                            videoList.get(i).setIsSuccess(uploadPhoto.getIsSuccess());
                            adapter_video.notifyItemChanged(i);
                        }
                    }
                    break;
                case 2:
                    for (int i = 0; i < videoList_later.size(); i++) {
                        if (videoList_later.get(i).getFileName().equals(uploadPhoto.getFileName())) {
                            videoList_later.get(i).setFileDomain(uploadPhoto.getFileDomain());
                            videoList_later.get(i).setFileRelativePath(uploadPhoto.getFileRelativePath());
                            videoList_later.get(i).setIsSuccess(uploadPhoto.getIsSuccess());
                            adapter_video_later.notifyItemChanged(i);
                        }
                    }
                    break;
                case 3:
                    for (int i = 0; i < uploadPhotoList.size(); i++) {
                        if (uploadPhotoList.get(i).getFileName().equals(uploadPhoto.getFileName())) {
                            uploadPhotoList.get(i).setFileDomain(uploadPhoto.getFileDomain());
                            uploadPhotoList.get(i).setFileRelativePath(uploadPhoto.getFileRelativePath());
                            uploadPhotoList.get(i).setIsSuccess(uploadPhoto.getIsSuccess());
                            adapter.notifyItemChanged(i);
                        }
                    }
                    break;
                case 4:
                    for (int i = 0; i < uploadPhotoList_later.size(); i++) {
                        if (uploadPhotoList_later.get(i).getFileName().equals(uploadPhoto.getFileName())) {
                            uploadPhotoList_later.get(i).setFileDomain(uploadPhoto.getFileDomain());
                            uploadPhotoList_later.get(i).setFileRelativePath(uploadPhoto.getFileRelativePath());
                            uploadPhotoList_later.get(i).setIsSuccess(uploadPhoto.getIsSuccess());
                            adapter_later.notifyItemChanged(i);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
