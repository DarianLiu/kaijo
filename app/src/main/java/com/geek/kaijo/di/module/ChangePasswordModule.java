package com.geek.kaijo.di.module;

import com.geek.kaijo.mvp.contract.ChangePasswordContract;
import com.geek.kaijo.mvp.model.ChangePasswordModel;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;


@Module
public class ChangePasswordModule {
    private ChangePasswordContract.View view;

    /**
     * 构建ChangePasswordModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ChangePasswordModule(ChangePasswordContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ChangePasswordContract.View provideChangePasswordView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ChangePasswordContract.Model provideChangePasswordModel(ChangePasswordModel model) {
        return model;
    }
}