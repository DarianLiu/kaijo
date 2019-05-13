package com.geek.kaijo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.geek.kaijo.mvp.contract.ComponentDetailContract;
import com.geek.kaijo.mvp.model.ComponentDetailModel;


@Module
public class ComponentDetailModule {
    private ComponentDetailContract.View view;

    /**
     * 构建ComponentDetailModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ComponentDetailModule(ComponentDetailContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ComponentDetailContract.View provideComponentDetailView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ComponentDetailContract.Model provideComponentDetailModel(ComponentDetailModel model) {
        return model;
    }
}