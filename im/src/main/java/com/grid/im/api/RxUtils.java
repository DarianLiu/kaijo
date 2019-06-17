/**
 * Copyright 2017 JessYan
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.grid.im.api;

import android.content.Context;
import com.grid.im.app.BaseResult;
import com.jess.arms.mvp.IView;
import com.jess.arms.utils.RxLifecycleUtils;
import com.trello.rxlifecycle2.LifecycleTransformer;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * ================================================
 * 放置便于使用 RxJava 的一些工具类
 * <p>
 * Created by JessYan on 11/10/2016 16:39
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class RxUtils {

    private RxUtils() {
    }

    public static <T> ObservableTransformer<T, T> applySchedulers(final IView view) {
        //隐藏进度条
        return observable -> observable.subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    view.showLoading();//显示进度条
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(view::hideLoading).compose(RxLifecycleUtils.bindToLifecycle(view));
    }

    public static <T> ObservableTransformer<T, T> applySchedulersHide(final IView view) {
        //隐藏进度条
        return observable -> observable.subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
//                    view.showLoading();//显示进度条
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(view::hideLoading).compose(RxLifecycleUtils.bindToLifecycle(view));
    }

    /**
     * 统一返回结果处理
     *
     * @param <T> 泛型
     * @return ObservableTransformer
     */
    public static <T> ObservableTransformer<BaseResult<T>, T> handleBaseResult(Context context) {
        return observable -> observable.flatMap((Function<BaseResult<T>, ObservableSource<T>>) tBaseResponse -> {
            if (tBaseResponse.getResult() == 0) {
                return createData(tBaseResponse.getData());
            } else {
                return Observable.error(new ApiException(tBaseResponse.getMsg(), tBaseResponse.getResult()));
            }
        });
    }



    public static <T> Observable<T> createData(final T t) {
        return Observable.create(emitter -> {
            if(t==null){
                try {
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }else {
                try {
                    emitter.onNext(t);   //传输的对象 不能为null
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
//            try {
//                emitter.onNext(t);
//                emitter.onComplete();
//            } catch (Exception e) {
//                emitter.onError(e);
//            }
        });
    }

    /**
     * 此接口已废弃
     *
     * @param view
     * @param <T>
     * @return
     * @see RxLifecycleUtils 使用此类代替
     */
    @Deprecated
    public static <T> LifecycleTransformer<T> bindToLifecycle(IView view) {
        return RxLifecycleUtils.bindToLifecycle(view);
    }

}
