package com.geek.kaijo.mvp.model;

import android.app.Application;

import com.geek.kaijo.app.api.AppService;
import com.geek.kaijo.mvp.model.entity.BaseArrayResult;
import com.geek.kaijo.mvp.model.entity.BaseResult;
import com.geek.kaijo.mvp.model.entity.CaseAttribute;
import com.geek.kaijo.mvp.model.entity.CaseInfo;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.geek.kaijo.mvp.contract.CaseSearchContract;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;


@ActivityScope
public class CaseSearchModel extends BaseModel implements CaseSearchContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public CaseSearchModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<BaseResult<List<CaseAttribute>>> findCaseCategoryListByAttribute(String caseCategory) {
        return mRepositoryManager.obtainRetrofitService(AppService.class).findCaseCategoryListByAttribute(caseCategory);
    }

    @Override
    public Observable<BaseResult<BaseArrayResult<CaseInfo>>> findCaseInfoList(RequestBody body) {
        return mRepositoryManager.obtainRetrofitService(AppService.class).findCaseInfoList(body);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }
}