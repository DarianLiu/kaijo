package com.geek.kaijo.mvp.presenter;

import android.app.Application;

import com.geek.kaijo.app.api.RxUtils;
import com.geek.kaijo.mvp.contract.InspectionProjectRegisterContract;
import com.geek.kaijo.mvp.model.entity.Case;
import com.geek.kaijo.mvp.model.entity.IPRegisterBean;
import com.geek.kaijo.mvp.model.entity.Inspection;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import java.util.List;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

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

    /**
     * 获取案件信息
     *
     * @param caseAttribute 案件类型
     */
    public void findThingPositionListBy(String assortId,String caseAttribute,String userId) {
        mModel.findThingPositionListBy(assortId, caseAttribute,userId)
                .compose(RxUtils.applySchedulers(mRootView))
                .compose(RxUtils.handleBaseResult(mApplication))
                .subscribeWith(new ErrorHandleSubscriber<List<IPRegisterBean>>(mErrorHandler) {
                    @Override
                    public void onNext(List<IPRegisterBean> inspectionList) {
                        mRootView.httpGetThingListSuccess(inspectionList);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        mRootView.finishRefresh();
                    }
                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        mRootView.finishRefresh();
                    }
                });
    }

}
