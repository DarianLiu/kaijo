package com.geek.kaijo.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.geek.kaijo.di.module.WinterSnowModule;
import com.geek.kaijo.mvp.contract.WinterSnowContract;

import com.jess.arms.di.scope.ActivityScope;
import com.geek.kaijo.mvp.ui.activity.society.emergency.WinterSnowActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/12/2019 22:10
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = WinterSnowModule.class, dependencies = AppComponent.class)
public interface WinterSnowComponent {
    void inject(WinterSnowActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        WinterSnowComponent.Builder view(WinterSnowContract.View view);

        WinterSnowComponent.Builder appComponent(AppComponent appComponent);

        WinterSnowComponent build();
    }
}