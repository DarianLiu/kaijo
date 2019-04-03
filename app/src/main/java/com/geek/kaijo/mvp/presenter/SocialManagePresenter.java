package com.geek.kaijo.mvp.presenter;

import android.app.Application;

import com.geek.kaijo.app.api.RxUtils;
import com.geek.kaijo.mvp.model.entity.SocialThing;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

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
     * @param assortId    是	Long	分类ID
     * @param streetId    否	Long	所属街道
     * @param communityId 否	Long	所属社区
     * @param gridId      否	Long	所属网格
     * @param thingType   是	Integer	物件类型
     * @param name        否	String	物件商店名称
     */
    public void findThingPositionList(boolean isRefresh, long assortId, long streetId, long communityId, long gridId, int thingType, String name) {
        currPage = isRefresh ? 1 : currPage + 1;
        mModel.findThingPositionList(currPage, 10, assortId, streetId, communityId, gridId, thingType, name)
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
                .subscribe(new ErrorHandleSubscriber<List<SocialThing>>(mErrorHandler) {
                    @Override
                    public void onNext(List<SocialThing> datas) {
                        if (isRefresh) {
                            mRootView.refreshData(datas);
                        } else {
                            mRootView.loadMoreData(datas);
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
