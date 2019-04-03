package com.geek.kaijo.di.component;

import com.geek.kaijo.di.module.InspectionProjectRegisterModule;
import com.geek.kaijo.mvp.ui.activity.InspectionProjectRegisterActivity;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

@ActivityScope
@Component(modules = InspectionProjectRegisterModule.class, dependencies = AppComponent.class)
public interface InspectionProjectRegisterComponent {
    void inject(InspectionProjectRegisterActivity activity);
}