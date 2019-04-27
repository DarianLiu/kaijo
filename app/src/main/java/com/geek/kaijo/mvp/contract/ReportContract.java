package com.geek.kaijo.mvp.contract;

import com.geek.kaijo.mvp.model.entity.BaseResult;
import com.geek.kaijo.mvp.model.entity.CaseInfo;
import com.geek.kaijo.mvp.model.entity.CaseAttribute;
import com.geek.kaijo.mvp.model.entity.Grid;
import com.geek.kaijo.mvp.model.entity.Street;
import com.geek.kaijo.mvp.model.entity.UploadFile;
import com.geek.kaijo.mvp.model.entity.User;
import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public interface ReportContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void finishRefresh();

        void setCaseAttributeList(List<CaseAttribute> attributeList);

        void setAllStreetCommunity(List<Street> list);

        void setGridList(List<Grid> list);

        void uploadSuccess(UploadFile uploadPhoto);

        void uploadCaseInfoSuccess(CaseInfo caseInfoEntity);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<BaseResult<List<CaseAttribute>>> findCaseCategoryListByAttribute(String caseCategory);

        Observable<BaseResult<List<Street>>> findAllStreetCommunity(int type);

        Observable<BaseResult<List<Grid>>> findGridListByCommunityId(String communityId);

        Observable<BaseResult<CaseInfo>> addOrUpdateCaseInfo(String userId,String acceptDate, String streetId, String communityId,
                                                             String gridId, String lat, String lng, String source,
                                                             String address, String description, String caseAttribute,
                                                             String casePrimaryCategory, String caseSecondaryCategory,
                                                             String caseChildCategory, String handleType, String whenType,
                                                             String caseProcessRecordID,List<UploadFile> uploadPhotoList);


        Observable<UploadFile> uploadFile(List<MultipartBody.Part> parts);
        Observable<BaseResult<String>> addCaseAttach(RequestBody info);
    }
}
