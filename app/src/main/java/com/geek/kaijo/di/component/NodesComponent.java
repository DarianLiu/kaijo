package com.geek.kaijo.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.geek.kaijo.di.module.NodesModule;
import com.geek.kaijo.mvp.contract.NodesContract;

import com.jess.arms.di.scope.ActivityScope;
import com.geek.kaijo.mvp.ui.activity.NodesActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/13/2019 15:57
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = NodesModule.class, dependencies = AppComponent.class)
public interface NodesComponent {
    void inject(NodesActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        NodesComponent.Builder view(NodesContract.View view);

        NodesComponent.Builder appComponent(AppComponent appComponent);

        NodesComponent build();
    }
}