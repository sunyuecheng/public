package com.sct.mailsecurityscanserver.dao.impl;

import com.sct.mailsecurityscanserver.dao.IOperatorInfoDao;
import com.sct.mailsecurityscanserver.entity.OperatorInfo;
import com.sct.mailsecurityscanserver.entity.TerminalInfo;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("OperatorInfoDao")
public class OperatorInfoDaoImpl implements IOperatorInfoDao {
    private static final Logger logger = Logger.getLogger(OperatorInfoDaoImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public OperatorInfo getOperatorInfo(String account) {
        Session session = sessionFactory.getCurrentSession();
        try {
            Query query = session.createQuery ("from OperatorInfo as t where t.account=:account ");
            query.setParameter("account",account);

            OperatorInfo operatorInfo = (OperatorInfo)query.getSingleResult();
            return operatorInfo;
        } catch (Exception e) {
            logger.error("Get operator info error,account("+ account +"),error info("+ e.getMessage() +")");
        }

        return null;
    }

}
