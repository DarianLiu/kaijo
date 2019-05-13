package dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.geek.kaijo.mvp.model.entity.CaseInfo;
import com.geek.kaijo.mvp.model.entity.IPRegisterBean;
import com.geek.kaijo.mvp.model.entity.UploadCaseFile;

import dao.CaseInfoDao;
import dao.IPRegisterBeanDao;
import dao.UploadCaseFileDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig caseInfoDaoConfig;
    private final DaoConfig iPRegisterBeanDaoConfig;
    private final DaoConfig uploadCaseFileDaoConfig;

    private final CaseInfoDao caseInfoDao;
    private final IPRegisterBeanDao iPRegisterBeanDao;
    private final UploadCaseFileDao uploadCaseFileDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        caseInfoDaoConfig = daoConfigMap.get(CaseInfoDao.class).clone();
        caseInfoDaoConfig.initIdentityScope(type);

        iPRegisterBeanDaoConfig = daoConfigMap.get(IPRegisterBeanDao.class).clone();
        iPRegisterBeanDaoConfig.initIdentityScope(type);

        uploadCaseFileDaoConfig = daoConfigMap.get(UploadCaseFileDao.class).clone();
        uploadCaseFileDaoConfig.initIdentityScope(type);

        caseInfoDao = new CaseInfoDao(caseInfoDaoConfig, this);
        iPRegisterBeanDao = new IPRegisterBeanDao(iPRegisterBeanDaoConfig, this);
        uploadCaseFileDao = new UploadCaseFileDao(uploadCaseFileDaoConfig, this);

        registerDao(CaseInfo.class, caseInfoDao);
        registerDao(IPRegisterBean.class, iPRegisterBeanDao);
        registerDao(UploadCaseFile.class, uploadCaseFileDao);
    }
    
    public void clear() {
        caseInfoDaoConfig.clearIdentityScope();
        iPRegisterBeanDaoConfig.clearIdentityScope();
        uploadCaseFileDaoConfig.clearIdentityScope();
    }

    public CaseInfoDao getCaseInfoDao() {
        return caseInfoDao;
    }

    public IPRegisterBeanDao getIPRegisterBeanDao() {
        return iPRegisterBeanDao;
    }

    public UploadCaseFileDao getUploadCaseFileDao() {
        return uploadCaseFileDao;
    }

}
