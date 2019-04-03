package com.geek.kaijo.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.geek.kaijo.di.module.HandleDetailModule;

import com.geek.kaijo.mvp.ui.activity.HandleDetailActivity;

@ActivityScope
@Component(modules = HandleDetailModule.class, dependencies = AppComponent.class)
public interface HandleDetailComponent {
    void inject(HandleDetailActivity activity);
}