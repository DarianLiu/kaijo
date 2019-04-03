package com.geek.kaijo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.geek.kaijo.mvp.contract.InspectionAddContract;
import com.geek.kaijo.mvp.model.InspectionAddModel;


@Module
public class InspectionAddModule {
    private InspectionAddContract.View view;

    /**
     * 构建InspectionAddModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public InspectionAddModule(InspectionAddContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    InspectionAddContract.View provideInspectionAddView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    InspectionAddContract.Model provideInspectionAddModel(InspectionAddModel model) {
        return model;
    }
}