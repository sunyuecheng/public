package com.sct.mailsecurityscanserver.dao.impl;

import com.sct.mailsecurityscanserver.dao.IMailInfoDao;
import com.sct.mailsecurityscanserver.entity.MailInfo;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("MailInfoDao")
public class MailInfoDaoImpl implements IMailInfoDao {
    private static final Logger logger = Logger.getLogger(MailInfoDaoImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<MailInfo> getMailInfoListByType(Integer type, Integer pageIndex, Integer pageSize) {
        Session session = sessionFactory.getCurrentSession();
        try {
            Query query = session.createQuery("from MailInfo t where t.type = :type and t.workStatus = :workStatus");
            query.setParameter("type", type);
            query.setParameter("workStatus", 1);
            query.setFirstResult(pageIndex*pageSize);
            query.setMaxResults(pageSize);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Get mail info list error,error info(" + e.getMessage() + ")");
            return null;
        }
    }

    public boolean insertMailInfo(MailInfo mailInfo) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.save(mailInfo);
        } catch (Exception e) {
            logger.error("Insert mail info error,error info(" + e.getMessage() + ")");
            return false;
        }
        return true;
    }

    public boolean setMailStatus(String id, Integer workStatus) {
        Session session = sessionFactory.getCurrentSession();
        try {
            Query query = session.createQuery("update MailInfo t set t.work_status = :workStatus where t.id=:id");
            query.setParameter("id", id);
            query.setParameter("workStatus", workStatus);
            return query.executeUpdate() > 0;
        } catch (Exception e) {
            logger.error("Update mail status error,error info(" + e.getMessage() + ")");
            return false;
        }
    }
}
