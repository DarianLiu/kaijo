package com.geek.kaijo.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.geek.kaijo.di.module.AgainPasswordModule;

import com.jess.arms.di.scope.ActivityScope;
import com.geek.kaijo.mvp.ui.activity.AgainPasswordActivity;

@ActivityScope
@Component(modules = AgainPasswordModule.class, dependencies = AppComponent.class)
public interface AgainPasswordComponent {
    void inject(AgainPasswordActivity activity);
}