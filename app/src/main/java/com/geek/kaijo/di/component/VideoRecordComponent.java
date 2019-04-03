package com.geek.kaijo.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.geek.kaijo.di.module.VideoRecordModule;

import com.jess.arms.di.scope.ActivityScope;
import com.geek.kaijo.mvp.ui.activity.VideoRecordActivity;

@ActivityScope
@Component(modules = VideoRecordModule.class, dependencies = AppComponent.class)
public interface VideoRecordComponent {
    void inject(VideoRecordActivity activity);
}