package com.geek.kaijo.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.geek.kaijo.di.module.SocialManageModule;

import com.geek.kaijo.mvp.ui.activity.SocialManageActivity;

@ActivityScope
@Component(modules = SocialManageModule.class, dependencies = AppComponent.class)
public interface SocialManageComponent {
    void inject(SocialManageActivity activity);
}