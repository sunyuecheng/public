package com.sct.mailsecurityscanserver.dao.impl;

import com.sct.mailsecurityscanserver.dao.IMailBugInfoDao;
import com.sct.mailsecurityscanserver.entity.MailBugInfo;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("MailBugInfoDao")
public class MailBugInfoDaoImpl implements IMailBugInfoDao {
    private static final Logger logger = Logger.getLogger(MailBugInfoDaoImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<MailBugInfo> getMailBugInfoList(Integer pageIndex, Integer pageSize) {
        Session session = sessionFactory.getCurrentSession();
        try {
            Query query = session.createQuery("from MailBugInfo t ");
            query.setFirstResult(pageIndex*pageSize);
            query.setMaxResults(pageSize);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Get mail bug info list error,error info(" + e.getMessage() + ")");
            return null;
        }
    }

    public MailBugInfo getMailBugInfoByMailId(String mailId) {
        Session session = sessionFactory.getCurrentSession();
        try {
            Query query = session.createQuery ("from MailBugInfo as t where t.mailId=:mailId ");
            query.setParameter("mailId",mailId);

            MailBugInfo mailBugInfo = (MailBugInfo)query.getSingleResult();
            return mailBugInfo;
        } catch (Exception e) {
            logger.error("Get mail bug info error,mail id("+ mailId +"),error info("+ e.getMessage() +")");
        }

        return null;
    }

    public boolean insertMailBugInfo(MailBugInfo mailBugInfo) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.save(mailBugInfo);
        } catch (Exception e) {
            logger.error("Insert mail bug info error,error info(" + e.getMessage() + ")");
            return false;
        }
        return true;
    }
}
