package com.geek.kaijo.di.module;

import com.geek.kaijo.mvp.model.InspectionProjectRegisterModel;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.geek.kaijo.mvp.contract.InspectionProjectRegisterContract;


@Module
public class InspectionProjectRegisterModule {
    private InspectionProjectRegisterContract.View view;

    /**
     * 构建InspectionProjectRegistModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public InspectionProjectRegisterModule(InspectionProjectRegisterContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    InspectionProjectRegisterContract.View provideInspectionProjectRegistView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    InspectionProjectRegisterContract.Model provideInspectionProjectRegistModel(InspectionProjectRegisterModel model) {
        return model;
    }
}