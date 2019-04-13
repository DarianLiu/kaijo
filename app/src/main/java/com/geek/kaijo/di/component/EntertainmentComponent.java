package com.geek.kaijo.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.geek.kaijo.di.module.EntertainmentModule;
import com.geek.kaijo.mvp.contract.EntertainmentContract;

import com.jess.arms.di.scope.ActivityScope;
import com.geek.kaijo.mvp.ui.activity.society.culture.EntertainmentActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/12/2019 22:42
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = EntertainmentModule.class, dependencies = AppComponent.class)
public interface EntertainmentComponent {
    void inject(EntertainmentActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        EntertainmentComponent.Builder view(EntertainmentContract.View view);

        EntertainmentComponent.Builder appComponent(AppComponent appComponent);

        EntertainmentComponent build();
    }
}