package com.geek.kaijo.mvp.presenter;

import android.app.Application;

import com.geek.kaijo.app.api.RxUtils;
import com.geek.kaijo.mvp.model.entity.UserInfo;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import okhttp3.RequestBody;

import javax.inject.Inject;

import com.geek.kaijo.mvp.contract.AgainPasswordContract;


@ActivityScope
public class AgainPasswordPresenter extends BasePresenter<AgainPasswordContract.Model, AgainPasswordContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public AgainPasswordPresenter(AgainPasswordContract.Model model, AgainPasswordContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    /**
     * 用户修改
     */
    public void httpUpdateUserForApp(RequestBody requestBody) {


        mModel.httpUpdateUserForApp(requestBody)
//                .compose(RxUtils.applySchedulers(mRootView))
                .compose(RxUtils.applySchedulersHide(mRootView))
                .compose(RxUtils.handleBaseResult(mApplication))
                .subscribeWith(new ErrorHandleSubscriber<UserInfo>(mErrorHandler) {
                    @Override
                    public void onNext(UserInfo ipRegisterBeans) {

                        mRootView.httpUpdateUserSuccess(ipRegisterBeans);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();

                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);

                    }
                });
    }

}
