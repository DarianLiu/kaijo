package com.geek.kaijo.mvp.presenter;

import android.app.Application;

import com.geek.kaijo.app.MyApplication;
import com.geek.kaijo.app.api.RxUtils;
import com.geek.kaijo.mvp.contract.InspectionProjectRegisterContract;
import com.geek.kaijo.mvp.model.entity.IPRegisterBean;
import com.geek.kaijo.mvp.model.entity.Inspection;
import com.geek.kaijo.mvp.model.entity.InspentionResult;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.LogUtils;

import java.util.List;

import javax.inject.Inject;

import dao.DaoSession;
import dao.IPRegisterBeanDao;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import okhttp3.MediaType;
import okhttp3.RequestBody;


@ActivityScope
public class InspectionProjectRegisterPresenter extends BasePresenter<InspectionProjectRegisterContract.Model, InspectionProjectRegisterContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public InspectionProjectRegisterPresenter(InspectionProjectRegisterContract.Model model, InspectionProjectRegisterContract.View rootView) {
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

    /**
     * http巡查项列表
     */
    public void findThingPositionListBy(String streetId, String communityId, String gridId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("streetId", streetId);
        jsonObject.addProperty("communityId", communityId);
        jsonObject.addProperty("gridId", gridId);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),
                new Gson().toJson(jsonObject));

        mModel.findThingPositionListBy(requestBody)
