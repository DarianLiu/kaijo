package com.geek.kaijo.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.geek.kaijo.di.module.UploadModule;

import com.jess.arms.di.scope.ActivityScope;
import com.geek.kaijo.mvp.ui.activity.UploadActivity;

@ActivityScope
@Component(modules = UploadModule.class, dependencies = AppComponent.class)
public interface UploadComponent {
    void inject(UploadActivity activity);
}