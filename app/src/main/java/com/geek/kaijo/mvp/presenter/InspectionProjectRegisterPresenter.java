package com.geek.kaijo.mvp.presenter;

import android.app.Application;

import com.geek.kaijo.app.api.RxUtils;
import com.geek.kaijo.mvp.contract.InspectionProjectRegisterContract;
import com.geek.kaijo.mvp.model.entity.Case;
import com.geek.kaijo.mvp.model.entity.IPRegisterBean;
import com.geek.kaijo.mvp.model.entity.Inspection;
import com.geek.kaijo.mvp.model.entity.InspentionResult;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import java.util.List;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import okhttp3.MediaType;
import okhttp3.RequestBody;

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
     * 巡查项列表
     *
     */
    public void findThingPositionListBy(String streetId,String communityId,String gridId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("streetId", streetId);
        jsonObject.addProperty("communityId", communityId);
        jsonObject.addProperty("gridId", gridId);
//        jsonObject.addProperty("streetId", 120);
//        jsonObject.addProperty("communityId", 151);
//        jsonObject.addProperty("gridId", 276);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),
                new Gson().toJson(jsonObject));

        mModel.findThingPositionListBy(requestBody)
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


    /**
     * 开始巡查
     *
     */
    public void startPath(String userId,int state) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", userId);
        jsonObject.addProperty("state", state);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),
                new Gson().toJson(jsonObject));
        mModel.startPath(requestBody)
                .compose(RxUtils.applySchedulers(mRootView))
                .compose(RxUtils.handleBaseResult(mApplication))
                .subscribeWith(new ErrorHandleSubscriber<InspentionResult>(mErrorHandler) {
                    @Override
                    public void onNext(InspentionResult inspectionList) {
                        mRootView.httpStartSuccess();
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
//                        mRootView.finishRefresh();
                    }
                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
//                        mRootView.finishRefresh();
                    }
                });
    }

    /**
     * 结束巡查
     *
     */
    public void endPath(String userId,int state) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", userId);
        jsonObject.addProperty("state", state);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),
                new Gson().toJson(jsonObject));
        mModel.endPath(requestBody)
                .compose(RxUtils.applySchedulers(mRootView))
                .compose(RxUtils.handleBaseResult(mApplication))
                .subscribeWith(new ErrorHandleSubscriber<InspentionResult>(mErrorHandler) {
                    @Override
                    public void onNext(InspentionResult inspectionList) {
                        mRootView.httpEndSuccess();
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
//                        mRootView.finishRefresh();
                    }
                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
//                        mRootView.finishRefresh();
                    }
                });
    }


}
