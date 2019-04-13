package com.geek.kaijo.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.geek.kaijo.di.module.InternetBarModule;
import com.geek.kaijo.mvp.contract.InternetBarContract;

import com.jess.arms.di.scope.ActivityScope;
import com.geek.kaijo.mvp.ui.activity.society.culture.InternetBarActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/12/2019 22:27
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = InternetBarModule.class, dependencies = AppComponent.class)
public interface InternetBarComponent {
    void inject(InternetBarActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        InternetBarComponent.Builder view(InternetBarContract.View view);

        InternetBarComponent.Builder appComponent(AppComponent appComponent);

        InternetBarComponent build();
    }
}