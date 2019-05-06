package com.geek.kaijo.mvp.presenter;

import android.app.Application;

import com.geek.kaijo.app.api.RxUtils;
import com.geek.kaijo.mvp.model.entity.BaseArrayResult;
import com.geek.kaijo.mvp.model.entity.SocialThing;
import com.geek.kaijo.mvp.model.entity.ThingPositionInfo;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import javax.inject.Inject;

import com.geek.kaijo.mvp.contract.SocialManageContract;
import com.jess.arms.utils.RxLifecycleUtils;

import java.util.List;


@ActivityScope
public class SocialManagePresenter extends BasePresenter<SocialManageContract.Model, SocialManageContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public SocialManagePresenter(SocialManageContract.Model model, SocialManageContract.View rootView) {
        super(model, rootView);
    }

    private int currPage = 1;//当前页数

    /**
     * 物件点位列表
     *
     */
    public void findThingPositionList(boolean isRefresh,String assortType,String name,String danweiName,String jingyingzheName,String address) {
        currPage = isRefresh ? 1 : currPage + 1;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("current", currPage);
        jsonObject.addProperty("size", 10);
        jsonObject.addProperty("assortType", assortType);
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("danweiName", danweiName);
        jsonObject.addProperty("jingyingzheName", jingyingzheName);
        jsonObject.addProperty("address", address);
        RequestBody requestBody =  RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),
                new Gson().toJson(jsonObject));

        mModel.findThingPositionList(requestBody)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    if (isRefresh) {
                        mRootView.finishRefresh();//隐藏刷新
                    } else {
                        mRootView.finishLoadMore();//隐藏加载更多
                    }
                }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .compose(RxUtils.handleBaseResult(mAppManager.getTopActivity()))
                .subscribe(new ErrorHandleSubscriber<BaseArrayResult<ThingPositionInfo>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseArrayResult<ThingPositionInfo> datas) {
                        if (isRefresh) {
                            mRootView.refreshData(datas.getRecords());
                        } else {
                            mRootView.loadMoreData(datas.getRecords());
                        }
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
