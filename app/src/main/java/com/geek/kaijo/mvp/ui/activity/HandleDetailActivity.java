package com.geek.kaijo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.geek.kaijo.R;
import com.geek.kaijo.Utils.FileSizeUtil;
import com.geek.kaijo.app.Constant;
import com.geek.kaijo.di.component.DaggerHandleDetailComponent;
import com.geek.kaijo.di.module.HandleDetailModule;
import com.geek.kaijo.mvp.contract.HandleDetailContract;
import com.geek.kaijo.mvp.model.entity.ButtonLabel;
import com.geek.kaijo.mvp.model.entity.Case;
import com.geek.kaijo.mvp.model.entity.UploadFile;
import com.geek.kaijo.mvp.model.entity.UserInfo;
import com.geek.kaijo.mvp.presenter.HandleDetailPresenter;
import com.geek.kaijo.mvp.ui.adapter.UploadPhotoAdapter;
import com.geek.kaijo.mvp.ui.adapter.UploadVideoAdapter;
import com.geek.kaijo.view.LoadingProgressDialog;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.DataHelper;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * 处理详情
 */
public class HandleDetailActivity extends BaseActivity<HandleDetailPresenter> implements HandleDetailContract.View {

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
    @BindView(R.id.tv_case_attribute)
    TextView tvCaseAttribute;
    @BindView(R.id.tv_category_large)
    TextView tvCategoryLarge;
    @BindView(R.id.tv_category_small)
    TextView tvCategorySmall;
    @BindView(R.id.tv_category_sub)
    TextView tvCategorySub;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_view_location)
    TextView tvViewLocation;
    @BindView(R.id.tv_case_address)
    TextView tvCaseAddress;
    @BindView(R.id.tv_case_describe)
    TextView tvCaseDescribe;
    @BindView(R.id.tv_View_handle_process)
    TextView tvViewHandleProcess;
    @BindView(R.id.et_situation_description)
    EditText etSituationDescription;
    @BindView(R.id.tv_upload_image)
    TextView tvUploadImage;
    //    @BindView(R.id.ll_img_list)
