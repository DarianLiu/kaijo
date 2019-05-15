package com.geek.kaijo.mvp.contract;

import com.geek.kaijo.mvp.model.entity.BaseResult;
import com.geek.kaijo.mvp.model.entity.Case;
import com.geek.kaijo.mvp.model.entity.CaseInfo;
import com.geek.kaijo.mvp.model.entity.UploadFile;
import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;


public interface HandleDetailContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void updateView(Case data);
        void uploadSuccess(UploadFile uploadPhoto);
        void httpCommitSuccess();
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<BaseResult<Case>> findCaseInfoByMap(String caseId, String caseAttribute,String userId);

        Observable<BaseResult<CaseInfo>> addOrUpdateCaseInfo(String userId,String acceptDate, String streetId, String communityId,
                                                             String gridId, String lat, String lng, String source,
                                                             String address, String description, String caseAttribute,
                                                             String casePrimaryCategory, String caseSecondaryCategory,
                                                             String caseChildCategory, String handleType, String whenType,
                                                             String caseProcessRecordID,List<UploadFile> uploadPhotoList,String handleResultDescription);


        /**
         * 案件处理提交
         * @param userId
         * @param label
         * @param content
         * @param formId
         * @param processId
         * @param curNode
         * @param nextUserId
         * @param firstWorkunit
         * @param uploadPhotoList
         * @return
         */
        Observable<BaseResult<String>> addOperate(String userId,String label, String content, String formId,
                                                             String processId, String curNode, String nextUserId, String firstWorkunit,
                                                            List<UploadFile> uploadPhotoList,String handleResultDescription);
        Observable<UploadFile> uploadFile(List<MultipartBody.Part> parts);
    }
}
