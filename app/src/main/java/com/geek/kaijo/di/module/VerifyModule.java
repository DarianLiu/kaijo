package com.geek.kaijo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.geek.kaijo.mvp.contract.VerifyContract;
import com.geek.kaijo.mvp.model.VerifyModel;


@Module
public class VerifyModule {
    private VerifyContract.View view;

    /**
     * 构建VerifyModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public VerifyModule(VerifyContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    VerifyContract.View provideVerifyView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    VerifyContract.Model provideVerifyModel(VerifyModel model) {
        return model;
    }
}