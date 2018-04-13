package com.sct.mailsecurityscanserver.dao.impl;

import com.sct.mailsecurityscanserver.dao.ITerminalVersionInfoDao;
import com.sct.mailsecurityscanserver.entity.TerminalVersionInfo;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("TerminalVersionInfoDao")
public class TerminalVersionInfoDaoImpl implements ITerminalVersionInfoDao {
    private static final Logger logger = Logger.getLogger(TerminalVersionInfoDaoImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    public Integer checkTerminalVersionInfo(Integer systemType, String systemVersion, String terminalVersion) {
        Session session = sessionFactory.getCurrentSession();
        try {
            Query query = session.createQuery ("from TerminalVersionInfo as t where t.systemType=:systemType " +
                    "and t.systemVersion=:systemVersion and t.terminalVersion=:terminalVersion");
            query.setParameter("systemType",systemType);
            query.setParameter("systemVersion",systemVersion);
            query.setParameter("terminalVersion", terminalVersion);

            TerminalVersionInfo terminalVersionInfo = (TerminalVersionInfo)query.getSingleResult();

            return terminalVersionInfo.getVersionStatus();
        } catch (Exception e) {
            logger.error("Check terminal version info error,system type("+ systemType +"),system version("+ systemVersion +"),terminal version(" + terminalVersion +"),error info("+ e.getMessage() +")");
        }

        return null;
    }

    public TerminalVersionInfo getUploadTerminalVersionInfo(Integer systemType, String systemVersion, String terminalVersion) {
        Session session = sessionFactory.getCurrentSession();
        try {
            Query query = session.createQuery ("from TerminalVersionInfo as t where t.systemType=:systemType " +
                    "and t.systemVersion=:systemVersion and t.terminalVersion=:terminalVersion");
            query.setParameter("systemType",systemType);
            query.setParameter("systemVersion",systemVersion);
            query.setParameter("terminalVersion",terminalVersion);

            TerminalVersionInfo terminalVersionInfo = (TerminalVersionInfo)query.getSingleResult();

            query = session.createQuery ("from TerminalVersionInfo as t where t.systemType=:systemType " +
                    "and t.systemVersion=:systemVersion and t.terminalVersion=:terminalVersion");
            query.setParameter("systemType",terminalVersionInfo.getSystemType());
            query.setParameter("systemVersion",terminalVersionInfo.getSystemVersion());
            query.setParameter("terminalVersion",terminalVersionInfo.getUpdateVersion());

            TerminalVersionInfo retTerminalVersionInfo = (TerminalVersionInfo)query.getSingleResult();

            return retTerminalVersionInfo;
        } catch (Exception e) {
            logger.error("Get upload terminal version info error,system type("+ systemType +"),system version("+ systemVersion +"),terminal version(" + terminalVersion +"),error info("+ e.getMessage() +")");
        }

        return null;
    }
}
