package com.geek.kaijo.mvp.presenter;

import android.app.Application;
import android.content.Intent;

import com.geek.kaijo.app.api.RxUtils;
import com.geek.kaijo.mvp.model.entity.Case;
import com.geek.kaijo.mvp.model.entity.CaseInfo;
import com.geek.kaijo.mvp.model.entity.UploadFile;
import com.geek.kaijo.mvp.ui.activity.UploadActivity;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import javax.inject.Inject;

import com.geek.kaijo.mvp.contract.HandleDetailContract;

import java.util.List;


@ActivityScope
public class HandleDetailPresenter extends BasePresenter<HandleDetailContract.Model, HandleDetailContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public HandleDetailPresenter(HandleDetailContract.Model model, HandleDetailContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 获取案件信息
     *
     * @param caseId        案件ID
     * @param caseAttribute 案件类型
     */
    public void findCaseInfoByMap(String caseId, String caseAttribute) {
        mModel.findCaseInfoByMap(caseId, caseAttribute).compose(RxUtils.applySchedulers(mRootView))
                .compose(RxUtils.handleBaseResult(mApplication))
                .subscribeWith(new ErrorHandleSubscriber<Case>(mErrorHandler) {
                    @Override
                    public void onNext(Case aCase) {
                        mRootView.updateView(aCase);
                    }
                });
    }

    /**
     * 提交案件信息
     *
     * @param acceptDate            案件信息
     * @param streetId              案件街道ID
     * @param communityId           案件社区ID
     * @param gridId                案件网格ID
     * @param lat                   案件经度
     * @param lng                   案件纬度
     * @param source                案件来源
     * @param address               案件地址
     * @param description           案件描述
     * @param caseAttribute         案件属性
     * @param casePrimaryCategory   案件大类
     * @param caseSecondaryCategory 案件小类
     * @param caseChildCategory     案件子类
     */
    public void addOrUpdateCaseInfo(String userId,String acceptDate, String streetId, String communityId,
                                    String gridId, String lat, String lng, String source,
                                    String address, String description, String caseAttribute,
                                    String casePrimaryCategory, String caseSecondaryCategory,
                                    String caseChildCategory, String handleType, String whenType,
                                    String caseProcessRecordID,List<UploadFile> uploadPhotoList) {
        mModel.addOrUpdateCaseInfo(userId,acceptDate, streetId, communityId, gridId, lat, lng, source,
                address, description, caseAttribute, casePrimaryCategory, caseSecondaryCategory,
                caseChildCategory, handleType, whenType, caseProcessRecordID,uploadPhotoList)
                .compose(RxUtils.applySchedulers(mRootView))
                .compose(RxUtils.handleBaseResult(mApplication))
                .subscribeWith(new ErrorHandleSubscriber<CaseInfo>(mErrorHandler) {
                    @Override
                    public void onNext(CaseInfo caseInfoEntity) {
                        Intent intent = new Intent(mAppManager.getTopActivity(), UploadActivity.class);
                        intent.putExtra("case_id", caseInfoEntity.getCaseId());
                        mRootView.launchActivity(intent);
//                        mRootView.killMyself();
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
