package com.geek.kaijo.di.component;

import com.geek.kaijo.di.module.BuilderSiteModule;
import com.geek.kaijo.mvp.contract.BuilderSiteContract;
import com.geek.kaijo.mvp.ui.activity.BuilderSiteActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.BindsInstance;
import dagger.Component;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/12/2019 17:03
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = BuilderSiteModule.class, dependencies = AppComponent.class)
public interface BuilderSiteComponent {
    void inject(BuilderSiteActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        BuilderSiteComponent.Builder view(BuilderSiteContract.View view);

        BuilderSiteComponent.Builder appComponent(AppComponent appComponent);

        BuilderSiteComponent build();
    }
}