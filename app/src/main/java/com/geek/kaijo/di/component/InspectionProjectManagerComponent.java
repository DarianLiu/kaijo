package com.geek.kaijo.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.geek.kaijo.di.module.InspectionProjectManagerModule;

import com.geek.kaijo.mvp.ui.activity.InspectionProjectManagerActivity;

@ActivityScope
@Component(modules = InspectionProjectManagerModule.class, dependencies = AppComponent.class)
public interface InspectionProjectManagerComponent {
    void inject(InspectionProjectManagerActivity activity);
}