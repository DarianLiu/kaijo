package com.geek.kaijo.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.geek.kaijo.di.module.InfoEditModule;

import com.jess.arms.di.scope.ActivityScope;
import com.geek.kaijo.mvp.ui.activity.InfoEditActivity;

@ActivityScope
@Component(modules = InfoEditModule.class, dependencies = AppComponent.class)
public interface InfoEditComponent {
    void inject(InfoEditActivity activity);
}