package com.sct.hibernatetest.dao.impl;

import com.sct.hibernatetest.dao.IUserInfoOpenSessionDao;
import com.sct.hibernatetest.entity.UserInfo;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

@Repository("UserInfoOpenSessionDao")
public class UserInfoOpenSessionDao implements IUserInfoOpenSessionDao {
    private static final Logger logger = Logger.getLogger(UserInfoOpenSessionDao.class);

    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public boolean insertUserInfo(UserInfo userInfo) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.save(userInfo);
            transaction.commit();
        } catch (Exception e) {
            logger.error("Insert user info error,error info("+ e.getMessage() +")");
            if (transaction!=null) {
                transaction.rollback();
            }
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return true;
    }

    public boolean deleteUserInfoById(String id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            UserInfo userInfo = new UserInfo();
            userInfo.setId(id);
            session.delete(userInfo);
            transaction.commit();
        } catch (Exception e) {
            logger.error("Delete user info error,user id("+ id +"),error info("+ e.getMessage() +")");
            if (transaction!=null) {
                transaction.rollback();
            }
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return true;
    }

    public UserInfo findUserInfoById(String id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            UserInfo userInfo = session.get(UserInfo.class, id);
            transaction.commit();
            return userInfo;
        } catch (Exception e) {
            logger.error("Find user info error,user id("+ id +"),error info("+ e.getMessage() +")");
            if (transaction!=null) {
                transaction.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }

    public boolean updateUserInfo(UserInfo userInfo) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            //session.update(userInfo);

            Query query = session.createQuery("UPDATE UserInfo set name = :name " +
                    "WHERE id = :id");
            query.setParameter("name", userInfo.getName());
            query.setParameter("id", userInfo.getId());
            int result = query.executeUpdate();

            transaction.commit();

            return result>=1;


        } catch (Exception e) {
            logger.error("Update user info error,user id("+ userInfo.getId() +"),error info("+ e.getMessage() +")");
            if (transaction!=null) {
                transaction.rollback();
            }
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public Long getUserInfoCount() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Long num = (Long)session.createQuery("select count(*) from UserInfo ")
                    .uniqueResult();
            transaction.commit();
            return num;
        } catch (Exception e) {
            logger.error("Get user info count error,error info("+ e.getMessage() +")");
            if (transaction!=null) {
                transaction.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }

    public List<UserInfo> getUserInfoListByLastViewDate(Long beginDate, Long endDate, int pageIndex, int pageSize) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
//            Query query = session.createQuery ("from UserInfo as u where u.lastViewDate>=:beginDate and u.lastViewDate<=:endDate order by u.lastViewDate desc");
//            query.setParameter("beginDate",new Date(beginDate));
//            query.setParameter("endDate",new Date(endDate));
//            query.setFirstResult(pageIndex*pageSize);
//            query.setMaxResults(pageSize);
//            List<UserInfo> userInfoList = query.list();
//            transaction.commit();

            Criteria criteria = session.createCriteria(UserInfo.class);
            criteria.add(Restrictions.between("lastViewDate", new Date(beginDate), new Date(endDate)));
            int rowCount = (Integer) criteria.setProjection(
                    Projections.rowCount()).uniqueResult();
            criteria.setProjection(null);

            criteria.setFirstResult(pageIndex * pageSize);
            criteria.setMaxResults(pageSize);

            List<UserInfo> userInfoList = criteria.list();

            return userInfoList;
        } catch (Exception e) {
            logger.error("Get user info list by last view date error,begin date("+ beginDate +"),begin date("+ endDate +"),error info("+ e.getMessage() +")");
            if (transaction!=null) {
                transaction.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return null;
    }
}
