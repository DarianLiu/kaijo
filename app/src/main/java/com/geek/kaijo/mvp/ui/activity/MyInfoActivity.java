package com.geek.kaijo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.geek.kaijo.di.component.DaggerMyInfoComponent;
import com.geek.kaijo.di.module.MyInfoModule;
import com.geek.kaijo.mvp.contract.MyInfoContract;
import com.geek.kaijo.mvp.presenter.MyInfoPresenter;

import com.geek.kaijo.R;


import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class MyInfoActivity extends BaseActivity<MyInfoPresenter> implements MyInfoContract.View {
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;

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
