package com.geek.kaijo.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.geek.kaijo.di.module.NodesAddModule;
import com.geek.kaijo.mvp.contract.NodesAddContract;

import com.jess.arms.di.scope.ActivityScope;
import com.geek.kaijo.mvp.ui.activity.NodesAddActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/14/2019 14:14
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = NodesAddModule.class, dependencies = AppComponent.class)
public interface NodesAddComponent {
    void inject(NodesAddActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        NodesAddComponent.Builder view(NodesAddContract.View view);

        NodesAddComponent.Builder appComponent(AppComponent appComponent);

        NodesAddComponent build();
    }
}