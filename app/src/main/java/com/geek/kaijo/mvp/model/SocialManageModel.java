package com.geek.kaijo.mvp.model;

import android.app.Application;

import com.geek.kaijo.app.api.AppService;
import com.geek.kaijo.mvp.model.entity.BaseResult;
import com.geek.kaijo.mvp.model.entity.SocialThing;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.geek.kaijo.mvp.contract.SocialManageContract;

import java.util.List;

import io.reactivex.Observable;


@ActivityScope
public class SocialManageModel extends BaseModel implements SocialManageContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public SocialManageModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<BaseResult<List<SocialThing>>> findThingPositionList(int currPage, int pageSize, long assortId, long streetId, long communityId, long gridId, int thingType, String name) {
        return mRepositoryManager.obtainRetrofitService(AppService.class).findThingPositionList(currPage, pageSize, assortId, streetId, communityId, gridId, thingType, name);
    }
}