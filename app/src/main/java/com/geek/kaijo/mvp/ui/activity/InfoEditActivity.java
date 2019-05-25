package com.geek.kaijo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.geek.kaijo.app.Constant;
import com.geek.kaijo.mvp.model.entity.UserInfo;
import com.geek.kaijo.view.LoadingProgressDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.geek.kaijo.di.component.DaggerInfoEditComponent;
import com.geek.kaijo.di.module.InfoEditModule;
import com.geek.kaijo.mvp.contract.InfoEditContract;
import com.geek.kaijo.mvp.presenter.InfoEditPresenter;

import com.geek.kaijo.R;
import com.jess.arms.utils.DataHelper;


import butterknife.BindView;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class InfoEditActivity extends BaseActivity<InfoEditPresenter> implements InfoEditContract.View {

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_toolbar_title_right_text)
    TextView tv_toolbar_title_right_text;
    @BindView(R.id.edit_content)
    EditText edit_content;

    private UserInfo userInfo;
    private String title;
    private LoadingProgressDialog loadingDialog;


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerInfoEditComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .infoEditModule(new InfoEditModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_info_edit; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

        tv_toolbar_title_right_text.setVisibility(View.VISIBLE);
        tv_toolbar_title_right_text.setText("确定");

        userInfo = (UserInfo) getIntent().getSerializableExtra("UserInfo");
        title = getIntent().getStringExtra("tag");
        tvToolbarTitle.setText(title);
        String content = "";
        if(userInfo!=null){
            if(Constant.info_userName.equals(title)){   //用户名
                content = userInfo.getUsername();
            }
        }
        edit_content.setText(content);
        edit_content.setSelection(edit_content.getText().toString().trim().length());

        tv_toolbar_title_right_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(edit_content.getText().toString().trim())){
                    Toast.makeText(InfoEditActivity.this,"请输入"+title,Toast.LENGTH_LONG).show();
                    return;
                }
                if(userInfo==null)return;
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("userId", userInfo.getUserId());
                if(Constant.info_realName.equals(title)){ //真实姓名
                    jsonObject.addProperty("trueName", edit_content.getText().toString().trim());
                }else if (Constant.info_position.equals(title)){ //职位
                    jsonObject.addProperty("job", edit_content.getText().toString().trim());
                }else if (Constant.info_telephone.equals(title)){ //电话号码
                    jsonObject.addProperty("phone", edit_content.getText().toString().trim());
                }else if (Constant.info_phone.equals(title)){ //手机号
                    jsonObject.addProperty("mobile", edit_content.getText().toString().trim());
                }else if (Constant.info_adress.equals(title)){ //地址
                    jsonObject.addProperty("adress", edit_content.getText().toString().trim());
                }else if (Constant.info_code.equals(title)){ //身份证
                    jsonObject.addProperty("idcard", edit_content.getText().toString().trim());
                }else if (Constant.info_equipment.equals(title)){ //终端设备编号
                    jsonObject.addProperty("deviceSn", edit_content.getText().toString().trim());
                }else if (Constant.info_equipment_phone.equals(title)){ //终端电话号码
                    jsonObject.addProperty("devicePhone", edit_content.getText().toString().trim());
                }else if (Constant.info_sim.equals(title)){ //SIM编号
                    jsonObject.addProperty("deviceSim", edit_content.getText().toString().trim());
                }

                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),
                        new Gson().toJson(jsonObject));

                mPresenter.httpUpdateUserForApp(requestBody);
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
    public void httpUpdateUserSuccess(UserInfo userInfo) {
        DataHelper.saveDeviceData(this,Constant.SP_KEY_USER_INFO,userInfo);
        setResult(1);
        finish();
    }
}

