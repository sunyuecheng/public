package com.sct.hibernatetest.dao.impl;

import com.sct.hibernatetest.dao.IUserInfoCurrentSessionDao;
import com.sct.hibernatetest.entity.UserInfo;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository("UserInfoCurrentSessionTxDao")
public class UserInfoCurrentSessionTxDao implements IUserInfoCurrentSessionDao {
    private static final Logger logger = Logger.getLogger(UserInfoCurrentSessionTxDao.class);

    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public boolean insertUserInfo(UserInfo userInfo) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.save(userInfo);
        } catch (Exception e) {
            logger.error("Insert user info error,error info("+ e.getMessage() +")");
            return false;
        }
        return true;
    }

    public boolean deleteUserInfoById(String id) {
        Session session = sessionFactory.getCurrentSession();
        try {
            UserInfo userInfo = new UserInfo();
            userInfo.setId(id);
            session.delete(userInfo);
        } catch (Exception e) {
            logger.error("Delete user info error,user id("+ id +"),error info("+ e.getMessage() +")");
            return false;
        }
        return true;
    }

    public UserInfo findUserInfoById(String id) {
        Session session = sessionFactory.getCurrentSession();
        try {
            UserInfo userInfo = session.get(UserInfo.class, id);
            return userInfo;
        } catch (Exception e) {
            logger.error("Find user info error,user id("+ id +"),error info("+ e.getMessage() +")");
        }
        return null;
    }

    public boolean updateUserInfo(UserInfo userInfo) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.update(userInfo);
        } catch (Exception e) {
            logger.error("Update user info error,user id("+ userInfo.getId() +"),error info("+ e.getMessage() +")");
            return false;
        }
        return true;
    }

    public Long getUserInfoCount() {
        Session session = sessionFactory.getCurrentSession();
        try {
            Long num = (Long)session.createQuery("select count(*) from UserInfo ")
                    .uniqueResult();
            return num;
        } catch (Exception e) {
            logger.error("Get user info count error,error info("+ e.getMessage() +")");
        }
        return null;
    }

    public List<UserInfo> getUserInfoListByLastViewDate(Long beginDate, Long endDate, int pageIndex, int pageSize) {
        Session session = sessionFactory.getCurrentSession();
        try {
            Query query = session.createQuery ("from UserInfo as u where u.lastViewDate>=:beginDate and u.lastViewDate<=:endDate order by u.lastViewDate desc");
            query.setParameter("beginDate",new Date(beginDate));
            query.setParameter("endDate",new Date(endDate));
            query.setFirstResult(pageIndex*pageSize);
            query.setMaxResults(pageSize);
            List<UserInfo> userInfoList = query.list();

//            Criteria criteria = session.createCriteria(UserInfo.class);
//            criteria.add(Restrictions.between("lastViewDate", new Date(beginDate), new Date(endDate)));
//            int rowCount = (Integer) criteria.setProjection(
//                    Projections.rowCount()).uniqueResult();
//            criteria.setProjection(null);
//
//            criteria.setFirstResult(pageIndex * pageSize);
//            criteria.setMaxResults(pageSize);
//
//            List<UserInfo> userInfoList = criteria.list();

            return userInfoList;
        } catch (Exception e) {
            logger.error("Get user info list by last view date error,begin date("+ beginDate +"),begin date("+ endDate +"),error info("+ e.getMessage() +")");
        }

        return null;
    }
}