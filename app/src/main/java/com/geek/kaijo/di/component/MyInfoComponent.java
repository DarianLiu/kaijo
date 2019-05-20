package com.geek.kaijo.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.geek.kaijo.di.module.MyInfoModule;

import com.jess.arms.di.scope.ActivityScope;
import com.geek.kaijo.mvp.ui.activity.MyInfoActivity;

@ActivityScope
@Component(modules = MyInfoModule.class, dependencies = AppComponent.class)
public interface MyInfoComponent {
    void inject(MyInfoActivity activity);
}