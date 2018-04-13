package com.sct.mybatistest;

import com.sct.mybatistest.dao.impl.UserInfoDao;
import com.sct.mybatistest.entity.UserInfo;
import com.sct.mybatistest.service.impl.UserInfoService;
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

public class MybatisTest {
    private static final Logger logger = Logger.getLogger(MybatisTest.class);
    private static String filePath = null;

    public static void main(String[] args) {

        String path = MybatisTest.class.getProtectionDomain().getCodeSource().getLocation().getFile();
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

        UserInfoDao userInfoOpenSessionDao = (UserInfoDao)ctx.getBean("userInfoOpenSessionDao");
        UserInfo userInfo = new UserInfo();
        userInfo.setId(UUID.randomUUID().toString());
        userInfo.setName("test");
        userInfo.setRegisterDate(new Date());

        userInfoOpenSessionDao.insertUserInfo(userInfo);
    }

    public static void HibernateDaoTest() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("bootstrap/hibernate.cfg.xml").build();
        SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();

        UserInfoDao userInfoOpenSessionDao = new UserInfoDao();
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
        String resource = "conf.xml";
        //使用类加载器加载mybatis的配置文件（它也加载关联的映射文件）
        InputStream is = Test1.class.getClassLoader().getResourceAsStream(resource);
        //构建sqlSession的工厂
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
        //使用MyBatis提供的Resources类加载mybatis的配置文件（它也加载关联的映射文件）
        //Reader reader = Resources.getResourceAsReader(resource);
        //构建sqlSession的工厂
        //SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
        //创建能执行映射文件中sql的sqlSession
        SqlSession session = sessionFactory.openSession();
        /**
         * 映射sql的标识字符串，
         * me.gacl.mapping.userMapper是userMapper.xml文件中mapper标签的namespace属性的值，
         * getUser是select标签的id属性值，通过select标签的id属性值就可以找到要执行的SQL
         */
        String statement = "me.gacl.mapping.userMapper.getUser";//映射sql的标识字符串
        //执行查询返回一个唯一user对象的sql
        User user = session.selectOne(statement, 1);
        System.out.println(user);
    }
}
