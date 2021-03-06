package com.geek.kaijo.mvp.model;

import android.app.Application;

import com.geek.kaijo.app.api.AppService;
import com.geek.kaijo.app.api.RequestParamUtils;
import com.geek.kaijo.mvp.contract.ReportContract;
import com.geek.kaijo.mvp.model.entity.BaseResult;
import com.geek.kaijo.mvp.model.entity.CaseInfo;
import com.geek.kaijo.mvp.model.entity.CaseAttribute;
import com.geek.kaijo.mvp.model.entity.Grid;
import com.geek.kaijo.mvp.model.entity.Street;
import com.geek.kaijo.mvp.model.entity.UploadFile;
import com.geek.kaijo.mvp.model.entity.User;
import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


@ActivityScope
public class ReportModel extends BaseModel implements ReportContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public ReportModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<BaseResult<List<CaseAttribute>>> findCaseCategoryListByAttribute(String caseCategory) {
        return mRepositoryManager.obtainRetrofitService(AppService.class).findCaseCategoryListByAttribute(caseCategory);
    }

    @Override
    public Observable<BaseResult<List<Street>>> findAllStreetCommunity(int type) {
        return mRepositoryManager.obtainRetrofitService(AppService.class).dfindAllStreetCommunity(type);
    }

    @Override
    public Observable<BaseResult<List<Grid>>> findGridListByCommunityId(String communityId) {
        return mRepositoryManager.obtainRetrofitService(AppService.class).findGridListByCommunityId(communityId);
    }

    @Override
    public Observable<BaseResult<CaseInfo>> addOrUpdateCaseInfo(String userId,String acceptDate, String streetId, String communityId,
                                                                String gridId, String lat, String lng, String source,
                                                                String address, String description, String caseAttribute,
                                                                String casePrimaryCategory, String caseSecondaryCategory,
                                                                String caseChildCategory, String handleType, String whenType,
                                                                String caseProcessRecordID,List<UploadFile> uploadPhotoList,String handleResultDescription) {
        RequestBody requestBody = RequestParamUtils.addOrUpdateCaseInfo(userId,acceptDate, streetId,
                communityId, gridId, lat, lng, source, address, description, caseAttribute,
                casePrimaryCategory, caseSecondaryCategory, caseChildCategory, handleType, whenType, caseProcessRecordID,uploadPhotoList,handleResultDescription);
        return mRepositoryManager.obtainRetrofitService(AppService.class).addOrUpdateCaseInfo(requestBody);
    }

    @Override
    public Observable<UploadFile> uploadFile(List<MultipartBody.Part> parts) {
        return mRepositoryManager.obtainRetrofitService(AppService.class).uploadFile(parts);
    }


    @Override
    public Observable<BaseResult<String>> addCaseAttach(RequestBody info) {
        return mRepositoryManager.obtainRetrofitService(AppService.class).addCaseAttach(info);
    }
}