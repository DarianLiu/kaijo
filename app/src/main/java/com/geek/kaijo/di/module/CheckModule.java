package com.geek.kaijo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.geek.kaijo.mvp.contract.CheckContract;
import com.geek.kaijo.mvp.model.CheckModel;


@Module
public class CheckModule {
    private CheckContract.View view;

    /**
     * 构建CheckModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public CheckModule(CheckContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    CheckContract.View provideCheckView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    CheckContract.Model provideCheckModel(CheckModel model) {
        return model;
    }
}