package com.geek.kaijo.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.geek.kaijo.di.module.PlanViewModule;

import com.geek.kaijo.mvp.ui.activity.PlanViewActivity;

@ActivityScope
@Component(modules = PlanViewModule.class, dependencies = AppComponent.class)
public interface PlanViewComponent {
    void inject(PlanViewActivity activity);
}