package com.geek.kaijo.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.geek.kaijo.di.module.CaseManagerModule;

import com.geek.kaijo.mvp.ui.fragment.CaseManagerFragment;

@ActivityScope
@Component(modules = CaseManagerModule.class, dependencies = AppComponent.class)
public interface CaseManagerComponent {
    void inject(CaseManagerFragment fragment);
}