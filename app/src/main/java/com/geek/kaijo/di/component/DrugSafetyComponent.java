package com.geek.kaijo.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.geek.kaijo.di.module.DrugSafetyModule;
import com.geek.kaijo.mvp.contract.DrugSafetyContract;

import com.jess.arms.di.scope.ActivityScope;
import com.geek.kaijo.mvp.ui.activity.society.foot.DrugSafetyActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/12/2019 17:33
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = DrugSafetyModule.class, dependencies = AppComponent.class)
public interface DrugSafetyComponent {
    void inject(DrugSafetyActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        DrugSafetyComponent.Builder view(DrugSafetyContract.View view);

        DrugSafetyComponent.Builder appComponent(AppComponent appComponent);

        DrugSafetyComponent build();
    }
}