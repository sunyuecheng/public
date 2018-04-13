package com.sct.mailsecurityscanserver.dao.impl;

import com.sct.mailsecurityscanserver.dao.IManagerInfoDao;
import com.sct.mailsecurityscanserver.entity.ManagerInfo;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("ManagerInfoDao")
public class ManagerInfoDaoImpl implements IManagerInfoDao {
    private static final Logger logger = Logger.getLogger(ManagerInfoDaoImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public ManagerInfo getManagerInfo(String account) {
        Session session = sessionFactory.getCurrentSession();
        try {
            Query query = session.createQuery("from ManagerInfo as t where t.account=:account ");
            query.setParameter("account", account);

            ManagerInfo managerInfo = (ManagerInfo) query.getSingleResult();
            return managerInfo;
        } catch (Exception e) {
            logger.error("Get manager info error,account(" + account + "),error info(" + e.getMessage() + ")");
        }

        return null;
    }
}
