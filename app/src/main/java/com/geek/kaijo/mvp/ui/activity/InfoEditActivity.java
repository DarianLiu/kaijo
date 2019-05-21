package com.geek.kaijo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.geek.kaijo.app.Constant;
import com.geek.kaijo.mvp.model.entity.UserInfo;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.geek.kaijo.di.component.DaggerInfoEditComponent;
import com.geek.kaijo.di.module.InfoEditModule;
import com.geek.kaijo.mvp.contract.InfoEditContract;
import com.geek.kaijo.mvp.presenter.InfoEditPresenter;

import com.geek.kaijo.R;


import butterknife.BindView;

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
