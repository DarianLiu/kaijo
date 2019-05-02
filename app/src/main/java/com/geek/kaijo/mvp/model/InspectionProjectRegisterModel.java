package com.geek.kaijo.mvp.model;

import android.app.Application;

import com.geek.kaijo.app.api.AppService;
import com.geek.kaijo.mvp.model.entity.BaseResult;
import com.geek.kaijo.mvp.model.entity.IPRegisterBean;
import com.geek.kaijo.mvp.model.entity.Inspection;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.geek.kaijo.mvp.contract.InspectionProjectRegisterContract;

import java.util.List;

import io.reactivex.Observable;


@ActivityScope
public class InspectionProjectRegisterModel extends BaseModel implements InspectionProjectRegisterContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public InspectionProjectRegisterModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<BaseResult<List<IPRegisterBean>>> findThingPositionListBy(String assortId, String caseAttribute, String userId) {
        return mRepositoryManager.obtainRetrofitService(AppService.class).findThingPositionListBy(assortId,caseAttribute,userId);
    }
}