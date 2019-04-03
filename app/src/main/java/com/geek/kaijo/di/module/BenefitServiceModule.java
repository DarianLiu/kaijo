package com.geek.kaijo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.geek.kaijo.mvp.contract.BenefitServiceContract;
import com.geek.kaijo.mvp.model.BenefitServiceModel;


@Module
public class BenefitServiceModule {
    private BenefitServiceContract.View view;

    /**
     * 构建BenefitServiceModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public BenefitServiceModule(BenefitServiceContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    BenefitServiceContract.View provideBenefitServiceView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    BenefitServiceContract.Model provideBenefitServiceModel(BenefitServiceModel model) {
        return model;
    }
}