package com.geek.kaijo.mvp.model;

import android.app.Application;

import com.geek.kaijo.app.api.AppService;
import com.geek.kaijo.mvp.contract.SocialManageContract;
import com.geek.kaijo.mvp.model.entity.BaseArrayResult;
import com.geek.kaijo.mvp.model.entity.BaseResult;
import com.geek.kaijo.mvp.model.entity.ThingPositionInfo;
import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;


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
    public Observable<BaseResult<BaseArrayResult<ThingPositionInfo>>> findThingPositionList(@Body RequestBody body) {
        return mRepositoryManager.obtainRetrofitService(AppService.class).findThingPositionList(body);
    }

    @Override
    public Observable<BaseResult<ThingPositionInfo>> deleteInfo(RequestBody requestBody) {
        return mRepositoryManager.obtainRetrofitService(AppService.class).deleteInfo(requestBody);
    }
}