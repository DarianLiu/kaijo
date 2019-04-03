package com.geek.kaijo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.geek.kaijo.mvp.contract.CaseManagerContract;
import com.geek.kaijo.mvp.model.CaseManagerModel;


@Module
public class CaseManagerModule {
    private CaseManagerContract.View view;

    /**
     * 构建CaseManagerModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public CaseManagerModule(CaseManagerContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    CaseManagerContract.View provideCaseManagerView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    CaseManagerContract.Model provideCaseManagerModel(CaseManagerModel model) {
        return model;
    }
}