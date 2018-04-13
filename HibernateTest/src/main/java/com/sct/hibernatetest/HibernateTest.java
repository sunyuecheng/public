package com.sct.hibernatetest;

import com.sct.hibernatetest.dao.impl.UserInfoCurrentSessionDao;
import com.sct.hibernatetest.dao.impl.UserInfoCurrentSessionTxDao;
import com.sct.hibernatetest.dao.impl.UserInfoOpenSessionDao;
import com.sct.hibernatetest.entity.UserInfo;
import com.sct.hibernatetest.service.impl.UserInfoService;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import java.io.File;
import java.net.URLDecoder;
import java.util.Date;
import java.util.UUID;

public class HibernateTest {
    private static final Logger logger = Logger.getLogger(HibernateTest.class);
    private static String filePath = null;

    public static void main(String[] args) {

        String path = HibernateTest.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        try {
            path = java.net.URLDecoder.decode(path, "UTF-8");
        }
        catch (java.io.UnsupportedEncodingException e) {
            return;
        }
        File file = new File(path);
        filePath = file.getParent() + "/";

        String configPath = filePath + "config/log4j.properties";
        try {
            configPath = URLDecoder.decode(configPath, "UTF-8");
            PropertyConfigurator.configure(configPath);
        } catch (Exception e) {
            return;
        }

        SpringHibernateTxDaoTest();

    }

    public static void SpringHibernateTxDaoTest() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");

        UserInfoService userInfoService = (UserInfoService)ctx.getBean("UserInfoService");
        userInfoService.doSomeThing();
    }

    public static void SpringHibernateDaoTest() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");

        UserInfoOpenSessionDao userInfoOpenSessionDao = (UserInfoOpenSessionDao)ctx.getBean("userInfoOpenSessionDao");
        UserInfo userInfo = new UserInfo();
        userInfo.setId(UUID.randomUUID().toString());
        userInfo.setName("test");
        userInfo.setRegisterDate(new Date());

        userInfoOpenSessionDao.insertUserInfo(userInfo);
    }

    public static void HibernateDaoTest() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("bootstrap/hibernate.cfg.xml").build();
        SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();

        UserInfoOpenSessionDao userInfoOpenSessionDao = new UserInfoOpenSessionDao();
        userInfoOpenSessionDao.setSessionFactory(sessionFactory);
        userInfoOpenSessionDao.getUserInfoCount();

        UserInfoCurrentSessionDao userInfoCurrentSessionDao = new UserInfoCurrentSessionDao();
        userInfoCurrentSessionDao.setSessionFactory(sessionFactory);

        UserInfo userInfo = new UserInfo();
        userInfo.setId(UUID.randomUUID().toString());
        userInfo.setName("test");
        userInfo.setRegisterDate(new Date());

        userInfoCurrentSessionDao.insertUserInfo(userInfo);
    }

    void HibernateTest() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("bootstrap/hibernate.cfg.xml").build();
        SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();

        Session session = sessionFactory.openSession();

        Transaction transaction = session.beginTransaction();
        try {
            UserInfo userInfo = new UserInfo();
            userInfo.setId("4");
            userInfo.setName("test1");
            userInfo.setRegisterDate(new Date());

            session.save(userInfo);
            transaction.commit();

            session.beginTransaction();
            UserInfo userInfo1 = session.get(UserInfo.class,"1");
            transaction.commit();

        } catch (Exception e) {
            if (transaction!=null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
    }
}
