package com.geek.kaijo.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.geek.kaijo.di.module.TemporaryModule;
import com.geek.kaijo.mvp.contract.TemporaryContract;

import com.jess.arms.di.scope.ActivityScope;
import com.geek.kaijo.mvp.ui.activity.TemporaryActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/13/2019 23:38
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = TemporaryModule.class, dependencies = AppComponent.class)
public interface TemporaryComponent {
    void inject(TemporaryActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        TemporaryComponent.Builder view(TemporaryContract.View view);

        TemporaryComponent.Builder appComponent(AppComponent appComponent);

        TemporaryComponent build();
    }
}