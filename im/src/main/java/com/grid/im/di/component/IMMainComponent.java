package com.grid.im.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.grid.im.di.module.IMMainModule;
import com.grid.im.mvp.contract.IMMainContract;

import com.jess.arms.di.scope.ActivityScope;
import com.grid.im.mvp.ui.activity.IMMainActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/04/2019 18:26
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = IMMainModule.class, dependencies = AppComponent.class)
public interface IMMainComponent {
    void inject(IMMainActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        IMMainComponent.Builder view(IMMainContract.View view);

        IMMainComponent.Builder appComponent(AppComponent appComponent);

        IMMainComponent build();
    }
}