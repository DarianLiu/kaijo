package com.geek.kaijo.mvp.model;

import android.app.Application;

import com.geek.kaijo.app.api.AppService;
import com.geek.kaijo.mvp.model.entity.BaseArrayResult;
import com.geek.kaijo.mvp.model.entity.BaseResult;
import com.geek.kaijo.mvp.model.entity.Inspection;
import com.geek.kaijo.mvp.model.entity.Thing;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.geek.kaijo.mvp.contract.InspectionProjectManagerContract;

import io.reactivex.Observable;


@ActivityScope
public class InspectionProjectManagerModel extends BaseModel implements InspectionProjectManagerContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public InspectionProjectManagerModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<BaseResult<BaseArrayResult<Thing>>> findAllThingList(int currPage,int pageSize) {
        return mRepositoryManager.obtainRetrofitService(AppService.class).findAllThingList(currPage, pageSize);
    }

    @Override
    public Observable<BaseResult<BaseArrayResult<Inspection>>> findThingPositionListPage(int thingType, String name) {
        return mRepositoryManager.obtainRetrofitService(AppService.class).findThingPositionListPage(thingType,name);
    }

    @Override
    public Observable<BaseResult<Thing>> delThings(String thingIds) {
        return mRepositoryManager.obtainRetrofitService(AppService.class).delThings(thingIds);
    }
}