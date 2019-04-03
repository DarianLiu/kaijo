package com.geek.kaijo.mvp.presenter;

import android.app.Application;

import com.geek.kaijo.app.api.RequestParamUtils;
import com.geek.kaijo.app.api.RxUtils;
import com.geek.kaijo.mvp.model.entity.BaseArrayResult;
import com.geek.kaijo.mvp.model.entity.CaseAttribute;
import com.geek.kaijo.mvp.model.entity.CaseInfo;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import okhttp3.RequestBody;

import javax.inject.Inject;

import com.geek.kaijo.mvp.contract.CaseSearchContract;

import java.util.List;


@ActivityScope
public class CaseSearchPresenter extends BasePresenter<CaseSearchContract.Model, CaseSearchContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public CaseSearchPresenter(CaseSearchContract.Model model, CaseSearchContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 获得案件属性列表
     *
     * @param caseCategory 案件类型
     */
    public void findCaseCategoryListByAttribute(int caseCategory) {
        mModel.findCaseCategoryListByAttribute(String.valueOf(caseCategory))
                .compose(RxUtils.applySchedulers(mRootView))
                .compose(RxUtils.handleBaseResult(mApplication))
                .subscribeWith(new ErrorHandleSubscriber<List<CaseAttribute>>(mErrorHandler) {
                    @Override
                    public void onNext(List<CaseAttribute> caseAttributeList) {
                        mRootView.setCaseAttributeList(caseAttributeList);
                    }
                });
    }

    /**
     * 案件搜索
     *
     * @param caseCode              案件编号：非必须项
     * @param caseAttribute         案件属性：必须项
     * @param casePrimaryCategory   案件大类：必须项
     * @param caseSecondaryCategory 案件小类：必须项
     * @param caseChildCategory     案件子类：必须项
     */
    public void findCaseInfoList(String caseCode, String caseAttribute, String casePrimaryCategory,
                                 String caseSecondaryCategory, String caseChildCategory) {
        RequestBody body = RequestParamUtils.findCaseInfoList(caseCode, caseAttribute, casePrimaryCategory, caseSecondaryCategory, caseChildCategory);
        mModel.findCaseInfoList(body)
                .compose(RxUtils.applySchedulers(mRootView))
                .compose(RxUtils.handleBaseResult(mApplication))
                .subscribeWith(new ErrorHandleSubscriber<BaseArrayResult<CaseInfo>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseArrayResult<CaseInfo> arrayResult) {
                        mRootView.setCaseSearchResult(arrayResult.getRecords());
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
