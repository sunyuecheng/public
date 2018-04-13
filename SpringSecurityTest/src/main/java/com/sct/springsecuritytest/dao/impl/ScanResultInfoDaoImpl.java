package com.sct.mailsecurityscanserver.dao.impl;

import com.sct.mailsecurityscanserver.dao.IScanResultInfoDao;
import com.sct.mailsecurityscanserver.entity.ScanResultInfo;
import com.sct.mailsecurityscanserver.entity.ScanTaskInfo;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ScanResultInfoDao")
public class ScanResultInfoDaoImpl implements IScanResultInfoDao {
    private static final Logger logger = Logger.getLogger(ScanResultInfoDaoImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<ScanResultInfo> getScanResultInfoListById(String taskId) {
        Session session = sessionFactory.getCurrentSession();
        try {
            Query query = session.createQuery("from ScanResultInfo t where t.taskId = :taskId");
            query.setParameter("taskId", taskId);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Get scan result info list error,error info(" + e.getMessage() + ")");
            return null;
        }
    }

    public boolean insertScanResultInfo(ScanResultInfo scanResultInfo) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.save(scanResultInfo);
        } catch (Exception e) {
            logger.error("Insert scan result info error,error info("+ e.getMessage() +")");
            return false;
        }
        return true;
    }

}
