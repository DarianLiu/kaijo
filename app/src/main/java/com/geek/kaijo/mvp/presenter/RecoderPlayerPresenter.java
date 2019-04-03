package com.geek.kaijo.mvp.presenter;

import android.app.Application;
import android.content.Context;

import com.geek.kaijo.app.api.RxUtils;
import com.geek.kaijo.mvp.model.entity.UploadFile;
import com.geek.kaijo.view.CommProgressDailog;
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

import com.geek.kaijo.mvp.contract.RecoderPlayerContract;

import java.io.File;
import java.util.List;


@ActivityScope
public class RecoderPlayerPresenter extends BasePresenter<RecoderPlayerContract.Model, RecoderPlayerContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    CommProgressDailog dialogUtils;

    @Inject
    public RecoderPlayerPresenter(RecoderPlayerContract.Model model, RecoderPlayerContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    private CommProgressDailog getDailog() {
        if (dialogUtils == null) {
            dialogUtils = new CommProgressDailog();
        }
        return dialogUtils;
    }

    /**
     * 上传图片 单张图片
     *
     * @param
     * @param
     */
    public void uploadFile(Context context,String filePath) {
//        File file = new File(pathUrl);
//        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", "test.txt", file);
        File file = new File(filePath);//filePath 图片地址
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)//表单类型
                .addFormDataPart("fileName", file.getPath());//
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        builder.addFormDataPart("file", file.getName(), imageBody);//imgfile 后台接收图片流的参数名

        List<MultipartBody.Part> parts = builder.build().parts();
        getDailog().showProgressDailog(context,"正在上传文件");
        mModel.uploadFile(parts).compose(RxUtils.applySchedulers(mRootView))
                .compose(RxUtils.handleBaseResultResult(mApplication))
                .subscribeWith(new ErrorHandleSubscriber<UploadFile>(mErrorHandler) {
                    @Override
                    public void onNext(UploadFile uploadPhoto) {
                        getDailog().cancelProgressDailog();
                        mRootView.uploadSuccess(uploadPhoto);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        getDailog().cancelProgressDailog();
                    }
                });
    }

}
