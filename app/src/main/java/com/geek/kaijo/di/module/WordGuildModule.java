package com.geek.kaijo.di.module;

import com.geek.kaijo.mvp.contract.WordGuildContract;
import com.geek.kaijo.mvp.model.WordGuildModel;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;


@Module
public class WordGuildModule {
    private WordGuildContract.View view;

    /**
     * 构建WordGuildModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public WordGuildModule(WordGuildContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    WordGuildContract.View provideWordGuildView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    WordGuildContract.Model provideWordGuildModel(WordGuildModel model) {
        return model;
    }
}