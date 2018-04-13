package com.sct.mailsecurityscanserver.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.sct.mailsecurityscanserver.initialize.InitializeBean;
import com.sct.mailsecurityscanserver.initialize.InitializeData;
import com.sct.mailsecurityscanserver.util.DeferredResultPool;
import com.sct.mailsecurityscanserver.manage.WorkerConsumer;
import com.sct.mailsecurityscanserver.manage.WorkerMsgProducer;
import com.sct.mailsecurityscanserver.manage.ResultDealConsumer;
import com.sct.mailsecurityscanserver.manage.ResultMsgProducer;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import redis.clients.jedis.ShardedJedisPool;
import java.util.Date;

public final class WebContextListener implements ServletContextListener {
    private static final Logger logger = Logger.getLogger(WebContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent arg) {        
        ApplicationContext ctx = InitializeBean.getApplicationContext();
        Resource res = ctx.getResource("/WEB-INF/classes/config/context.properties");
        Properties props = new Properties();
        try {
            props.load(res.getInputStream());
            InitializeData.setSessionInfoTimeOutInterval(Integer.parseInt(props.getProperty("SessionInfoTimeOutInterval")));
            InitializeData.setLoginRandomNumTimeOutInterval(Integer.parseInt(props.getProperty("LoginRandomNumTimeOutInterval")));
            InitializeData.setLoginTimeOutInterval(Integer.parseInt(props.getProperty("LoginTimeOutInterval")));
            InitializeData.setWaitResponseTime(Integer.parseInt(props.getProperty("WaitResponseTime")));
            InitializeData.setQueueServerAddress(props.getProperty("QueueServerAddress"));
            InitializeData.setQueueWorkerMsgTopic(props.getProperty("QueueWorkerMsgTopic"));     
            InitializeData.setQueueResultMsgTopic(props.getProperty("QueueResultMsgTopic"));     
            InitializeData.setMaxDeferredResultPoolSize(Integer.parseInt(props.getProperty("MaxDeferredResultPoolSize")));

            DeferredResultPool<String> strDeferredResultPool = new DeferredResultPool<String>(InitializeData.getMaxDeferredResultPoolSize());
            InitializeData.setStrDeferredResultPool(strDeferredResultPool);

            DeferredResultPool<byte[]> byteDeferredResultPool = new DeferredResultPool<byte[]>(InitializeData.getMaxDeferredResultPoolSize());
            InitializeData.setByteDeferredResultPool(byteDeferredResultPool);
            
            WorkerMsgProducer workerMsgProducer=WorkerMsgProducer.getInstance();
            if(workerMsgProducer.startQueue()==false) {
                logger.error("Start queue error!");
                return;
            }
            
            WorkerConsumer workerConsumer=WorkerConsumer.getInstance();
            if(workerConsumer.startDeal()==false) {
                logger.error("Start deal error!");
                return;
            }       
            
            ResultMsgProducer resultMsgProducer=ResultMsgProducer.getInstance();
            if(resultMsgProducer.startQueue()==false) {
                logger.error("Start queue error!");
                return;
            }
            
            ResultDealConsumer resultDealConsumer=ResultDealConsumer.getInstance();
            if(resultDealConsumer.startDeal()==false) {
                logger.error("Start deal error!");
                return;
            }
        } catch (Exception e) {
            logger.error("Initialize lost navigation server error,error code("+ e.getMessage() +")!");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        try {
            ApplicationContext ctx = InitializeBean.getApplicationContext();                                
            ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) ctx.getBean("taskExecutor");
            taskExecutor.shutdown();
            WorkerConsumer.getInstance().stopDeal();
            WorkerMsgProducer.getInstance().stopQueue();
            ResultDealConsumer.getInstance().stopDeal();
            ResultMsgProducer.getInstance().stopQueue();
        } catch (Exception e) {
        }
    }
}
