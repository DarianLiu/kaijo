package com.geek.kaijo.mvp.model;

import android.app.Application;

import com.geek.kaijo.app.api.AppService;
import com.geek.kaijo.app.api.RequestParamUtils;
import com.geek.kaijo.mvp.model.entity.BaseResult;
import com.geek.kaijo.mvp.model.entity.Case;
import com.geek.kaijo.mvp.model.entity.CaseInfo;
import com.geek.kaijo.mvp.model.entity.UploadFile;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.geek.kaijo.mvp.contract.HandleDetailContract;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;


@ActivityScope
public class HandleDetailModel extends BaseModel implements HandleDetailContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public HandleDetailModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<BaseResult<Case>> findCaseInfoByMap(String caseId, String caseAttribute) {
        return mRepositoryManager.obtainRetrofitService(AppService.class).findCaseInfoByMap(caseId, caseAttribute);
    }

    @Override
    public Observable<BaseResult<CaseInfo>> addOrUpdateCaseInfo(String userId,String acceptDate, String streetId, String communityId,
                                                                String gridId, String lat, String lng, String source,
                                                                String address, String description, String caseAttribute,
                                                                String casePrimaryCategory, String caseSecondaryCategory,
                                                                String caseChildCategory, String handleType, String whenType,
                                                                String caseProcessRecordID,List<UploadFile> uploadPhotoList) {
        RequestBody requestBody = RequestParamUtils.addOrUpdateCaseInfo(userId,acceptDate, streetId,
                communityId, gridId, lat, lng, source, address, description, caseAttribute,
                casePrimaryCategory, caseSecondaryCategory, caseChildCategory, handleType, whenType, caseProcessRecordID,uploadPhotoList);
        return mRepositoryManager.obtainRetrofitService(AppService.class).addOrUpdateCaseInfo(requestBody);
    }
}