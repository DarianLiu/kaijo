package com.grid.im.di.component;

import com.grid.im.di.module.IMContactsModule;
import com.grid.im.mvp.contract.IMContactsContract;
import com.grid.im.mvp.ui.fragment.IMContactsFragment;
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
@Component(modules = IMContactsModule.class, dependencies = AppComponent.class)
public interface IMContactsComponent {
    void inject(IMContactsFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        IMContactsComponent.Builder view(IMContactsContract.View view);

        IMContactsComponent.Builder appComponent(AppComponent appComponent);

        IMContactsComponent build();
    }
}