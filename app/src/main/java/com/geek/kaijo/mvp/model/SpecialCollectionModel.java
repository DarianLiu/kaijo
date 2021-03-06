package com.geek.kaijo.mvp.model;

import android.app.Application;

import com.geek.kaijo.app.api.AppService;
import com.geek.kaijo.app.api.RequestParamUtils;
import com.geek.kaijo.mvp.model.entity.BaseResult;
import com.geek.kaijo.mvp.model.entity.Street;
import com.geek.kaijo.mvp.model.entity.ThingPositionInfo;
import com.geek.kaijo.mvp.model.entity.UploadFile;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.geek.kaijo.mvp.contract.SpecialCollectionContract;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/12/2019 16:43
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class SpecialCollectionModel extends BaseModel implements SpecialCollectionContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public SpecialCollectionModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<BaseResult<List<Street>>> findAllStreetCommunity(int type) {
        return mRepositoryManager.obtainRetrofitService(AppService.class).dfindAllStreetCommunity(type);
    }

    @Override
    public Observable<BaseResult<ThingPositionInfo>> insertInfo(RequestBody body) {

        return mRepositoryManager.obtainRetrofitService(AppService.class).insertInfo(body);
    }

    @Override
    public Observable<BaseResult<ThingPositionInfo>> updateInfo(RequestBody body) {
        return mRepositoryManager.obtainRetrofitService(AppService.class).updateInfo(body);
    }

    @Override
    public Observable<UploadFile> uploadFile(List<MultipartBody.Part> parts) {
        return mRepositoryManager.obtainRetrofitService(AppService.class).uploadFile(parts);
    }
}