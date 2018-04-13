package com.sct.mailsecurityscanserver.dao.impl;

import com.sct.mailsecurityscanserver.dao.IMailDataDownloadRecordInfoDao;
import com.sct.mailsecurityscanserver.entity.MailDataDownloadRecordInfo;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("MailDataDownloadRecordInfoDao")
public class MailDataDownloadRecordInfoDaoImpl implements IMailDataDownloadRecordInfoDao {
    private static final Logger logger = Logger.getLogger(MailDataDownloadRecordInfoDaoImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public boolean insertMailDataDownloadRecordInfo(MailDataDownloadRecordInfo mailDataDownloadRecordInfo) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.save(mailDataDownloadRecordInfo);
        } catch (Exception e) {
            logger.error("Insert mail data download record info error,error info(" + e.getMessage() + ")");
            return false;
        }
        return true;
    }
}
