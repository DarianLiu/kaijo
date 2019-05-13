package com.geek.kaijo.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.geek.kaijo.di.module.ComponentDetailModule;

import com.jess.arms.di.scope.ActivityScope;
import com.geek.kaijo.mvp.ui.activity.ComponentDetailActivity;

@ActivityScope
@Component(modules = ComponentDetailModule.class, dependencies = AppComponent.class)
public interface ComponentDetailComponent {
    void inject(ComponentDetailActivity activity);
}