package com.geek.kaijo.mvp.ui.activity.society.safety;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.geek.kaijo.R;
import com.geek.kaijo.Utils.FileSizeUtil;
import com.geek.kaijo.di.component.DaggerSocialProductDangerComponent;
import com.geek.kaijo.di.module.SocialProductDangerModule;
import com.geek.kaijo.mvp.contract.SocialProductDangerContract;
import com.geek.kaijo.mvp.model.entity.UploadFile;
import com.geek.kaijo.mvp.presenter.SocialProductDangerPresenter;
import com.geek.kaijo.mvp.ui.adapter.UploadPhotoAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * 社会治理-安全生产-危化品点位采集
 */
public class SocialProductDangerActivity extends BaseActivity<SocialProductDangerPresenter> implements SocialProductDangerContract.View {

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_toolbar_title_right)
    ImageView tvToolbarTitleRight;
    @BindView(R.id.tv_toolbar_title_right_text)
    TextView tvToolbarTitleRightText;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.spinner_street)
    AppCompatSpinner spinnerStreet;
    @BindView(R.id.spinner_community)
    AppCompatSpinner spinnerCommunity;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.et_other)
    EditText etOther;
    @BindView(R.id.et_sort)
    EditText etSort;
    @BindView(R.id.tv_location_longitude)
    TextView tvLocationLongitude;
    @BindView(R.id.tv_location_latitude)
    TextView tvLocationLatitude;
    @BindView(R.id.btn_location_obtain)
    TextView btnLocationObtain;
    @BindView(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;
    @BindView(R.id.btn_submit_pic)
    TextView btnSubmitPic;//上传图片
    @BindView(R.id.upload_picture_list)
    RecyclerView uploadPictureList;//照片列表
    @BindView(R.id.btn_submit_check_record)
    TextView btnSubmitCheckRecord;//上传检查
    @BindView(R.id.check_record_list)
    RecyclerView checkRecordList;//检查列表
    @BindView(R.id.submit)
    TextView submit;//提交
    @BindView(R.id.cancel)
    TextView cancel;//取消

    private int isWhich = 0;//1 上传图片  2 上传检查记录
    private List<UploadFile> checkPhotoList;
    private List<UploadFile> uploadPhotoList;
    private UploadPhotoAdapter adapter1;
    private UploadPhotoAdapter adapter2;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSocialProductDangerComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .socialProductDangerModule(new SocialProductDangerModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_social_product_danger; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initRefresh();
        //照片列表
        uploadPictureList.setLayoutManager(new LinearLayoutManager(this));
        uploadPictureList.setHasFixedSize(true);

        uploadPhotoList = new ArrayList<>();
        //检查列表
        checkRecordList.setLayoutManager(new LinearLayoutManager(this));
        checkRecordList.setHasFixedSize(true);

        checkPhotoList = new ArrayList<>();


    }

    private void initRefresh() {
        smartRefresh.setEnableLoadMore(false);
        smartRefresh.setOnRefreshListener(refreshLayout -> {

        });
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

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

    /**
     * 头像选择 PictureSelector
     */
    private void pictureSelector() {
        PictureSelector.create(this).openGallery(PictureMimeType.ofImage())
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
                        if (media.isCompressed()) {
                            String compressedPath = media.getCompressPath();
                            Timber.d("picture==" + compressedPath);
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
                                    UploadFile uploadPhoto2 = new UploadFile();
                                    uploadPhoto2.setFileName(compressedPath);
                                    String size2 = FileSizeUtil.getAutoFileOrFilesSize(uploadPhoto2.getFileName());
                                    uploadPhoto2.setFileSize(size2);
                                    checkPhotoList.add(uploadPhoto2);
                                    recyclerViewAdapter2();
                                    if (mPresenter != null) {
                                        mPresenter.uploadFile(compressedPath);
                                    }
                                    break;
                                default:
                                    break;
                            }
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
            uploadPictureList.setAdapter(adapter1);
        } else {
            adapter1.notifyChanged(uploadPhotoList);
        }
        adapter1.setOnItemOnClilcklisten(new UploadPhotoAdapter.OnItemOnClicklisten() {
            @Override
            public void onItemDeleteClick(View v, int position) {
                if (uploadPhotoList.get(position).getIsSuccess() == 1) { //上传成功  显示删除
                    uploadPhotoList.remove(position);
                    adapter1.notifyDataSetChanged();
                }

            }

            @Override
            public void onItemAgainUploadClick(View v, int position) {//上传失败 重新上传
                if (mPresenter != null) {
                    mPresenter.uploadFile(uploadPhotoList.get(position).getFileName());
                    isWhich = 1;
                }
            }
        });
    }

    //检查记录
    private void recyclerViewAdapter2() {
        if (adapter2 == null) {
            adapter2 = new UploadPhotoAdapter(this, checkPhotoList);
            checkRecordList.setAdapter(adapter2);
        } else {
            adapter2.notifyChanged(checkPhotoList);
        }
        adapter2.setOnItemOnClilcklisten(new UploadPhotoAdapter.OnItemOnClicklisten() {
            @Override
            public void onItemDeleteClick(View v, int position) {
                if (checkPhotoList.get(position).getIsSuccess() == 1) { //上传成功  显示删除
                    checkPhotoList.remove(position);
                    adapter2.notifyDataSetChanged();
                }

            }

            @Override
            public void onItemAgainUploadClick(View v, int position) { //上传失败 重新上传
                if (mPresenter != null) {
                    mPresenter.uploadFile(checkPhotoList.get(position).getFileName());
                    isWhich = 2;
                }
            }
        });


    }

    @OnClick({R.id.btn_submit_pic, R.id.btn_submit_check_record, R.id.submit, R.id.cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_submit_pic://上传图片
                isWhich = 1;
                pictureSelector();
                break;
            case R.id.btn_submit_check_record://上传检查记录
                isWhich = 2;
                pictureSelector();
                break;
            case R.id.submit://提交
                break;
            case R.id.cancel://取消
                break;
        }
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
                    for (int i = 0; i < checkPhotoList.size(); i++) {
                        if (checkPhotoList.get(i).getFileName().equals(uploadPhoto.getFileName())) {
                            checkPhotoList.get(i).setFileDomain(uploadPhoto.getFileDomain());
                            checkPhotoList.get(i).setFileRelativePath(uploadPhoto.getFileRelativePath());
                            checkPhotoList.get(i).setIsSuccess(uploadPhoto.getIsSuccess());
                            adapter2.notifyItemChanged(i);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
