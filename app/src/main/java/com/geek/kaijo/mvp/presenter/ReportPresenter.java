package com.geek.kaijo.mvp.presenter;

import android.app.Application;
import android.content.Intent;

import com.geek.kaijo.app.api.RxUtils;
import com.geek.kaijo.mvp.contract.ReportContract;
import com.geek.kaijo.mvp.model.entity.CaseAttribute;
import com.geek.kaijo.mvp.model.entity.CaseInfo;
import com.geek.kaijo.mvp.model.entity.Grid;
import com.geek.kaijo.mvp.model.entity.Street;
import com.geek.kaijo.mvp.model.entity.UploadCaseFile;
import com.geek.kaijo.mvp.model.entity.UploadFile;
import com.geek.kaijo.mvp.model.entity.User;
import com.geek.kaijo.mvp.ui.activity.UploadActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


@ActivityScope
public class ReportPresenter extends BasePresenter<ReportContract.Model, ReportContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public ReportPresenter(ReportContract.Model model, ReportContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 获得案件属性列表
     *
     * @param caseCategory 案件类型
     */
    public void findCaseCategoryListByAttribute(int caseCategory) {
        mModel.findCaseCategoryListByAttribute(String.valueOf(caseCategory))
                .compose(RxUtils.applySchedulers(mRootView))
                .compose(RxUtils.handleBaseResult(mApplication))
                .subscribeWith(new ErrorHandleSubscriber<List<CaseAttribute>>(mErrorHandler) {
                    @Override
                    public void onNext(List<CaseAttribute> caseAttributeList) {
                        mRootView.setCaseAttributeList(caseAttributeList);
                    }
                });
    }

    /**
     * 获取所有街道社区列表
     */
    public void findAllStreetCommunity(int type) {
        mModel.findAllStreetCommunity(type).subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
//                                disposable.dispose();
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() {
                        mRootView.finishRefresh();//隐藏进度条
                    }
                }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .compose(RxUtils.handleBaseResult(mApplication))
                .subscribeWith(new ErrorHandleSubscriber<List<Street>>(mErrorHandler) {
                    @Override
                    public void onNext(List<Street> streetList) {
                        mRootView.setAllStreetCommunity(streetList);
                    }
                });
    }


    /**
     * 根据社区id，获取网格列表
     *
     * @param communityId 社区id
     */
    public void findGridListByCommunityId(String communityId) {
        mModel.findGridListByCommunityId(communityId).compose(RxUtils.applySchedulers(mRootView))
                .compose(RxUtils.handleBaseResult(mApplication))
                .subscribeWith(new ErrorHandleSubscriber<List<Grid>>(mErrorHandler) {
                    @Override
                    public void onNext(List<Grid> gridList) {
                        mRootView.setGridList(gridList);
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
    public void addOrUpdateCaseInfo(String acceptDate, String streetId, String communityId,
                                    String gridId, String lat, String lng, String source,
                                    String address, String description, String caseAttribute,
                                    String casePrimaryCategory, String caseSecondaryCategory,
                                    String caseChildCategory, String handleType, String whenType,
                                    String caseProcessRecordID) {
        mModel.addOrUpdateCaseInfo(acceptDate, streetId, communityId, gridId, lat, lng, source,
                address, description, caseAttribute, casePrimaryCategory, caseSecondaryCategory,
                caseChildCategory, handleType, whenType, caseProcessRecordID)
                .compose(RxUtils.applySchedulers(mRootView))
                .compose(RxUtils.handleBaseResult(mApplication))
                .subscribeWith(new ErrorHandleSubscriber<CaseInfo>(mErrorHandler) {
                    @Override
                    public void onNext(CaseInfo caseInfoEntity) {


                        Intent intent = new Intent(mAppManager.getTopActivity(), UploadActivity.class);
                        intent.putExtra("caseInfo", caseInfoEntity);
                        mRootView.launchActivity(intent);
//                        mRootView.killMyself();
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

                });
    }


    /**
     * 上传案件
     */
    public void addCaseAttach(List<UploadCaseFile> caseFileList) {
        if (caseFileList == null || caseFileList.size() == 0)
            return;
        String jsonString = new Gson().toJson(caseFileList, new TypeToken<List<UploadCaseFile>>() {
        }.getType());

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonString);
//        mRootView.showLoading();
        mModel.addCaseAttach(body).compose(RxUtils.applySchedulers(mRootView))
                .compose(RxUtils.handleBaseResult(mApplication))
                .subscribe(new ErrorHandleSubscriber<User>(mErrorHandler) {
                    @Override
                    public void onNext(User user) {
                       mRootView.showMessage("案件上报成功");
//                        mRootView.hideLoading();
                        mRootView.killMyself();
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
//                        mRootView.hideLoading();
                    }
                });
    }
}
