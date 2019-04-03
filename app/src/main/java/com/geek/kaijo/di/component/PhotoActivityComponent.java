package com.geek.kaijo.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.geek.kaijo.di.module.PhotoActivityModule;

import com.jess.arms.di.scope.ActivityScope;
import com.geek.kaijo.mvp.ui.activity.PhotoActivityActivity;

@ActivityScope
@Component(modules = PhotoActivityModule.class, dependencies = AppComponent.class)
public interface PhotoActivityComponent {
    void inject(PhotoActivityActivity activity);
}