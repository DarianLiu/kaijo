package com.geek.kaijo.mvp.presenter;

import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.geek.kaijo.app.Constant;
import com.geek.kaijo.app.EventBusTags;
import com.geek.kaijo.app.api.Api;
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
import com.kook.KKCallback;
import com.kook.KKManager;
import com.kook.sdk.wrapper.auth.consts.AuthTypeEnum;
import com.kook.sdk.wrapper.auth.model.LoginResult;
import com.kook.view.dialog.DialogShowUtil;

import org.simple.eventbus.EventBus;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
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
                .subscribeWith(new ErrorHandleSubscriber<UserInfo>(mErrorHandler) {
                    @Override
                    public void onNext(UserInfo user) {

                        KKManager.getInstance().login("wanfushequdiyiwangge01", Api.IM_IP, new KKCallback() {
                            @Override
                            public void onError(int i) {
//                                Log.i(this.getClass().getName(),"2222222222222222222222222登陆失败IM"+i);
                                mRootView.showMessage("失败");
                            }

                            @Override
                            public void onSucceed() {
//                                Log.i(this.getClass().getName(),"1111111111111111111111登陆成功IM");
                                mRootView.showMessage("登录成功");
                                if(user!=null){
                                    DataHelper.setStringSF(mApplication, Constant.SP_KEY_USER_NAME, user.getUsername());
                                    DataHelper.setStringSF(mApplication, Constant.SP_KEY_USER_ID, user.getUserId());
                                    DataHelper.saveDeviceData(mApplication, Constant.SP_KEY_USER_INFO, user);
                                    mRootView.launchActivity(new Intent(mAppManager.getTopActivity(), MainActivity.class));
                                    mRootView.killMyself();
                                }
                            }
                        });  //IM登陆

//                        mRootView.launchActivity(new Intent(mAppManager.getTopActivity(), MainActivity.class));
//                        mRootView.killMyself();
                    }
                });
    }

//    private void imLogin(){
//    //登录操作
//        Disposable subscribe = KKManager.getInstance().login("xiaoxiao", "11111111", Api.IM_IP, AuthTypeEnum.eAuthTypeNonePassword)
//                .take(1)
//                .subscribe(new Consumer<LoginResult>() {
//                    @Override
//                    public void accept(LoginResult loginResult) throws Exception {
//                        DialogShowUtil.dismissLoadingDialog();
//                        int errorCode = loginResult.getLoginErr();
//                        if (errorCode == 0) {
////                            Intent intent = new Intent(DemoLoginActivity.this, DemoMainActivity.class);
////                            startActivity(intent);
////                            getSharedPreferences("login", MODE_PRIVATE)
////                                    .edit().putString("ac", account)
////                                    .putString("ip", ip)
////                                    .apply();
////                            finish();
//                            mRootView.showMessage("IM登录成功");
//                        } else {
//                            mRootView.showMessage("IM登录失败"+errorCode);
//                            Log.i(this.getClass().getName(),"111111111111111111111111IM登录失败==="+loginResult.getErrMsg());
//                        }
//                    }
//                });
//        new CompositeDisposable().add(subscribe);
//    }

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
                        if(user!=null && !TextUtils.isEmpty(user.getUsername())){
                            DataHelper.setStringSF(mApplication, Constant.SP_KEY_USER_NAME, user.getUsername());
                        }
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
