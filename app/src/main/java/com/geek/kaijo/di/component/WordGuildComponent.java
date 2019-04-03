package com.geek.kaijo.di.component;

import com.geek.kaijo.di.module.WordGuildModule;
import com.geek.kaijo.mvp.ui.activity.WordGuildActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

@ActivityScope
@Component(modules = WordGuildModule.class, dependencies = AppComponent.class)
public interface WordGuildComponent {
    void inject(WordGuildActivity activity);
}