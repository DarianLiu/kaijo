package com.geek.kaijo.mvp.contract;

import com.geek.kaijo.mvp.model.entity.BaseResult;
import com.geek.kaijo.mvp.model.entity.Street;
import com.geek.kaijo.mvp.model.entity.ThingPositionInfo;
import com.geek.kaijo.mvp.model.entity.UploadFile;
import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;

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
public interface SpecialCollectionContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void httpStreetCommunitySuccess(List<Street> streetList);
        void httpInsertInfoSuccess();
        void uploadFileSuccess(UploadFile uploadPhoto);
        void uploadPhotoSuccess(UploadFile uploadPhoto);

    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<BaseResult<List<Street>>> findAllStreetCommunity(int type);


        Observable<BaseResult<ThingPositionInfo>> insertInfo(RequestBody body);
        Observable<BaseResult<ThingPositionInfo>> updateInfo(RequestBody body);

        Observable<UploadFile> uploadFile(List<MultipartBody.Part> parts);
    }
}
