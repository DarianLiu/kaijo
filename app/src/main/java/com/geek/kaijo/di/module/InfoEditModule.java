package com.geek.kaijo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.geek.kaijo.mvp.contract.InfoEditContract;
import com.geek.kaijo.mvp.model.InfoEditModel;


@Module
public class InfoEditModule {
    private InfoEditContract.View view;

    /**
     * 构建InfoEditModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public InfoEditModule(InfoEditContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    InfoEditContract.View provideInfoEditView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    InfoEditContract.Model provideInfoEditModel(InfoEditModel model) {
        return model;
    }
}