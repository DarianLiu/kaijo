package com.geek.kaijo.app;

import android.app.ActivityManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.geek.kaijo.BuildConfig;
import com.geek.kaijo.R;
import com.geek.kaijo.app.api.Api;
import com.jess.arms.base.BaseApplication;
import com.jess.arms.utils.ArmsUtils;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;

import org.greenrobot.greendao.query.QueryBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import butterknife.ButterKnife;
import dao.DaoMaster;
import dao.DaoSession;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import timber.log.Timber;


/**
 * Application
 * Created by LiuLi on 2018/8/23.
 */

public class MyApplication extends BaseApplication {

    private static MyApplication instance;


    private DaoSession daoSession;

    public static MyApplication get() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        fix();
    }

    /**
     * 解决oppoR9 TimeoutExceptions
     */
    public void fix() {
        try {
            Class clazz = Class.forName("java.lang.Daemons$FinalizerWatchdogDaemon");

            Method method = clazz.getSuperclass().getDeclaredMethod("stop");
            method.setAccessible(true);

            Field field = clazz.getDeclaredField("INSTANCE");
            field.setAccessible(true);

            method.invoke(field.get(null));

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.LOG_DEBUG) {//Timber初始化
            //Timber 是一个日志框架容器,外部使用统一的Api,内部可以动态的切换成任何日志框架(打印策略)进行日志打印
            //并且支持添加多个日志框架(打印策略),做到外部调用一次 Api,内部却可以做到同时使用多个策略
            //比如添加三个策略,一个打印日志,一个将日志保存本地,一个将日志上传服务器
            Timber.plant(new Timber.DebugTree());
            // 如果你想将框架切换为 Logger 来打印日志,请使用下面的代码,如想切换为其他日志框架请根据下面的方式扩展
//                    Logger.addLogAdapter(new AndroidLogAdapter());
//                    Timber.plant(new Timber.DebugTree() {
//                        @Override
//                        protected void log(int priority, String tag, String message, Throwable t) {
//                            Logger.log(priority, tag, message, t);
//                        }
//                    });
            ButterKnife.setDebug(true);
        }
        //leakCanary内存泄露检查
//        ArmsUtils.obtainAppComponentFromContext(application).extras().put(RefWatcher.class.getName(), BuildConfig.USE_CANARY ? LeakCanary.install(application) : RefWatcher.DISABLED);
        //扩展 AppManager 的远程遥控功能
        ArmsUtils.obtainAppComponentFromContext(this).appManager().setHandleListener((appManager, message) -> {
            switch (message.what) {
                //case 0:
                //do something ...
                //   break;
            }
        });
        //Usage:
        //Message msg = new Message();
        //msg.what = 0;
        //AppManager.post(msg); like EventBus

        RetrofitUrlManager.getInstance().putDomain(Api.USER_DOMAIN_NAME, Api.URL_USER);
        RetrofitUrlManager.getInstance().putDomain(Api.FILE_UPLOAD_DOMAIN_NAME, Api.URL_FILE_UPLOAD);
        instance = this;
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, Constant.DB_NAME, null);
        SQLiteDatabase database = devOpenHelper.getWritableDatabase();
        database.enableWriteAheadLogging();//这将允许来自多个线程的事务
//        database.setLocale(Locale.CHINA);
        DaoMaster daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
    }


    private void initDatabase() {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, Constant.DB_NAME, null);
        SQLiteDatabase database = devOpenHelper.getWritableDatabase();
        database.enableWriteAheadLogging();//这将允许来自多个线程的事务
//        database.setLocale(Locale.CHINA);
        DaoMaster daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();

        QueryBuilder.LOG_SQL = true;//是否开启数据库日志；正式版false；
        QueryBuilder.LOG_VALUES = true;
    }

    private String getProcessName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = null;
        if (am != null) {
            runningApps = am.getRunningAppProcesses();
        }
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo proInfo : runningApps) {
            if (proInfo.pid == android.os.Process.myPid()) {
                if (proInfo.processName != null) {
                    return proInfo.processName;
                }
            }
        }
        return null;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
