package com.geek.kaijo.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.geek.kaijo.di.module.MyMessageModule;

import com.geek.kaijo.mvp.ui.fragment.MyMessageFragment;

@ActivityScope
@Component(modules = MyMessageModule.class, dependencies = AppComponent.class)
public interface MyMessageComponent {
    void inject(MyMessageFragment fragment);
}