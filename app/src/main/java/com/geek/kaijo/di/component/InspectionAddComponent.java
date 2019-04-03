package com.geek.kaijo.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.geek.kaijo.di.module.InspectionAddModule;

import com.geek.kaijo.mvp.ui.activity.InspectionAddActivity;

@ActivityScope
@Component(modules = InspectionAddModule.class, dependencies = AppComponent.class)
public interface InspectionAddComponent {
    void inject(InspectionAddActivity activity);
}