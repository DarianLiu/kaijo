package com.geek.kaijo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.geek.kaijo.di.component.DaggerAgainPasswordComponent;
import com.geek.kaijo.di.module.AgainPasswordModule;
import com.geek.kaijo.mvp.contract.AgainPasswordContract;
import com.geek.kaijo.mvp.presenter.AgainPasswordPresenter;

import com.geek.kaijo.R;


import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class AgainPasswordActivity extends BaseActivity<AgainPasswordPresenter> implements AgainPasswordContract.View {

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_toolbar_title_right_text)
    TextView tv_toolbar_title_right_text;

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

        tv_toolbar_title_right_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
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
}
