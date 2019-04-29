package com.geek.kaijo.mvp.model;

import android.app.Application;

import com.geek.kaijo.app.api.AppService;
import com.geek.kaijo.app.api.RequestParamUtils;
import com.geek.kaijo.mvp.model.entity.BaseResult;
import com.geek.kaijo.mvp.model.entity.Inspection;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.geek.kaijo.mvp.contract.InspectionAddContract;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;


@ActivityScope
public class InspectionAddModel extends BaseModel implements InspectionAddContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public InspectionAddModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<BaseResult<List<Inspection>>> findThingListBy(String assortId) {
        return mRepositoryManager.obtainRetrofitService(AppService.class).findThingListBy(assortId);
    }
    @Override
    public Observable<BaseResult<Inspection>> addOrUpdateThingPosition(int thingId, String name, Double lat, Double lng, int streetId, int communityId, int gridId, String createUser) {



        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("thingId", thingId);
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("lat", lat);
        jsonObject.addProperty("lng", lng);
        jsonObject.addProperty("streetId", streetId);
        jsonObject.addProperty("communityId", communityId);
        jsonObject.addProperty("gridId", gridId);
        jsonObject.addProperty("createUser", createUser);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),
                new Gson().toJson(jsonObject));

        return mRepositoryManager.obtainRetrofitService(AppService.class).addOrUpdateThingPosition(requestBody);
    }

}