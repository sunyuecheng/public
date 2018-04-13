package com.sct.mailsecurityscanserver.dao.impl;

import com.sct.mailsecurityscanserver.dao.IMailServerBugInfoDao;
import com.sct.mailsecurityscanserver.entity.MailBugInfo;
import com.sct.mailsecurityscanserver.entity.MailServerBugInfo;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("MailServerBugInfoDao")
public class MailServerBugInfoDaoImpl implements IMailServerBugInfoDao {
    private static final Logger logger = Logger.getLogger(MailServerBugInfoDaoImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public MailServerBugInfo getMailServerBugInfo(String mailServerName, String mailServerVersion) {
        Session session = sessionFactory.getCurrentSession();
        try {
            Query query = session.createQuery ("from MailServerBugInfo as t where t.scanMailServerName=:mailServerName and t.scanMailServerVersion = :mailServerVersion");
            query.setParameter("mailServerName",mailServerName);
            query.setParameter("mailServerVersion",mailServerVersion);

            MailServerBugInfo mailServerBugInfo = (MailServerBugInfo)query.getSingleResult();
            return mailServerBugInfo;
        } catch (Exception e) {
            logger.error("Get mail server bug info error,mail server name("+ mailServerName +"),mail server version("+ mailServerVersion +"),error info("+ e.getMessage() +")");
        }

        return null;
    }
}
