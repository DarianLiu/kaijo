package com.geek.kaijo.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.geek.kaijo.di.module.ForestFireModule;
import com.geek.kaijo.mvp.contract.ForestFireContract;

import com.jess.arms.di.scope.ActivityScope;
import com.geek.kaijo.mvp.ui.activity.society.emergency.ForestFireActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/12/2019 21:56
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = ForestFireModule.class, dependencies = AppComponent.class)
public interface ForestFireComponent {
    void inject(ForestFireActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        ForestFireComponent.Builder view(ForestFireContract.View view);

        ForestFireComponent.Builder appComponent(AppComponent appComponent);

        ForestFireComponent build();
    }
}