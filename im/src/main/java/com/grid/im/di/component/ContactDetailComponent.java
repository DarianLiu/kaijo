package com.grid.im.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.grid.im.di.module.ContactDetailModule;
import com.grid.im.mvp.contract.ContactDetailContract;

import com.jess.arms.di.scope.ActivityScope;
import com.grid.im.mvp.ui.activity.ContactDetailActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/10/2019 09:50
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = ContactDetailModule.class, dependencies = AppComponent.class)
public interface ContactDetailComponent {
    void inject(ContactDetailActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        ContactDetailComponent.Builder view(ContactDetailContract.View view);

        ContactDetailComponent.Builder appComponent(AppComponent appComponent);

        ContactDetailComponent build();
    }
}