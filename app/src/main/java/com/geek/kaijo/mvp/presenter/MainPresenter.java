package com.geek.kaijo.mvp.presenter;

import android.app.Application;

import com.geek.kaijo.app.api.RxUtils;
import com.geek.kaijo.mvp.model.entity.Banner;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import javax.inject.Inject;

import com.geek.kaijo.mvp.contract.MainContract;

import java.util.List;


@ActivityScope
public class MainPresenter extends BasePresenter<MainContract.Model, MainContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public MainPresenter(MainContract.Model model, MainContract.View rootView) {
        super(model, rootView);
    }

//    private String getUserId() {
//        return DataHelper.getStringSF(mApplication, Constant.SP_KEY_USER_ID);
//    }

    public void findAllBannerList() {
        mModel.findAllBannerList()
                .compose(RxUtils.applySchedulers(mRootView))
                .compose(RxUtils.handleBaseResult(mApplication))
                .subscribeWith(new ErrorHandleSubscriber<List<Banner>>(mErrorHandler) {
                    @Override
                    public void onNext(List<Banner> bannerList) {
                        mRootView.setAutoBanner(bannerList);
                    }
                });
    }

//    /**
//     * 根取当前登录用户，所属街道、社区、网格
//     */
//    public void findStreetById() {
//        if (TextUtils.isEmpty(getUserId())) {
//            mRootView.launchActivity(new Intent(mApplication, LoginActivity.class));
//            return;
//        }
//        mModel.findStreetById(getUserId()).compose(RxUtils.applySchedulers(mRootView))
//                .compose(RxUtils.handleBaseResult(mApplication))
//                .subscribeWith(new ErrorHandleSubscriber<UserInfo>(mErrorHandler) {
//                    @Override
//                    public void onNext(UserInfo user) {
//                        mRootView.updateUserInfo();
//                    }
//                });
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }
}
