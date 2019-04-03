package com.geek.kaijo.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.geek.kaijo.di.module.CaseSearchModule;

import com.geek.kaijo.mvp.ui.activity.CaseSearchActivity;

@ActivityScope
@Component(modules = CaseSearchModule.class, dependencies = AppComponent.class)
public interface CaseSearchComponent {
    void inject(CaseSearchActivity activity);
}