//                .compose(RxUtils.applySchedulers(mRootView))
                .compose(RxUtils.applySchedulersHide(mRootView))
                .compose(RxUtils.handleBaseResult(mApplication))
                .subscribeWith(new ErrorHandleSubscriber<List<IPRegisterBean>>(mErrorHandler) {
                    @Override
                    public void onNext(List<IPRegisterBean> ipRegisterBeans) {

//                        mRootView.httpGetThingListSuccess(ipRegisterBeans);
                        dbShowContent(ipRegisterBeans);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        mRootView.finishRefresh();
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        mRootView.finishRefresh();
                    }
                });
    }


    /**
     * 获取http数据后需比较本地状态是否已经巡查
     *
     * @param httpResult
     */
    public void dbShowContent(List<IPRegisterBean> httpResult) {
        Observable<List<IPRegisterBean>> observable = Observable.create(new ObservableOnSubscribe<List<IPRegisterBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<IPRegisterBean>> emitter) throws Exception {

                LogUtils.debugInfo("111111111111111111111线程名："+Thread.currentThread().getName());

                DaoSession daoSession1 = MyApplication.get().getDaoSession();
                IPRegisterBeanDao ipRegisterBeanDao = daoSession1.getIPRegisterBeanDao();
                List<IPRegisterBean> dbresult = ipRegisterBeanDao.loadAll();
//                if (httpResult != null && dbresult != null) {
//                    for (int i = 0; i < httpResult.size(); i++) {
//                        for (int k = 0; k < dbresult.size(); k++) {
//                            if (httpResult.get(i).getThingPositionId() == dbresult.get(k).getThingPositionId()) {
//                                httpResult.get(i).setStatus(dbresult.get(k).getStatus());
//                            }
//                        }
//                    }
//                }

                for(int i=0;i<dbresult.size();i++){
                    boolean flag = true;
                    for(int k = 0;k<httpResult.size();k++){
                        if (httpResult.get(k).getThingPositionId() == dbresult.get(i).getThingPositionId()) {
                            httpResult.get(k).setStatus(dbresult.get(i).getStatus());
                            flag = false;
                            break;
                        }
                    }
                    if(flag){
                        ipRegisterBeanDao.delete(dbresult.get(i));
                    }
                }

                ipRegisterBeanDao.insertOrReplaceInTx(httpResult);

                emitter.onNext(httpResult);
                emitter.onComplete();

            }
        });
        //创建一个下游 Observer
        Observer<List<IPRegisterBean>> observer = new Observer<List<IPRegisterBean>>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(List<IPRegisterBean> result) {
                LogUtils.debugInfo("111111111111111111111线程名噶："+Thread.currentThread().getName());
                mRootView.dbHttpShowContent(result);
            }

            @Override
            public void onError(Throwable e) {
//                mRootView.preError();
            }

            @Override
            public void onComplete() {
            }
        };
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer); //建立连接
    }


    /**
     * 巡查项数据库获取
     */
    public void dbFindThingList() {
        Observable<List<IPRegisterBean>> observable = Observable.create(new ObservableOnSubscribe<List<IPRegisterBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<IPRegisterBean>> emitter) throws Exception {

                DaoSession daoSession1 = MyApplication.get().getDaoSession();
                IPRegisterBeanDao ipRegisterBeanDao = daoSession1.getIPRegisterBeanDao();
                List<IPRegisterBean> ipRegisterBeanList = ipRegisterBeanDao.loadAll();

                emitter.onNext(ipRegisterBeanList);
                emitter.onComplete();

            }
        });
        //创建一个下游 Observer
        Observer<List<IPRegisterBean>> observer = new Observer<List<IPRegisterBean>>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(List<IPRegisterBean> result) {
                mRootView.dbGetThingListSuccess(result);
            }

            @Override
            public void onError(Throwable e) {
//                mRootView.preError();
            }

            @Override
            public void onComplete() {
            }
        };

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer); //建立连接


    }

    /**
     * 巡查结束  更改状态
     */
    public void dbEndState( List<IPRegisterBean> list) {
        Observable<List<IPRegisterBean>> observable = Observable.create(new ObservableOnSubscribe<List<IPRegisterBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<IPRegisterBean>> emitter) throws Exception {

                DaoSession daoSession1 = MyApplication.get().getDaoSession();
                IPRegisterBeanDao ipRegisterBeanDao = daoSession1.getIPRegisterBeanDao();
                ipRegisterBeanDao.insertOrReplaceInTx(list);

                emitter.onNext(list);
                emitter.onComplete();

            }
        });
        //创建一个下游 Observer
        Observer<List<IPRegisterBean>> observer = new Observer<List<IPRegisterBean>>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(List<IPRegisterBean> result) {
                mRootView.dbEndStateSuccess();

            }

            @Override
            public void onError(Throwable e) {
//                mRootView.preError();
            }

            @Override
            public void onComplete() {
            }
        };

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer); //建立连接


    }


    /**
     * 开始巡查
     */
    public void startPath(String userId, int state) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", userId);
        jsonObject.addProperty("state", state);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),
                new Gson().toJson(jsonObject));
        mModel.startPath(requestBody)
                .compose(RxUtils.applySchedulers(mRootView))
                .compose(RxUtils.handleBaseResult(mApplication))
                .subscribeWith(new ErrorHandleSubscriber<InspentionResult>(mErrorHandler) {
                    @Override
                    public void onNext(InspentionResult result) {
                        mRootView.httpStartSuccess(result);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
//                        mRootView.finishRefresh();
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
//                        mRootView.finishRefresh();
                    }
                });
    }

    /**
     * 结束巡查
     */
    public void endPath(String userId, int state, List<IPRegisterBean> list,int pathId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", userId);
        jsonObject.addProperty("state", state);
        jsonObject.addProperty("pathId", pathId);
        if(list!=null && list.size()>0){
            JsonArray jsonArray = new JsonArray();
            for(int i=0;i<list.size();i++){
                if(list.get(i).getStatus()==1){
                    JsonObject object = new JsonObject();
                    object.addProperty("thingPositionId",list.get(i).getThingPositionId());
                    object.addProperty("arriveTime",list.get(i).getArriveTime());
                    jsonArray.add(object);
                }
            }
            jsonObject.addProperty("thingPositions",jsonArray.toString());
//            jsonObject.add("thingPositions",jsonArray);
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),
                new Gson().toJson(jsonObject));
        mModel.endPath(requestBody)
                .compose(RxUtils.applySchedulers(mRootView))
                .compose(RxUtils.handleBaseResult(mApplication))
                .subscribeWith(new ErrorHandleSubscriber<InspentionResult>(mErrorHandler) {
                    @Override
                    public void onNext(InspentionResult result) {

                        mRootView.httpEndSuccess(result);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
//                        mRootView.finishRefresh();
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
//                        mRootView.finishRefresh();
                    }
                });
    }

    /**
     * 取消巡查
     */
    public void cancelPath(String userId, int state,int pathId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", userId);
        jsonObject.addProperty("state", state);
        jsonObject.addProperty("pathId", pathId);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),
                new Gson().toJson(jsonObject));
        mModel.cancelPath(requestBody)
                .compose(RxUtils.applySchedulers(mRootView))
                .compose(RxUtils.handleBaseResult(mApplication))
                .subscribeWith(new ErrorHandleSubscriber<InspentionResult>(mErrorHandler) {
                    @Override
                    public void onNext(InspentionResult result) {

                        mRootView.httpCancelSuccess(result);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
//                        mRootView.finishRefresh();
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
//                        mRootView.finishRefresh();
                    }
                });
    }



}
