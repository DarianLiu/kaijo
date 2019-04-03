package com.geek.kaijo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.geek.kaijo.mvp.contract.PhotoActivityContract;
import com.geek.kaijo.mvp.model.PhotoActivityModel;


@Module
public class PhotoActivityModule {
    private PhotoActivityContract.View view;

    /**
     * 构建PhotoActivityModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public PhotoActivityModule(PhotoActivityContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    PhotoActivityContract.View providePhotoActivityView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    PhotoActivityContract.Model providePhotoActivityModel(PhotoActivityModel model) {
        return model;
    }
}