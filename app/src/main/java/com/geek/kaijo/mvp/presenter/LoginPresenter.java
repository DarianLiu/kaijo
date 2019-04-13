package com.geek.kaijo.mvp.presenter;

import android.app.Application;
import android.content.Intent;

import com.geek.kaijo.app.Constant;
import com.geek.kaijo.app.EventBusTags;
import com.geek.kaijo.app.api.RxUtils;
import com.geek.kaijo.mvp.contract.LoginContract;
import com.geek.kaijo.mvp.model.entity.User;
import com.geek.kaijo.mvp.model.entity.UserInfo;
import com.geek.kaijo.mvp.ui.activity.MainActivity;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.DataHelper;

import org.simple.eventbus.EventBus;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;


@ActivityScope
public class LoginPresenter extends BasePresenter<LoginContract.Model, LoginContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public LoginPresenter(LoginContract.Model model, LoginContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 登录
     *
     * @param userName 用户名/账号
     * @param password 密码
     */
    public void login(String userName, String password) {
        mModel.login(userName, password).compose(RxUtils.applySchedulers(mRootView))
                .compose(RxUtils.handleBaseResult(mApplication))
                .subscribeWith(new ErrorHandleSubscriber<User>(mErrorHandler) {
                    @Override
                    public void onNext(User user) {
                        findStreetById(user.getToken(), user.getUserId());
                    }
                });
    }

    /**
     * 根取当前登录用户，所属街道、社区、网格
     */
    private void findStreetById(String token, String userId) {
        mModel.findStreetById(userId).compose(RxUtils.applySchedulers(mRootView))
                .compose(RxUtils.handleBaseResult(mApplication))
                .subscribeWith(new ErrorHandleSubscriber<UserInfo>(mErrorHandler) {
                    @Override
                    public void onNext(UserInfo user) {
                        DataHelper.setStringSF(mApplication, Constant.SP_KEY_USER_ID, userId);
                        DataHelper.setStringSF(mApplication, Constant.SP_KEY_USER_TOKEN, token);
                        DataHelper.saveDeviceData(mApplication, Constant.SP_KEY_USER_INFO, user);
                        mRootView.showMessage("登录成功");
                        EventBus.getDefault().post(user, EventBusTags.TAG_LOGIN_STATE);
                        mRootView.launchActivity(new Intent(mAppManager.getTopActivity(), MainActivity.class));
                        mRootView.killMyself();
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }
}
