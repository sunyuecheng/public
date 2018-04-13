package com.sct.mailsecurityscanserver.dao.impl;

import com.sct.mailsecurityscanserver.dao.ITerminalInfoDao;
import com.sct.mailsecurityscanserver.entity.TerminalInfo;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("TerminalInfoDao")
public class TerminalInfoDaoImpl implements ITerminalInfoDao {
    private static final Logger logger = Logger.getLogger(TerminalInfoDaoImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Integer checkTerminalInfo(String terminalId, String cpuSerialNum, String hardDiskSerialNum) {
        Session session = sessionFactory.getCurrentSession();
        try {
            Query query = session.createQuery ("select count(*) from TerminalInfo as t where t.id=:terminalId " +
                    "and t.cpuSerialNum=:cpuSerialNum and t.hardDiskSerialNum=:hardDiskSerialNum");
            query.setParameter("terminalId",terminalId);
            query.setParameter("cpuSerialNum",cpuSerialNum);
            query.setParameter("hardDiskSerialNum", hardDiskSerialNum);

            Integer num = (Integer)query.uniqueResult();

            return num;
        } catch (Exception e) {
            logger.error("Check terminal info error,terminal id("+ terminalId +"),cpu serial num("+ cpuSerialNum +")," +
                    "hard disk serial num(" + hardDiskSerialNum +"),error info("+ e.getMessage() +")");
        }

        return -1;
    }

    public Integer checkTerminalStatus(String terminalId) {
        Session session = sessionFactory.getCurrentSession();
        try {
            Query query = session.createQuery ("select count(*) from TerminalInfo as t where t.id=:terminalId ");
            query.setParameter("terminalId",terminalId);

            Long num = (Long)query.uniqueResult();

            if(num > 0) {
                TerminalInfo terminalInfo = session.get(TerminalInfo.class, terminalId);
                if(terminalInfo == null) {
                    return -1;
                }
                return terminalInfo.getWorkStatus();
            } else {
                return -2;
            }
        } catch (Exception e) {
            logger.error("Check terminal info error,terminal id("+ terminalId +"),error info("+ e.getMessage() +")");
        }

        return -3;
    }

    public TerminalInfo getTerminalInfoById(String terminalId) {
        Session session = sessionFactory.getCurrentSession();
        try {
            TerminalInfo terminalInfo = session.get(TerminalInfo.class, terminalId);
            return terminalInfo;
        } catch (Exception e) {
            logger.error("Get terminal info error,terminal id("+ terminalId +"),error info("+ e.getMessage() +")");
        }
        return null;
    }

    public boolean updateTerminalVersion(String terminalId,String terminalVersion) {
        Session session = sessionFactory.getCurrentSession();
        try {
            Query query = session.createQuery ("update TerminalInfo t set t.terminalVersion = :terminalVersion where t.id=:terminalId");
            query.setParameter("terminalId",terminalId);
            query.setParameter("terminalVersion",terminalVersion);
            return query.executeUpdate() > 0;
        } catch (Exception e) {
            logger.error("Update terminal status error,error info("+ e.getMessage() +")");
            return false;
        }
    }
}
