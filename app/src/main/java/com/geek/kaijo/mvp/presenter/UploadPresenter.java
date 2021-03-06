package com.geek.kaijo.mvp.presenter;

import android.annotation.SuppressLint;
import android.app.Application;

import com.geek.kaijo.app.api.RequestParamUtils;
import com.geek.kaijo.app.api.RxUtils;
import com.geek.kaijo.mvp.contract.UploadContract;
import com.geek.kaijo.mvp.model.entity.CaseInfo;
import com.geek.kaijo.mvp.model.entity.UploadCaseFile;
import com.geek.kaijo.mvp.model.entity.UploadFile;
import com.geek.kaijo.mvp.model.entity.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import javax.inject.Inject;

import java.io.File;
import java.util.List;


@ActivityScope
public class UploadPresenter extends BasePresenter<UploadContract.Model, UploadContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public UploadPresenter(UploadContract.Model model, UploadContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 上传图片 单张图片
     */
    public void uploadFile(String filePath) {
//        File file = new File(pathUrl);
//        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", "test.txt", file);
        File file = new File(filePath);//filePath 图片地址
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)//表单类型
                .addFormDataPart("fileName", file.getPath());//
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        builder.addFormDataPart("file", file.getName(), imageBody);//imgfile 后台接收图片流的参数名

        List<MultipartBody.Part> parts = builder.build().parts();

        mModel.uploadFile(parts).compose(RxUtils.applySchedulers(mRootView))
                .compose(RxUtils.handleBaseResultResult(mApplication))
                .subscribeWith(new ErrorHandleSubscriber<UploadFile>(mErrorHandler) {
                    @Override
                    public void onNext(UploadFile uploadPhoto) {
                        mRootView.uploadSuccess(uploadPhoto);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        mRootView.uploadPhotoError();
                    }
                });
    }


    /**
     * 案件上报
     *
     * @param acceptDate            案发时间
     * @param streetId              街道ID
     * @param communityId           社区ID
     * @param gridId                网格ID
     * @param lat                   纬度
     * @param lng                   经度
     * @param source                来源 网格员默认17
     * @param address               地址
     * @param description           问题描述
     * @param caseAttribute         案件属性
     * @param casePrimaryCategory   案件大类
     * @param caseSecondaryCategory 案件小类
     * @param caseChildCategory     案件子类
     * @param handleType            直接处理传1 ，非直接处理传2
     * @param whenType              直接处理( 整改前的写1  整改后写2),  非直接处理 whenType 1
     * @param caseProcessRecordID   直接处理 caseProcessRecordID  19,  非直接处理 caseProcessRecordID  11
     */
    public void addOrUpdateCaseInfo(String userId,String acceptDate, String streetId, String communityId,
                                    String gridId, String lat, String lng, String source,
                                    String address, String description, String caseAttribute,
                                    String casePrimaryCategory, String caseSecondaryCategory,
                                    String caseChildCategory, String handleType, String whenType,
                                    String caseProcessRecordID,List<UploadFile> uploadPhotoList,String handleResultDescription) {
        RequestBody requestBody = RequestParamUtils.addOrUpdateCaseInfo(userId,acceptDate, streetId, communityId, gridId, lat, lng, source,
                address, description, caseAttribute, casePrimaryCategory, caseSecondaryCategory,
                caseChildCategory, handleType, whenType, caseProcessRecordID,uploadPhotoList,handleResultDescription);

        mModel.addOrUpdateCaseInfo(requestBody)
                .compose(RxUtils.applySchedulers(mRootView))
                .compose(RxUtils.handleBaseResult(mApplication))
                .subscribeWith(new ErrorHandleSubscriber<CaseInfo>(mErrorHandler) {
                    @Override
                    public void onNext(CaseInfo caseInfoEntity) {
                        mRootView.uploadCaseInfoSuccess(caseInfoEntity);
//                        mRootView.killMyself();
                    }
                });
    }



    /**
     * 上传案件
     */
    @SuppressLint("CheckResult")
    public void addCaseAttach(List<UploadCaseFile> caseFileList) {
        if (caseFileList == null || caseFileList.size() == 0) return;
        String jsonString = new Gson().toJson(caseFileList, new TypeToken<List<UploadCaseFile>>() {
        }.getType());

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonString);
//        mRootView.showLoading();
        mModel.addCaseAttach(body).compose(RxUtils.applySchedulers(mRootView))
                .compose(RxUtils.handleBaseResult(mApplication))
                .subscribeWith(new ErrorHandleSubscriber<String>(mErrorHandler) {
                    @Override
                    public void onNext(String user) {
//                        mRootView.hideLoading();
                        mRootView.showMessage("提交成功");
                        mRootView.killMyself();
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
//                        mRootView.hideLoading();
                    }
                });
    }


    /**
     * 上传图片 多张图片
     *
     * @param
     * @param
     */
    public void uploadFileList(List<UploadFile> photoList) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);//表单类型
        for (int i = 0; i < photoList.size(); i++) {
            File file = new File(photoList.get(i).getFileName());//filePath 图片地址
            builder.addFormDataPart("fileName", file.getPath());//
            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            builder.addFormDataPart("file", file.getPath(), imageBody);//imgfile 后台接收图片流的参数名
        }
        List<MultipartBody.Part> parts = builder.build().parts();
        mModel.uploadFile(parts).compose(RxUtils.applySchedulers(mRootView))
                .compose(RxUtils.handleBaseResultResult(mApplication))
                .subscribeWith(new ErrorHandleSubscriber<UploadFile>(mErrorHandler) {
                    @Override
                    public void onNext(UploadFile uploadPhoto) {
                        mRootView.uploadSuccess(uploadPhoto);
                    }

                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }
}
