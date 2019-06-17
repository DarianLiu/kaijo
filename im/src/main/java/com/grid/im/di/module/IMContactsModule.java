package com.grid.im.di.module;

import com.grid.im.mvp.contract.IMContactsContract;
import com.grid.im.mvp.model.IMContactsModel;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;


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
@Module
public abstract class IMContactsModule {

    @Binds
    abstract IMContactsContract.Model bindIMMainModel(IMContactsModel model);
}

//@Module
//public class IMContactsModule {
//    private IMContactsContract.View view;
//
//    /**
//     * 构建MyMessageModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
//     *
//     * @param view
//     */
//    public IMContactsModule(IMContactsContract.View view) {
//        this.view = view;
//    }
//
//    @ActivityScope
//    @Provides
//    IMContactsContract.View provideMyMessageView() {
//        return this.view;
//    }
//
//    @ActivityScope
//    @Provides
//    IMContactsContract.Model provideMyMessageModel(IMContactsModel model) {
//        return model;
//    }
//}