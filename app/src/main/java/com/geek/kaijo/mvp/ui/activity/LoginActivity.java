package com.geek.kaijo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.geek.kaijo.R;
import com.geek.kaijo.app.Constant;
import com.geek.kaijo.di.component.DaggerLoginComponent;
import com.geek.kaijo.di.module.LoginModule;
import com.geek.kaijo.mvp.contract.LoginContract;
import com.geek.kaijo.mvp.model.entity.UserInfo;
import com.geek.kaijo.mvp.presenter.LoginPresenter;
import com.geek.kaijo.view.LoadingProgressDialog;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.DataHelper;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {

    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.tv_login)
    TextView tvLogin;

    private LoadingProgressDialog loadingDialog;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerLoginComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .loginModule(new LoginModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_login; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
//        int width = (int) DeviceUtils.getScreenWidth(this);
//        int height = width * 1280 / 1080 + 1;
//        ivLogo.setLayoutParams(new RelativeLayout.LayoutParams(width, height));

//        etUsername.setText("nsc_wgy1");
//        etPassword.setText("111111");
        String sfUserName = DataHelper.getStringSF(this, Constant.SP_KEY_USER_NAME);
        if (!TextUtils.isEmpty(sfUserName)) {
            etUsername.setText(sfUserName);
        }

        tvLogin.setOnClickListener(v -> {
            String userName = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            if (checkLogin(userName, password) && mPresenter != null) {
                mPresenter.login(userName, password);
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

    private boolean checkLogin(String username, String password) {
        if (TextUtils.isEmpty(username)) {
            showMessage("用户名/账号不能为空");
            return false;
        } else if (TextUtils.isEmpty(password)) {
            showMessage("密码不能为空");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.makeText(getApplicationContext(),message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private long exitTime = 0;

    /**
     * 退出应用
     */
    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            showMessage("再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            ArmsUtils.exitApp();
        }
    }


    @Override
    public void killMyself() {
        finish();
    }

}
