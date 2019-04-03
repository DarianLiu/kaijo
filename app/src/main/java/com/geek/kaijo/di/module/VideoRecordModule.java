package com.geek.kaijo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.geek.kaijo.mvp.contract.VideoRecordContract;
import com.geek.kaijo.mvp.model.VideoRecordModel;


@Module
public class VideoRecordModule {
    private VideoRecordContract.View view;

    /**
     * 构建VideoRecordModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public VideoRecordModule(VideoRecordContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    VideoRecordContract.View provideVideoRecordView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    VideoRecordContract.Model provideVideoRecordModel(VideoRecordModel model) {
        return model;
    }
}