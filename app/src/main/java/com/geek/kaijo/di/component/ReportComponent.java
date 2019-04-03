package com.geek.kaijo.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.geek.kaijo.di.module.ReportModule;

import com.geek.kaijo.mvp.ui.activity.ReportActivity;

@ActivityScope
@Component(modules = ReportModule.class, dependencies = AppComponent.class)
public interface ReportComponent {
    void inject(ReportActivity activity);
}