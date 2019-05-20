package com.geek.kaijo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geek.kaijo.R;
import com.geek.kaijo.di.component.DaggerChangePasswordComponent;
import com.geek.kaijo.di.module.ChangePasswordModule;
import com.geek.kaijo.mvp.contract.ChangePasswordContract;
import com.geek.kaijo.mvp.presenter.ChangePasswordPresenter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class ChangePasswordActivity extends BaseActivity<ChangePasswordPresenter> implements ChangePasswordContract.View {
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.change_pd_username)
    TextView changePdUsername;//用户名
    @BindView(R.id.change_pd_actualname)
    EditText changePdActualname;//真实姓名
    @BindView(R.id.change_pd_department)
    TextView changePdDepartment;//部门
    @BindView(R.id.change_pd_position)
    EditText changePdPosition;//职务
    @BindView(R.id.change_pd_phone)
    EditText changePdPhone;//电话
    @BindView(R.id.change_pd_cellphonenumber)
    EditText changePdCellphonenumber;//手机号码
    @BindView(R.id.change_pd_address)
    EditText changePdAddress;//地址
    @BindView(R.id.change_pd_idcard)
    EditText changePdIdcard;//身份证
    @BindView(R.id.change_pd_terminalnumber)
    EditText changePdTerminalnumber;//终端设备号
    @BindView(R.id.change_pd_terminalphone)
    EditText changePdTerminalphone;//终端设备编号
    @BindView(R.id.change_pd_simnumber)
    EditText changePdSimnumber;//终端电话号码
    @BindView(R.id.change_pd_return)
    TextView changePdReturn;//返回
    @BindView(R.id.change_pd_changepwd)
    TextView changePdChangepwd;//修改密码
    @BindView(R.id.change_pd_save)
    TextView changePdSave;//保存
    @BindView(R.id.change_pd_uploadavatar)
    TextView changePdUploadavatar;//上传头像
    @BindView(R.id.change_pd_saveavater)
    TextView changePdSaveavater;//保存图像
    @BindView(R.id.edit_picture_layout)
    LinearLayout editPictureLayout;//头像编辑布局
    @BindView(R.id.change_pd_oldpwd)
    EditText changePdOldpwd;//原密码
    @BindView(R.id.change_pd_newpwd)
    EditText changePdNewpwd;//新密码
    @BindView(R.id.change_pd_confirmnewpwd)
    EditText changePdConfirmnewpwd;//确认新密码
    @BindView(R.id.change_pd_savepwd)
    TextView changePdSavepwd;//密码  保存
    @BindView(R.id.change_pd_layout)
    LinearLayout changePdLayout;//修改密码布局
    @BindView(R.id.change_pd_bttext)
    TextView changePdBttext;//底部文字
    @BindView(R.id.change_pd_editpicture)
    TextView changePdEditpicture;//编辑头像
    @BindView(R.id.change_pd_scrollview)
    NestedScrollView changePdScrollview;//滑动控件

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerChangePasswordComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .changePasswordModule(new ChangePasswordModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_change_password; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        tvToolbarTitle.setText("编辑个人信息");
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


    @OnClick({R.id.change_pd_editpicture, R.id.change_pd_changepwd, R.id.change_pd_save, R.id.change_pd_uploadavatar, R.id.change_pd_saveavater, R.id.change_pd_savepwd, R.id.change_pd_return})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.change_pd_editpicture://编辑头像
                editPictureLayout.setVisibility(View.VISIBLE);
                changePdScrollview.fullScroll(NestedScrollView.FOCUS_DOWN);
                break;
            case R.id.change_pd_changepwd://修改密码
                changePdLayout.setVisibility(View.VISIBLE);
                changePdScrollview.fullScroll(NestedScrollView.FOCUS_DOWN);  //滚动到底部
                break;
            case R.id.change_pd_save://保存
                break;
            case R.id.change_pd_uploadavatar://上传头像
                break;
            case R.id.change_pd_saveavater:// 头像保存
                break;
            case R.id.change_pd_savepwd://密码保存
                break;
            case R.id.change_pd_return://返回
                killMyself();
                break;

        }
    }

}
