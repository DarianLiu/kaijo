package com.geek.kaijo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.geek.kaijo.R;
import com.geek.kaijo.app.Constant;
import com.geek.kaijo.app.MyApplication;
import com.geek.kaijo.app.api.Api;
import com.geek.kaijo.di.component.DaggerMyInfoComponent;
import com.geek.kaijo.di.module.MyInfoModule;
import com.geek.kaijo.mvp.contract.MyInfoContract;
import com.geek.kaijo.mvp.model.entity.UploadFile;
import com.geek.kaijo.mvp.model.entity.UserInfo;
import com.geek.kaijo.mvp.presenter.MyInfoPresenter;
import com.geek.kaijo.view.LoadingProgressDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.DataHelper;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class MyInfoActivity extends BaseActivity<MyInfoPresenter> implements MyInfoContract.View {
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.img_head)
    ImageView img_head;
    @BindView(R.id.tv_userName)
    TextView tv_userName;   //用户名
    @BindView(R.id.tv_name)
    TextView tv_name;   //真实姓名
    @BindView(R.id.tv_phone)
    TextView tv_phone;   //手机号码
    @BindView(R.id.tv_department)
    TextView tv_department;   //部门
    @BindView(R.id.tv_position)
    TextView tv_position;   //职务
    @BindView(R.id.tv_telephone)
    TextView tv_telephone;   //电话
    @BindView(R.id.tv_adress)
    TextView tv_adress;   //地址
    @BindView(R.id.tv_code)
    TextView tv_code;   //身份证
    @BindView(R.id.tv_equipment)
    TextView tv_equipment;   //终端设备编号
    @BindView(R.id.tv_equipment_phone)
    TextView tv_equipment_phone;   //终端电话号码
    @BindView(R.id.tv_sim)
    TextView tv_sim;   //SIM编号


    private UserInfo userInfo;
    RequestOptions options ;//图片加载失败后，显示的图片
    private LoadingProgressDialog loadingDialog;
    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerMyInfoComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .myInfoModule(new MyInfoModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_my_info; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        tvToolbarTitle.setText("个人信息");
        options = new RequestOptions()
                .placeholder(R.mipmap.ic_launcher)//图片加载出来前，显示的图片
                .fallback(R.mipmap.ic_launcher) //url为空的时候,显示的图片
                .error(R.mipmap.ic_launcher);//图片加载失败后，显示的图片
        initInfo();
    }

    private void initInfo(){
        userInfo = DataHelper.getDeviceData(this, Constant.SP_KEY_USER_INFO);
        if(userInfo!=null){
            Glide.with(MyApplication.get())
                    .load(Api.URL_BANNER + "/" + userInfo.getHeadUrl()) //图片地址
//                    .load(userInfo.getHeadUrl()) //图片地址
                    .apply(options)
                    .into(img_head);

            tv_userName.setText(userInfo.getUsername());
            tv_name.setText(userInfo.getTrueName());
            tv_phone.setText(userInfo.getMobile());
            tv_department.setText(userInfo.getBelongEntity()); //部门
            tv_position.setText(userInfo.getJob());  //职位
            tv_telephone.setText(userInfo.getPhone());
            tv_adress.setText(userInfo.getAddress());
            tv_code.setText(userInfo.getIdcard());
            tv_equipment.setText(userInfo.getDeviceSn()); //终端设备编号
            tv_equipment_phone.setText(userInfo.getDevicePhone());//终端电话号码
            tv_sim.setText(userInfo.getDeviceSim());//sim编号
        }
    }

    @OnClick({R.id.rl_head, R.id.rl_name, R.id.rl_phone, R.id.rl_position, R.id.rl_telephone, R.id.rl_adress,
            R.id.rl_code,R.id.rl_equipment,R.id.rl_equipment_phone,R.id.rl_sim,R.id.rl_change_password,})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.rl_head://头像
                pictureSelector();
                break;

            case R.id.rl_name://真实姓名
                intent = new Intent(this,InfoEditActivity.class);
                intent.putExtra("UserInfo",userInfo);
                intent.putExtra("tag",Constant.info_realName);
                startActivityForResult(intent,1);
                break;
            case R.id.rl_phone://手机号码
                intent = new Intent(this,InfoEditActivity.class);
                intent.putExtra("UserInfo",userInfo);
                intent.putExtra("tag",Constant.info_phone);
                startActivityForResult(intent,1);
                break;

            case R.id.rl_position://职务
                intent = new Intent(this,InfoEditActivity.class);
                intent.putExtra("UserInfo",userInfo);
                intent.putExtra("tag",Constant.info_position);
                startActivityForResult(intent,1);
                break;
            case R.id.rl_telephone://电话
                intent = new Intent(this,InfoEditActivity.class);
                intent.putExtra("UserInfo",userInfo);
                intent.putExtra("tag",Constant.info_telephone);
                startActivityForResult(intent,1);
                break;
            case R.id.rl_adress://地址
                intent = new Intent(this,InfoEditActivity.class);
                intent.putExtra("UserInfo",userInfo);
                intent.putExtra("tag",Constant.info_adress);
                startActivityForResult(intent,1);
                break;
            case R.id.rl_code://身份证
                intent = new Intent(this,InfoEditActivity.class);
                intent.putExtra("UserInfo",userInfo);
                intent.putExtra("tag",Constant.info_code);
                startActivityForResult(intent,1);
                break;
            case R.id.rl_equipment://终端设备编号
                intent = new Intent(this,InfoEditActivity.class);
                intent.putExtra("UserInfo",userInfo);
                intent.putExtra("tag",Constant.info_equipment);
                startActivityForResult(intent,1);
                break;
            case R.id.rl_equipment_phone://终端电话号码
                intent = new Intent(this,InfoEditActivity.class);
                intent.putExtra("UserInfo",userInfo);
                intent.putExtra("tag",Constant.info_equipment_phone);
                startActivityForResult(intent,1);
                break;
            case R.id.rl_sim://SIM编号
                intent = new Intent(this,InfoEditActivity.class);
                intent.putExtra("UserInfo",userInfo);
                intent.putExtra("tag",Constant.info_sim);
                startActivityForResult(intent,1);
                break;
            case R.id.rl_change_password://修改密码
                intent = new Intent(this,AgainPasswordActivity.class);
                intent.putExtra("UserInfo",userInfo);
                startActivityForResult(intent,1);
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
                .enableCrop(true) //是否裁剪
                .compress(true) //是否压缩
                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .minimumCompressSize(100)
                .withAspectRatio(1, 1)
                .showCropFrame(true)
                .rotateEnabled(true)
                .isDragFrame(true)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PictureConfig.CHOOSE_REQUEST && resultCode==RESULT_OK){
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            String cutPath = selectList.get(0).getCutPath();
            if(!TextUtils.isEmpty(cutPath)){
                if (mPresenter != null) {
                    mPresenter.uploadFile(cutPath);
                }
            }

        }else if(requestCode==1 && resultCode==1){
            initInfo();
        }

    }

    @Override
    public void uploadSuccess(UploadFile uploadPhoto) {
        Glide.with(MyApplication.get())
//                .load(Api.URL_BANNER + "/" + uploadPhoto.getFileRelativePath()) //图片地址
                .load(uploadPhoto.getFileName()) //图片地址
                .apply(options)
                .into(img_head);
        if(uploadPhoto!=null){
            httpUpdateHeader(uploadPhoto.getFileRelativePath());
        }
    }

    private void httpUpdateHeader(String headUrl){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", userInfo.getUserId());
        jsonObject.addProperty("headUrl", headUrl);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),
                new Gson().toJson(jsonObject));

        mPresenter.httpUpdateUserForApp(requestBody);
    }

    @Override
    public void uploadPhotoError() {

    }

    @Override
    public void httpUpdateUserSuccess(UserInfo userInfo) {
        if(userInfo!=null){
            DataHelper.saveDeviceData(this,Constant.SP_KEY_USER_INFO,userInfo);
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
    public void killMyself() {
        finish();
    }


}
