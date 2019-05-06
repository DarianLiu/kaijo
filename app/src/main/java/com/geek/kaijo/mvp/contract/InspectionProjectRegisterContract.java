package com.geek.kaijo.mvp.contract;

import com.geek.kaijo.mvp.model.entity.BaseResult;
import com.geek.kaijo.mvp.model.entity.Case;
import com.geek.kaijo.mvp.model.entity.IPRegisterBean;
import com.geek.kaijo.mvp.model.entity.Inspection;
import com.geek.kaijo.mvp.model.entity.InspentionResult;
import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Query;


public interface InspectionProjectRegisterContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void httpGetThingListSuccess(List<IPRegisterBean> inspectionList);
        void httpStartSuccess();
        void httpEndSuccess();
        void finishRefresh();
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
//        Observable<BaseResult<List<IPRegisterBean>>> findThingPositionListBy(String assortId,String caseAttribute,String userId);
        Observable<BaseResult<List<IPRegisterBean>>> findThingPositionListBy(RequestBody body);

        Observable<BaseResult<InspentionResult>> startPath(RequestBody body);
        Observable<BaseResult<InspentionResult>> endPath(RequestBody body);

    }
}
