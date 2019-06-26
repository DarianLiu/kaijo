package com.geek.kaijo.mvp.presenter;

import android.app.Application;

import com.geek.kaijo.app.api.RxUtils;
import com.geek.kaijo.mvp.model.entity.BaseArrayResult;
import com.geek.kaijo.mvp.model.entity.Nodes;
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

import com.geek.kaijo.mvp.contract.NodesContract;
import com.jess.arms.utils.RxLifecycleUtils;

import java.util.List;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/13/2019 15:57
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class NodesPresenter extends BasePresenter<NodesContract.Model, NodesContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    private int currPage = 1;//当前页数

    @Inject
    public NodesPresenter(NodesContract.Model model, NodesContract.View rootView) {
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
     * 笔记列表
     *
     */
    public void httpFindNotepadList(boolean isRefresh,String userId) {
        currPage = isRefresh ? 1 : currPage + 1;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("currPage", currPage);
        jsonObject.addProperty("pageSize", 10);
        jsonObject.addProperty("userId", userId);

        RequestBody requestBody =  RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),
                new Gson().toJson(jsonObject));

        mModel.httpFindNotepadList(requestBody)
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
                .subscribe(new ErrorHandleSubscriber<BaseArrayResult<Nodes>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseArrayResult<Nodes> datas) {
                        if (isRefresh) {
                            mRootView.refreshData(datas.getRecords());
                        } else {
                            mRootView.loadMoreData(datas.getRecords());
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        currPage --;
                        mRootView.finishRefresh();
                        mRootView.finishLoadMore();
                    }
                });
    }

    /**
     * 删除
     */
    public void httpDelNotepad(long notepadIds,int position) {

        mModel.httpDelNotepad(notepadIds)
                .compose(RxUtils.applySchedulers(mRootView))
//                .compose(RxUtils.applySchedulersHide(mRootView))
                .compose(RxUtils.handleBaseResult(mApplication))
                .subscribeWith(new ErrorHandleSubscriber<Nodes>(mErrorHandler) {
                    @Override
                    public void onNext(Nodes ipRegisterBeans) {

//                        mRootView.httpDelNotepadSuccess(ipRegisterBeans,position);
                    }

                    @Override
                    public void onComplete() {
                        mRootView.httpDelNotepadSuccess(position);
                        super.onComplete();

                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);

                    }
                });
    }
}
