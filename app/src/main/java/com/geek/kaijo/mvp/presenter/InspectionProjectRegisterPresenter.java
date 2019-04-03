package com.geek.kaijo.mvp.presenter;

import android.app.Application;

import com.geek.kaijo.mvp.contract.InspectionProjectRegisterContract;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;


@ActivityScope
public class InspectionProjectRegisterPresenter extends BasePresenter<InspectionProjectRegisterContract.Model, InspectionProjectRegisterContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public InspectionProjectRegisterPresenter(InspectionProjectRegisterContract.Model model, InspectionProjectRegisterContract.View rootView) {
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
}
