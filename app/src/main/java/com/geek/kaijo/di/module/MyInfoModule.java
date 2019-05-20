package com.geek.kaijo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.geek.kaijo.mvp.contract.MyInfoContract;
import com.geek.kaijo.mvp.model.MyInfoModel;


@Module
public class MyInfoModule {
    private MyInfoContract.View view;

    /**
     * 构建MyInfoModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public MyInfoModule(MyInfoContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    MyInfoContract.View provideMyInfoView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    MyInfoContract.Model provideMyInfoModel(MyInfoModel model) {
        return model;
    }
}