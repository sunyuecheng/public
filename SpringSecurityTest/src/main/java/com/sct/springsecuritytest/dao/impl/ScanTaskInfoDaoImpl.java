package com.sct.mailsecurityscanserver.dao.impl;

import com.sct.mailsecurityscanserver.dao.IScanTaskInfoDao;
import com.sct.mailsecurityscanserver.entity.ScanResultInfo;
import com.sct.mailsecurityscanserver.entity.ScanTaskInfo;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ScanTaskInfoDao")
public class ScanTaskInfoDaoImpl implements IScanTaskInfoDao {
    private static final Logger logger = Logger.getLogger(ScanTaskInfoDaoImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public ScanTaskInfo getScanTaskInfoById(String taskId) {
        Session session = sessionFactory.getCurrentSession();
        try {
            ScanTaskInfo scanTaskInfo = session.get(ScanTaskInfo.class, taskId);
            return scanTaskInfo;
        } catch (Exception e) {
            logger.error("Get scan task info error,task id("+ taskId +"),error info("+ e.getMessage() +")");
        }
        return null;
    }

    public boolean insertScanTaskInfo(ScanTaskInfo scanTaskInfo, List<ScanResultInfo> scanResultInfoList) {
        Session session = sessionFactory.getCurrentSession();
        try {
            Transaction tx = session.beginTransaction();
            session.save(scanTaskInfo);

            for ( int i=0; i<scanResultInfoList.size(); i++ ) {
                session.save(scanResultInfoList.get(i));
            }
            tx.commit();
        } catch (Exception e) {
            logger.error("Insert scan task info error,error info("+ e.getMessage() +")");
            return false;
        }
        return true;
    }

}
