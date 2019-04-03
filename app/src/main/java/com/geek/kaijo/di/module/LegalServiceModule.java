package com.geek.kaijo.di.module;

import com.geek.kaijo.mvp.contract.LegalServiceContract;
import com.geek.kaijo.mvp.model.LegalServiceModel;

import dagger.Binds;
import dagger.Module;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/01/2019 22:24
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
public abstract class LegalServiceModule {

    @Binds
    abstract LegalServiceContract.Model bindLegalServiceModel(LegalServiceModel model);
}