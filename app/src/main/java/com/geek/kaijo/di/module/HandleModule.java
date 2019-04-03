package com.geek.kaijo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.geek.kaijo.mvp.contract.HandleContract;
import com.geek.kaijo.mvp.model.HandleModel;


@Module
public class HandleModule {
    private HandleContract.View view;

    /**
     * 构建HandleModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public HandleModule(HandleContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    HandleContract.View provideHandleView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    HandleContract.Model provideHandleModel(HandleModel model) {
        return model;
    }
}