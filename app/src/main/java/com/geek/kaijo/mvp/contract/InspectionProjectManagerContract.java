package com.geek.kaijo.mvp.contract;

import com.geek.kaijo.mvp.model.entity.BaseArrayResult;
import com.geek.kaijo.mvp.model.entity.BaseResult;
import com.geek.kaijo.mvp.model.entity.Thing;
import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;

import java.util.List;

import io.reactivex.Observable;


public interface InspectionProjectManagerContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void finishRefresh();

        void finishLoadMore();

        void updateInspectionProjectList(boolean isRefresh, List<Thing> list);

        void delThings(int[] positions);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<BaseResult<BaseArrayResult<Thing>>> findAllThingList(int currPage,int pageSize);

        Observable<BaseResult<Thing>> delThings(String thingIds);
    }
}
