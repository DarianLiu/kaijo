package com.geek.kaijo.mvp.contract;

import com.geek.kaijo.mvp.model.entity.BaseResult;
import com.geek.kaijo.mvp.model.entity.CaseInfo;
import com.geek.kaijo.mvp.model.entity.UploadFile;
import com.geek.kaijo.mvp.model.entity.UserInfo;
import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public interface MyInfoContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void uploadSuccess(UploadFile uploadPhoto);

        void uploadPhotoError();
        void httpUpdateUserSuccess(UserInfo userInfo);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<UploadFile> uploadFile(List<MultipartBody.Part> parts);
        Observable<BaseResult<UserInfo>> httpUpdateUserForApp(RequestBody body);
    }
}
