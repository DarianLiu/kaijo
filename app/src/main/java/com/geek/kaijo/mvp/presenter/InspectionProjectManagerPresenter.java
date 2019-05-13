package com.geek.kaijo.mvp.presenter;

import android.app.Application;

import com.geek.kaijo.app.api.RxUtils;
import com.geek.kaijo.mvp.contract.InspectionProjectManagerContract;
import com.geek.kaijo.mvp.model.entity.BaseArrayResult;
import com.geek.kaijo.mvp.model.entity.IPRegisterBean;
import com.geek.kaijo.mvp.model.entity.Inspection;
import com.geek.kaijo.mvp.model.entity.Thing;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import okhttp3.MediaType;
import okhttp3.RequestBody;


@ActivityScope
public class InspectionProjectManagerPresenter extends BasePresenter<InspectionProjectManagerContract.Model, InspectionProjectManagerContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public InspectionProjectManagerPresenter(InspectionProjectManagerContract.Model model, InspectionProjectManagerContract.View rootView) {
        super(model, rootView);
    }

    private int currPage = 0, pageSize = 10;

//    /**
//     * 获取巡查项目列表
//     *
//     * @param isRefresh 是否刷新
//     */
//    public void getInspectionProjectList(boolean isRefresh) {
//        mModel.findThingPositionListPage(1,"")
//                .subscribeOn(Schedulers.io())
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doFinally(() -> {
//                    if (isRefresh) {
//                        mRootView.finishRefresh();//隐藏刷新
//                    } else {
//                        mRootView.finishLoadMore();//隐藏加载更多
//                    }
//                }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
//                .compose(RxUtils.handleBaseResult(mApplication))
//                .subscribeWith(new ErrorHandleSubscriber<BaseArrayResult<Inspection>>(mErrorHandler) {
//                    @Override
//                    public void onNext(BaseArrayResult<Inspection> arrayResult) {
//                        if (arrayResult.getRecords().size() > 0) {
//                            currPage = isRefresh ? 0 : currPage++;
//                        }
//                        mRootView.updateInspectionProjectList(isRefresh, arrayResult.getRecords());
//                    }
//                });
//    }

    /**
     * http巡查项列表
     */
    public void findThingPositionListBy(String streetId, String communityId, String gridId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("streetId", streetId);
        jsonObject.addProperty("communityId", communityId);
        jsonObject.addProperty("gridId", gridId);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),
                new Gson().toJson(jsonObject));

        mModel.findThingPositionListBy(requestBody)
//                .compose(RxUtils.applySchedulers(mRootView))
                .compose(RxUtils.applySchedulersHide(mRootView))
                .compose(RxUtils.handleBaseResult(mApplication))
                .subscribeWith(new ErrorHandleSubscriber<List<Inspection>>(mErrorHandler) {
                    @Override
                    public void onNext(List<Inspection> ipRegisterBeans) {

//                        mRootView.httpGetThingListSuccess(ipRegisterBeans);
                        mRootView.httpThingListSuccess(ipRegisterBeans);
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
     * 删除巡查项目
     */
    public void delThings(int[] positions, String[] thingIds) {
        String param = Arrays.toString(thingIds).replace("[", "").replace("]", "");
        mModel.delThings(param)
                .compose(RxUtils.applySchedulers(mRootView))
                .compose(RxUtils.handleBaseResult(mApplication))
                .subscribeWith(new ErrorHandleSubscriber<Thing>(mErrorHandler) {
                    @Override
                    public void onNext(Thing thing) {
//                        mRootView.delThings(positions);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        mRootView.delThings(positions);
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
