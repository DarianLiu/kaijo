package com.grid.im.di.component;

import com.grid.im.di.module.ConnectModule;
import com.grid.im.di.module.GroupModule;
import com.grid.im.mvp.contract.ConnectContract;
import com.grid.im.mvp.contract.GroupContract;
import com.grid.im.mvp.ui.fragment.ConnectFragment;
import com.grid.im.mvp.ui.fragment.GroupFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.BindsInstance;
import dagger.Component;


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
@Component(modules = GroupModule.class, dependencies = AppComponent.class)
public interface GroupComponent {
    void inject(GroupFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        GroupComponent.Builder view(GroupContract.View view);

        GroupComponent.Builder appComponent(AppComponent appComponent);

        GroupComponent build();
    }
}