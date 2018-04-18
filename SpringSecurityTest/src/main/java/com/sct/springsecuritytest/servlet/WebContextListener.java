package com.sct.springsecuritytest.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.sct.springsecuritytest.initialize.InitializeBean;
import com.sct.springsecuritytest.initialize.InitializeData;

import java.util.Properties;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public final class WebContextListener implements ServletContextListener {
    private static final Logger logger = Logger.getLogger(WebContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent arg) {        
        ApplicationContext ctx = InitializeBean.getApplicationContext();
        Resource res = ctx.getResource("/WEB-INF/classes/config/context.properties");
        Properties props = new Properties();
        try {
            props.load(res.getInputStream());
            InitializeData.setTokenTimeOutInterval(Integer.parseInt(props.getProperty("LoginTimeOutInterval")));
            InitializeData.setWaitResponseTime(Integer.parseInt(props.getProperty("WaitResponseTime")));

        } catch (Exception e) {
            logger.error("Initialize lost navigation server error,error code("+ e.getMessage() +").");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        try {
            ApplicationContext ctx = InitializeBean.getApplicationContext();                                
            ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) ctx.getBean("taskExecutor");
            taskExecutor.shutdown();
        } catch (Exception e) {
        }
    }
}
