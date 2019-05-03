package com.geek.kaijo.di.module;

import com.geek.kaijo.mvp.contract.ComponentContract;
import com.geek.kaijo.mvp.contract.MyMessageContract;
import com.geek.kaijo.mvp.model.ConponentModel;
import com.geek.kaijo.mvp.model.MyMessageModel;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;


@Module
public class ConponentModule {
    private ComponentContract.View view;

    /**
     * 构建MyMessageModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ConponentModule(ComponentContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ComponentContract.View provideMyMessageView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ComponentContract.Model provideMyMessageModel(ConponentModel model) {
        return model;
    }
}