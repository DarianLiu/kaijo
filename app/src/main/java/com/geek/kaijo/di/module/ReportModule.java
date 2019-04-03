package com.geek.kaijo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.geek.kaijo.mvp.contract.ReportContract;
import com.geek.kaijo.mvp.model.ReportModel;


@Module
public class ReportModule {
    private ReportContract.View view;

    /**
     * 构建ReportModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ReportModule(ReportContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ReportContract.View provideReportView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ReportContract.Model provideReportModel(ReportModel model) {
        return model;
    }
}