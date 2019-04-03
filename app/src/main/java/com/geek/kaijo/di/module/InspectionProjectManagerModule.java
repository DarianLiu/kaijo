package com.geek.kaijo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.geek.kaijo.mvp.contract.InspectionProjectManagerContract;
import com.geek.kaijo.mvp.model.InspectionProjectManagerModel;


@Module
public class InspectionProjectManagerModule {
    private InspectionProjectManagerContract.View view;

    /**
     * 构建InspectionProjectManagerModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public InspectionProjectManagerModule(InspectionProjectManagerContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    InspectionProjectManagerContract.View provideInspectionProjectManagerView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    InspectionProjectManagerContract.Model provideInspectionProjectManagerModel(InspectionProjectManagerModel model) {
        return model;
    }
}