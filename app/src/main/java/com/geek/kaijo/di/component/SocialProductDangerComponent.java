package com.geek.kaijo.di.component;

import com.geek.kaijo.di.module.SocialProductDangerModule;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.geek.kaijo.mvp.ui.activity.society.safety.SocialProductDangerActivity;

@ActivityScope
@Component(modules = SocialProductDangerModule.class, dependencies = AppComponent.class)
public interface SocialProductDangerComponent {
    void inject(SocialProductDangerActivity activity);
}