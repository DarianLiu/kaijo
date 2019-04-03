package com.geek.kaijo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.geek.kaijo.mvp.contract.RecoderPlayerContract;
import com.geek.kaijo.mvp.model.RecoderPlayerModel;


@Module
public class RecoderPlayerModule {
    private RecoderPlayerContract.View view;

    /**
     * 构建RecoderPlayerModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public RecoderPlayerModule(RecoderPlayerContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    RecoderPlayerContract.View provideRecoderPlayerView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    RecoderPlayerContract.Model provideRecoderPlayerModel(RecoderPlayerModel model) {
        return model;
    }
}