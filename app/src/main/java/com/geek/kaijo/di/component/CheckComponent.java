package com.geek.kaijo.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.geek.kaijo.di.module.CheckModule;

import com.geek.kaijo.mvp.ui.activity.CheckActivity;

@ActivityScope
@Component(modules = CheckModule.class, dependencies = AppComponent.class)
public interface CheckComponent {
    void inject(CheckActivity activity);
}