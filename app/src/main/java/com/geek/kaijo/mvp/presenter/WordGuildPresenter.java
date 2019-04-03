package com.geek.kaijo.mvp.presenter;

import android.app.Application;

import com.geek.kaijo.app.api.RequestParamUtils;
import com.geek.kaijo.app.api.RxUtils;
import com.geek.kaijo.mvp.contract.WordGuildContract;
import com.geek.kaijo.mvp.model.entity.BaseArrayResult;
import com.geek.kaijo.mvp.model.entity.ServiceBean;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import okhttp3.RequestBody;


@ActivityScope
public class WordGuildPresenter extends BasePresenter<WordGuildContract.Model, WordGuildContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public WordGuildPresenter(WordGuildContract.Model model, WordGuildContract.View rootView) {
        super(model, rootView);
    }
    private int currPage = 1;//当前页数

    /**
     * 查询服务项目列表
     *
     * @param isRefresh  是否刷新
     * @param title      文章标题，不是必传，根据文章标题查找时需要传入该参数
     * @param categoryId 文章分类菜单id,
     */
    public void findCmsArticlePage(boolean isRefresh, String title, String categoryId) {
        currPage = isRefresh ? 1 : currPage + 1;
        RequestBody body = RequestParamUtils.findCmsArticlePage(title, categoryId, currPage, 10);
        mModel.findCmsArticlePage(body)
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
                .subscribe(new ErrorHandleSubscriber<BaseArrayResult<ServiceBean>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseArrayResult<ServiceBean> arrayResult) {
                        if (isRefresh) {
                            mRootView.refreshData(arrayResult.getRecords());
                        } else {
                            mRootView.loadMoreData(arrayResult.getRecords());
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
