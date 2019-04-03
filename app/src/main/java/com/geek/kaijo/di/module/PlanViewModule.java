package com.geek.kaijo.di.module;

import com.geek.kaijo.mvp.contract.PlanViewContract;
import com.geek.kaijo.mvp.model.PlanViewModel;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;


@Module
public class PlanViewModule {
    private PlanViewContract.View view;

    /**
     * 构建PlanViewModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public PlanViewModule(PlanViewContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    PlanViewContract.View providePlanViewView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    PlanViewContract.Model providePlanViewModel(PlanViewModel model) {
        return model;
    }
}