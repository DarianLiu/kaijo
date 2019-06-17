package com.grid.im.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.grid.im.di.module.ChatModule;
import com.grid.im.mvp.contract.ChatContract;

import com.jess.arms.di.scope.ActivityScope;
import com.grid.im.mvp.ui.activity.ChatActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/10/2019 09:45
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = ChatModule.class, dependencies = AppComponent.class)
public interface ChatComponent {
    void inject(ChatActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        ChatComponent.Builder view(ChatContract.View view);

        ChatComponent.Builder appComponent(AppComponent appComponent);

        ChatComponent build();
    }
}