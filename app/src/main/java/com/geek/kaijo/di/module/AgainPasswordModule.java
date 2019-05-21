package com.geek.kaijo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.geek.kaijo.mvp.contract.AgainPasswordContract;
import com.geek.kaijo.mvp.model.AgainPasswordModel;


@Module
public class AgainPasswordModule {
    private AgainPasswordContract.View view;

    /**
     * 构建AgainPasswordModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public AgainPasswordModule(AgainPasswordContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    AgainPasswordContract.View provideAgainPasswordView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    AgainPasswordContract.Model provideAgainPasswordModel(AgainPasswordModel model) {
        return model;
    }
}