//    LinearLayout llImgList;
    @BindView(R.id.tv_sign_in)
    TextView tvSignIn;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.ra_picture_list)
    RecyclerView raPictureList;
    @BindView(R.id.ra_video_list)
    RecyclerView raVideoList;
    /*核查*/
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.ok_radio)
    RadioButton ok_radio;
    @BindView(R.id.no_radio)
    RadioButton no_radio;
    /*核实*/
    @BindView(R.id.true_radio)
    RadioButton true_radio;
    /*处理*/
    @BindView(R.id.handle_radioGroup)
    RadioGroup handle_radioGroup;
    @BindView(R.id.handle_true_radio)
    RadioButton handle_true_radio;
    @BindView(R.id.handle_back_radio)
    RadioButton handle_back_radio;

    /*评分*/
    @BindView(R.id.la_ratinbar)
    LinearLayout la_ratinbar;

    @BindView(R.id.tv_photo_list)
    TextView tv_photo_list;
    @BindView(R.id.tv_video_list)
    TextView tv_video_list;


    private List<UploadFile> uploadPhotoList;
    private List<UploadFile> uploadVideoList;
    private UploadPhotoAdapter adapter1;
    private UploadVideoAdapter adapterVideo;

    private TextView tvImageList;//图片列表提示文字
    int curNode;  //12: 案件处理13: 案件核实14: 案件核查
    private int isWhich = 0;//1 上传图片  2 上传视频
    private Case aCase;
    private UserInfo userInfo;
    private LoadingProgressDialog loadingDialog;
    private int radioCheckedPosition;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerHandleDetailComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .handleDetailModule(new HandleDetailModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_handle_detail; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        curNode = getIntent().getIntExtra("curNode", 0);
        String caseId = getIntent().getStringExtra("case_id");
        String caseAttribute = getIntent().getStringExtra("case_attribute");
        userInfo = DataHelper.getDeviceData(this, Constant.SP_KEY_USER_INFO);
        if (mPresenter != null && userInfo != null) {
            mPresenter.findCaseInfoByMap(caseId, caseAttribute, userInfo.getUserId());
        }
        if (curNode == 12) {
            tvToolbarTitle.setText("处理详情页");
//            handle_radioGroup.setVisibility(View.VISIBLE);
        } else if (curNode == 13) {
            tvToolbarTitle.setText("核实详情页");
//            true_radio.setVisibility(View.VISIBLE);
        } else if (curNode == 14) {
            tvToolbarTitle.setText("核查详情页");
//            radioGroup.setVisibility(View.VISIBLE);
//            la_ratinbar.setVisibility(View.VISIBLE);
        }

        /*tvImageList = new TextView(this);
        tvImageList.setText("照片列表");
        tvImageList.setTextSize(18);
        tvImageList.setTextColor(getResources().getColor(R.color.color_text_black));
        tvImageList.setGravity(Gravity.CENTER);
        llImgList.addView(tvImageList);*/

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

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton radiobutton = (RadioButton) HandleDetailActivity.this.findViewById(radioGroup.getCheckedRadioButtonId());
                for (int i = 0; i < aCase.getButtonList().size(); i++) {
                    if (aCase.getButtonList().get(i).getLabel().equals(radiobutton.getText().toString())) {
                        radioCheckedPosition = i;
                        break;
                    }
                }


            }
        });


    }

    @Override
    public void updateView(Case data) {
        this.aCase = data;
        tvCaseAttribute.setText(Html.fromHtml("<b>案件属性：</b>" + data.getCasePrimaryCategory()));
        tvCategoryLarge.setText(Html.fromHtml("<b>大类：</b>" + data.getCasePrimaryCategory()));
        tvCategorySmall.setText(Html.fromHtml("<b>小类：</b>" + data.getCaseSecondaryCategory()));
        tvCategorySub.setText(Html.fromHtml("<b>子类：</b>" + data.getCaseChildCategory()));
        tvLocation.setText(Html.fromHtml("<b>位置：</b>地图查看"));

        tvCaseAddress.setText(Html.fromHtml("<b>地址：</b>" + data.getAddress()));
        tvCaseDescribe.setText(Html.fromHtml("<b>描述：</b>" + data.getDescription()));
        if (aCase != null && aCase.getButtonList() != null) {
            for (int i = 0; i < aCase.getButtonList().size(); i++) {
                RadioButton radioButton = new RadioButton(this);
                radioButton.setTextColor(getResources().getColor(R.color.color_text_title));

                radioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.t13));
//                radioButton.setId(i);
                radioButton.setText(aCase.getButtonList().get(i).getLabel());
                radioGroup.addView(radioButton);
            }
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
                            uploadPhotoList.get(i).caseProcessRecordId = curNode;
                            uploadPhotoList.get(i).whenType = 2;
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
                            uploadVideoList.get(i).caseProcessRecordId = curNode;
                            uploadVideoList.get(i).whenType = 2;
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

    @Override
    public void httpCommitSuccess() {
        setResult(1);
        finish();
    }

    @Override
    public void uploadPhotoError() {
        if (uploadPhotoList != null) {
            for (int i = 0; i < uploadPhotoList.size(); i++) {
                if (TextUtils.isEmpty(uploadPhotoList.get(i).getUrl())) {
                    uploadPhotoList.get(i).setIsSuccess(2);
                    adapter1.notifyItemChanged(i);
                }
            }
        }
        if (uploadVideoList != null) {
            for (int i = 0; i < uploadVideoList.size(); i++) {
                if (TextUtils.isEmpty(uploadVideoList.get(i).getUrl())) {
                    uploadVideoList.get(i).setIsSuccess(2);
                    adapterVideo.notifyItemChanged(i);
                }
            }
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

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
//        llImgList.removeAllViews();
        super.onDestroy();
        tvImageList = null;
    }

    @Override
    public void killMyself() {
        finish();
    }

    @OnClick({R.id.tv_view_location, R.id.tv_View_handle_process, R.id.tv_upload_image, R.id.tv_sign_in, R.id.tv_submit, R.id.tv_cancel, R.id.ra_video})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_view_location://查看坐标
                if (aCase != null) {
                    Intent intent = new Intent(this, MapActivity.class);
                    intent.putExtra("lat", aCase.getLat());
                    intent.putExtra("lng", aCase.getLng());
                    launchActivity(intent);
                }
                break;
            case R.id.tv_View_handle_process://查看处理过程
                Intent intent = new Intent(this, ProcessActivity.class);
                intent.putExtra("Case", aCase);
                startActivity(intent);
                break;
            case R.id.tv_upload_image://上传照片
                isWhich = 1;
                pictureSelector();
                break;
            case R.id.ra_video://上传视频
                isWhich = 2;
                videoSelector();
                break;
            case R.id.tv_sign_in://签收
                break;
            case R.id.tv_submit://提交
                if (TextUtils.isEmpty(etSituationDescription.getText().toString())) {
                    Toast.makeText(this, "描述情况不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                if (userInfo != null && aCase != null) {
                    List<UploadFile> uploadFileList = new ArrayList<>();
                    for (int i = 0; i < uploadPhotoList.size(); i++) {
                        if (!TextUtils.isEmpty(uploadPhotoList.get(i).getUrl())) {
                            uploadFileList.add(uploadPhotoList.get(i));
                        }
                    }
                    for (int i = 0; i < uploadVideoList.size(); i++) {
                        if (!TextUtils.isEmpty(uploadVideoList.get(i).getUrl())) {
                            uploadFileList.add(uploadVideoList.get(i));
                        }
                    }
                    ButtonLabel buttonLabel = aCase.getButtonList().get(radioCheckedPosition);
                    if (TextUtils.isEmpty(buttonLabel.getLabel())) {
                        Toast.makeText(this, "请选择流程节点", Toast.LENGTH_LONG).show();
                        return;
                    }
//                        String label="";
//                        if(radioGroup.getCheckedRadioButtonId()==ok_radio.getId()){ //审核通过
//                            label = ok_radio.getText().toString();
//                        }else if(radioGroup.getCheckedRadioButtonId()==no_radio.getId()) { //审核不通过
//                            label = no_radio.getText().toString();
//                        }else if(handle_radioGroup.getCheckedRadioButtonId()==handle_true_radio.getId()){ //处理（下一步：核查）
//                            label = handle_true_radio.getText().toString();
//                        }else if(handle_radioGroup.getCheckedRadioButtonId()==handle_back_radio.getId()){
//                            label = handle_back_radio.getText().toString();
//                        }else if(true_radio.isChecked()){ //核实
//                            label = true_radio.getText().toString();
//                        }
                    mPresenter.addOperate(userInfo.getUserId(), buttonLabel.getLabel(), etSituationDescription.getText().toString(), aCase.getCaseId(), aCase.getProcessId(), buttonLabel.getState() + "", "", aCase.getFirstWorkunit(), uploadFileList);
                }

                break;
            case R.id.tv_cancel://取消
                this.finish();
                break;
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
            public void onItemAgainUploadClick(View v, int position) {  ////上传失败 重新上传
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
        }
    }
}
