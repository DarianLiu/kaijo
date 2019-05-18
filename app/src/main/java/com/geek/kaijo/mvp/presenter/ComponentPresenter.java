package com.geek.kaijo.mvp.presenter;

import android.app.Application;

import com.geek.kaijo.app.Constant;
import com.geek.kaijo.app.api.RxUtils;
import com.geek.kaijo.mvp.contract.ComponentContract;
import com.geek.kaijo.mvp.model.entity.Menu;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.DataHelper;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;


@ActivityScope
public class ComponentPresenter extends BasePresenter<ComponentContract.Model, ComponentContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public ComponentPresenter(ComponentContract.Model model, ComponentContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 文件取
     */
    public void preListInfoMenu() {
        Observable<List<Menu>> observable = Observable.create(new ObservableOnSubscribe<List<Menu>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Menu>> emitter) throws Exception {
                List<Menu> menuList = DataHelper.getDeviceData(mApplication, Constant.SP_KEY_MENUS);
                emitter.onNext(menuList);
                emitter.onComplete();
            }
        });
        //创建一个下游 Observer
        Observer<List<Menu>> observer = new Observer<List<Menu>>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(List<Menu> menuList) {
                mRootView.preListInfoMenuSuccess(menuList);
            }

            @Override
            public void onError(Throwable e) {
                mRootView.preError();
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
     * 部件采集菜单
     */
    public void httpListInfoMenu() {
        mModel.listInfoMenu().compose(RxUtils.applySchedulers(mRootView))
                .compose(RxUtils.handleBaseResult(mApplication))
                .subscribeWith(new ErrorHandleSubscriber<List<Menu>>(mErrorHandler) {
                    @Override
                    public void onNext(List<Menu> menuList) {
                        if (menuList != null && menuList.size() > 0) {
                            mRootView.httpListInfoMenuSuccess(menuList);
                            preSaveMenuList(menuList);
                        }
                    }
                });
    }


    /**
     * 获取user
     */
    public void preSaveMenuList(List<Menu> menuList) {
        Observable<List<Menu>> observable = Observable.create(new ObservableOnSubscribe<List<Menu>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Menu>> emitter) throws Exception {
//                User user = prefHelper.getPrefObject(Prefs.key_user, User.class);
                DataHelper.saveDeviceData(mApplication, Constant.SP_KEY_MENUS, menuList);
                emitter.onNext(menuList);
                emitter.onComplete();
            }
        });
        //创建一个下游 Observer
        Observer<List<Menu>> observer = new Observer<List<Menu>>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(List<Menu> menuList) {

            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        };
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer); //建立连接
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
