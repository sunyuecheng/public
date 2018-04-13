package com.sct.jdbctest;

import com.sct.jdbctest.dao.impl.UserInfoTxDao;
import com.sct.jdbctest.entity.UserInfo;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import java.io.File;
import java.net.URLDecoder;
import java.util.Date;

public class JdbcTest {
    private static final Logger logger = Logger.getLogger(JdbcTest.class);
    private static String filePath = null;

    public static void main(String[] args) {

        String path = JdbcTest.class.getProtectionDomain().getCodeSource().getLocation().getFile();
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

        SpringJdbcTxDaoTest();

    }

    public static void SpringJdbcTxDaoTest() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");

        UserInfoTxDao userInfoTxDao = (UserInfoTxDao)ctx.getBean("userInfoTxDao");

        UserInfo userInfo = new UserInfo();
        userInfo.setId("8");
        userInfo.setName("test9");
        userInfo.setRegisterDate(new Date());
        userInfoTxDao.insertUserInfo(userInfo);
    }

}
