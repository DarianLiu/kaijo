package com.geek.kaijo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.geek.kaijo.mvp.model.entity.UserInfo;
import com.geek.kaijo.view.LoadingProgressDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.geek.kaijo.di.component.DaggerAgainPasswordComponent;
import com.geek.kaijo.di.module.AgainPasswordModule;
import com.geek.kaijo.mvp.contract.AgainPasswordContract;
import com.geek.kaijo.mvp.presenter.AgainPasswordPresenter;

import com.geek.kaijo.R;


import butterknife.BindView;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class AgainPasswordActivity extends BaseActivity<AgainPasswordPresenter> implements AgainPasswordContract.View {

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_toolbar_title_right_text)
    TextView tv_toolbar_title_right_text;
    private LoadingProgressDialog loadingDialog;

    private UserInfo userInfo;
    @BindView(R.id.et_password_old)
    TextView et_password_old;  //原密码
    @BindView(R.id.et_password_new)
    TextView et_password_new;  //新密码
    @BindView(R.id.et_password_new_true)
    TextView et_password_new_true;  //确定密码


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerAgainPasswordComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .againPasswordModule(new AgainPasswordModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_again_password; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        tvToolbarTitle.setText("修改密码");
        tv_toolbar_title_right_text.setVisibility(View.VISIBLE);
        tv_toolbar_title_right_text.setText("确定");
        userInfo = (UserInfo) getIntent().getSerializableExtra("UserInfo");

        tv_toolbar_title_right_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(et_password_old.getText().toString().trim())){
                    Toast.makeText(AgainPasswordActivity.this,"请输入原密码",Toast.LENGTH_LONG).show();
                    return;
                }else if(TextUtils.isEmpty(et_password_new.getText().toString().trim())){
                    Toast.makeText(AgainPasswordActivity.this,"请输入新密码",Toast.LENGTH_LONG).show();
                    return;
                }else if(TextUtils.isEmpty(et_password_new_true.getText().toString().trim())){
                    Toast.makeText(AgainPasswordActivity.this,"请输入确认密码",Toast.LENGTH_LONG).show();
                    return;
                }else if(!et_password_new.getText().toString().trim().equals(et_password_new_true.getText().toString().trim())){
                    Toast.makeText(AgainPasswordActivity.this,"密码不一致",Toast.LENGTH_LONG).show();
                    return;
                }
                if(userInfo==null)return;
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("userId", userInfo.getUserId());
                jsonObject.addProperty("password", et_password_old.getText().toString().trim());
                jsonObject.addProperty("password2", et_password_new.getText().toString().trim());
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
        setResult(1);
        finish();
    }
}