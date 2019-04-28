package com.geek.kaijo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.geek.kaijo.R;
import com.geek.kaijo.di.component.DaggerInspectionAddComponent;
import com.geek.kaijo.di.module.InspectionAddModule;
import com.geek.kaijo.mvp.contract.InspectionAddContract;
import com.geek.kaijo.mvp.presenter.InspectionAddPresenter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class InspectionAddActivity extends BaseActivity<InspectionAddPresenter> implements InspectionAddContract.View {

    @BindView(R.id.tv_thing_name)
    TextView tv_thing_name;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerInspectionAddComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .inspectionAddModule(new InspectionAddModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_inspection_add; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

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

    @OnClick({R.id.tv_thing_name})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.tv_thing_name:

                break;
        }
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
