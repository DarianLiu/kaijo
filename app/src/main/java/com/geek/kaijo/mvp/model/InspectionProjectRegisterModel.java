package com.geek.kaijo.mvp.model;

import android.app.Application;

import com.geek.kaijo.app.api.AppService;
import com.geek.kaijo.mvp.model.entity.BaseResult;
import com.geek.kaijo.mvp.model.entity.IPRegisterBean;
import com.geek.kaijo.mvp.model.entity.Inspection;
import com.geek.kaijo.mvp.model.entity.InspentionResult;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.geek.kaijo.mvp.contract.InspectionProjectRegisterContract;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;


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
    public Observable<BaseResult<List<IPRegisterBean>>> findThingPositionListBy(RequestBody body) {
        return mRepositoryManager.obtainRetrofitService(AppService.class).findThingPositionListBy(body);
    }

    @Override
    public Observable<BaseResult<InspentionResult>> startPath(RequestBody body) {
        return mRepositoryManager.obtainRetrofitService(AppService.class).startPath(body);
    }

    @Override
    public Observable<BaseResult<InspentionResult>> endPath(RequestBody body) {
        return mRepositoryManager.obtainRetrofitService(AppService.class).endPath(body);
    }

}