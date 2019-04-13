package com.geek.kaijo.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.geek.kaijo.di.module.SpecialCollectionModule;
import com.geek.kaijo.mvp.contract.SpecialCollectionContract;

import com.jess.arms.di.scope.ActivityScope;
import com.geek.kaijo.mvp.ui.activity.society.safety.SpecialCollectionActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/12/2019 16:43
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = SpecialCollectionModule.class, dependencies = AppComponent.class)
public interface SpecialCollectionComponent {
    void inject(SpecialCollectionActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        SpecialCollectionComponent.Builder view(SpecialCollectionContract.View view);

        SpecialCollectionComponent.Builder appComponent(AppComponent appComponent);

        SpecialCollectionComponent build();
    }
}