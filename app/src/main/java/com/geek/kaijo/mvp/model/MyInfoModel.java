package com.geek.kaijo.mvp.model;

import android.app.Application;

import com.geek.kaijo.app.api.AppService;
import com.geek.kaijo.mvp.model.entity.BaseResult;
import com.geek.kaijo.mvp.model.entity.UploadFile;
import com.geek.kaijo.mvp.model.entity.UserInfo;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.geek.kaijo.mvp.contract.MyInfoContract;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


@ActivityScope
public class MyInfoModel extends BaseModel implements MyInfoContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public MyInfoModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<UploadFile> uploadFile(List<MultipartBody.Part> parts) {
        return mRepositoryManager.obtainRetrofitService(AppService.class).uploadFile(parts);
    }

    @Override
    public Observable<BaseResult<UserInfo>> httpUpdateUserForApp(RequestBody body) {
        return mRepositoryManager.obtainRetrofitService(AppService.class).updateUserForApp(body);
    }
}