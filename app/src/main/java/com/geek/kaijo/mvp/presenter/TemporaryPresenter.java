package com.geek.kaijo.mvp.presenter;

import android.app.Application;

import com.geek.kaijo.app.MyApplication;
import com.geek.kaijo.mvp.model.entity.CaseInfo;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import dao.CaseInfoDao;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import com.geek.kaijo.mvp.contract.TemporaryContract;

import java.util.List;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/13/2019 23:38
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class TemporaryPresenter extends BasePresenter<TemporaryContract.Model, TemporaryContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public TemporaryPresenter(TemporaryContract.Model model, TemporaryContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 获取user
     */
    public void dbGetCaseList() {
        Observable<List<CaseInfo>> observable = Observable.create(new ObservableOnSubscribe<List<CaseInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<List<CaseInfo>> emitter) throws Exception {
                List<CaseInfo> caseInfos =  MyApplication.get().getDaoSession().getCaseInfoDao().queryBuilder().orderDesc(CaseInfoDao.Properties.CaseId).list();
                emitter.onNext(caseInfos);
                emitter.onComplete();
            }
        });
        //创建一个下游 Observer
        Observer<List<CaseInfo>> observer = new Observer<List<CaseInfo>>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(List<CaseInfo> caseInfoList) {
                mRootView.dbDataSuccess(caseInfoList);
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        };
        //建立连接
        observable.subscribe(observer);

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
