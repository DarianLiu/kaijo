package com.geek.kaijo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.geek.kaijo.mvp.contract.HandleDetailContract;
import com.geek.kaijo.mvp.model.HandleDetailModel;


@Module
public class HandleDetailModule {
    private HandleDetailContract.View view;

    /**
     * 构建HandleDetailModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public HandleDetailModule(HandleDetailContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    HandleDetailContract.View provideHandleDetailView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    HandleDetailContract.Model provideHandleDetailModel(HandleDetailModel model) {
        return model;
    }
}