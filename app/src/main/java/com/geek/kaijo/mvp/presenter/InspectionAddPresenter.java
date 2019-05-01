package com.geek.kaijo.mvp.presenter;

import android.app.Application;

import com.geek.kaijo.app.api.RxUtils;
import com.geek.kaijo.mvp.model.entity.Case;
import com.geek.kaijo.mvp.model.entity.Inspection;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import javax.inject.Inject;

import com.geek.kaijo.mvp.contract.InspectionAddContract;

import java.util.List;


@ActivityScope
public class InspectionAddPresenter extends BasePresenter<InspectionAddContract.Model, InspectionAddContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public InspectionAddPresenter(InspectionAddContract.Model model, InspectionAddContract.View rootView) {
        super(model, rootView);
    }

    public void findThingListBy(String assortId){
        mModel.findThingListBy(assortId).compose(RxUtils.applySchedulers(mRootView))
                .compose(RxUtils.handleBaseResult(mApplication))
                .subscribeWith(new ErrorHandleSubscriber<List<Inspection>>(mErrorHandler) {
                    @Override
                    public void onNext(List<Inspection> inspectionList) {
                        mRootView.httpInspectionSuccess(inspectionList);
                    }
                });
    }
    public void addOrUpdateThingPosition(int thingId,String name,Double lat,Double lng,int streetId,int communityId,int gridId,String createUser){
        mModel.addOrUpdateThingPosition(thingId,name,lat,lng,streetId,communityId,gridId,createUser).compose(RxUtils.applySchedulers(mRootView))
                .compose(RxUtils.handleBaseResult(mApplication))
                .subscribeWith(new ErrorHandleSubscriber<Inspection>(mErrorHandler) {
                    @Override
                    public void onNext(Inspection inspection) {
                        mRootView.httpAddInspectionSuccess(inspection);
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
