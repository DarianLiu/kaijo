package com.geek.kaijo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.geek.kaijo.mvp.contract.SocialManageContract;
import com.geek.kaijo.mvp.model.SocialManageModel;


@Module
public class SocialManageModule {
    private SocialManageContract.View view;

    /**
     * 构建SocialManageModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public SocialManageModule(SocialManageContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    SocialManageContract.View provideSocialManageView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    SocialManageContract.Model provideSocialManageModel(SocialManageModel model) {
        return model;
    }
}