package com.geek.kaijo.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.geek.kaijo.di.module.FoodSafteModule;
import com.geek.kaijo.mvp.contract.FoodSafteContract;

import com.jess.arms.di.scope.ActivityScope;
import com.geek.kaijo.mvp.ui.activity.FoodSafteActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/12/2019 17:18
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = FoodSafteModule.class, dependencies = AppComponent.class)
public interface FoodSafteComponent {
    void inject(FoodSafteActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        FoodSafteComponent.Builder view(FoodSafteContract.View view);

        FoodSafteComponent.Builder appComponent(AppComponent appComponent);

        FoodSafteComponent build();
    }
}