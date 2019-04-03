package com.geek.kaijo.di.module;

import com.geek.kaijo.mvp.contract.SocialProductDangerContract;
import com.geek.kaijo.mvp.model.SocialProductDangerModel;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;


@Module
public class SocialProductDangerModule {
    private SocialProductDangerContract.View view;

    /**
     * 构建SocialThingModifyModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public SocialProductDangerModule(SocialProductDangerContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    SocialProductDangerContract.View provideSocialThingModifyView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    SocialProductDangerContract.Model provideSocialThingModifyModel(SocialProductDangerModel model) {
        return model;
    }
}