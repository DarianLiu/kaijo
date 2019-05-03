package com.geek.kaijo.di.component;

import com.geek.kaijo.di.module.ConponentModule;
import com.geek.kaijo.di.module.MyMessageModule;
import com.geek.kaijo.mvp.ui.fragment.ComponentFragment;
import com.geek.kaijo.mvp.ui.fragment.MyMessageFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

@ActivityScope
@Component(modules = ConponentModule.class, dependencies = AppComponent.class)
public interface ComponentComponent {
    void inject(ComponentFragment fragment);
}