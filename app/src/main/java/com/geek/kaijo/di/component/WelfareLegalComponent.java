package com.geek.kaijo.di.component;

import com.geek.kaijo.di.module.WelfareLegalModule;
import com.geek.kaijo.mvp.contract.WelfareLegalContract;
import com.geek.kaijo.mvp.ui.activity.WelfareLegalActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.BindsInstance;
import dagger.Component;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/01/2019 23:04
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = WelfareLegalModule.class, dependencies = AppComponent.class)
public interface WelfareLegalComponent {
    void inject(WelfareLegalActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        WelfareLegalComponent.Builder view(WelfareLegalContract.View view);

        WelfareLegalComponent.Builder appComponent(AppComponent appComponent);

        WelfareLegalComponent build();
    }
}