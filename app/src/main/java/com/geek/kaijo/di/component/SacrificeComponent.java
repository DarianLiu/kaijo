package com.geek.kaijo.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.geek.kaijo.di.module.SacrificeModule;
import com.geek.kaijo.mvp.contract.SacrificeContract;

import com.jess.arms.di.scope.ActivityScope;
import com.geek.kaijo.mvp.ui.activity.society.emergency.SacrificeActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/12/2019 22:18
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = SacrificeModule.class, dependencies = AppComponent.class)
public interface SacrificeComponent {
    void inject(SacrificeActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        SacrificeComponent.Builder view(SacrificeContract.View view);

        SacrificeComponent.Builder appComponent(AppComponent appComponent);

        SacrificeComponent build();
    }
}