package com.sct.rocketmqtest;

import com.sct.rocketmqtest.initialize.InitializeBean;
import com.sct.rocketmqtest.initialize.InitializeData;
import com.sct.rocketmqtest.manage.AddDataToQueueProcess;
import com.sct.rocketmqtest.manage.WorkerConsumer;
import com.sct.rocketmqtest.manage.WorkerMsgProducer;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLDecoder;
import java.util.Properties;

public final class RocketMqTest {
    private static final Logger logger = Logger.getLogger(RocketMqTest.class);
    private static String filePath = null;

    public static void main(String[] args) {
        String path = RocketMqTest.class.getProtectionDomain().getCodeSource().getLocation().getFile();
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

        //ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        ApplicationContext ctx = new FileSystemXmlApplicationContext("file:"+ filePath +"applicationContext.xml");
        InitializeBean.setApplicationContext(ctx);

        Properties props = new Properties();
        try {
            props.load(new FileInputStream(filePath + "config/context.properties"));
            InitializeData.setQueueServerAddress(props.getProperty("QueueServerAddress"));
        } catch (Exception e) {
            logger.error("Read server configure info error,error code("+ e.getMessage() +")!");
            return;
        }

        if(InitializeData.getQueueServerAddress()== null || InitializeData.getQueueServerAddress().isEmpty()) {
            logger.error("Read server configure info error!");
            return;
        }

        if(args.length >=1) {
            if (args[0].equals("-s")) {
                boolean inOrder = false;
                if(args[1].equals("-o")) {
                    inOrder= true;
                }
                Integer threadNum = Integer.valueOf(args[2]);
                Integer topicNum = Integer.valueOf(args[3]);
                Integer sendTimes = Integer.valueOf(args[4]);
                Integer waitSeconds = Integer.valueOf(args[5]);
                byte[] data = new byte[1024];

                WorkerMsgProducer.getInstance().startQueue();

                ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) ctx.getBean("taskExecutor");

                while(threadNum-->0) {
                    if (taskExecutor.getActiveCount() < taskExecutor.getMaxPoolSize()) {
                        taskExecutor.execute(new AddDataToQueueProcess(topicNum,sendTimes,waitSeconds,inOrder, data));
                    } else {
                    }
                }
                while(true) {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {

                    }
                }

            } else if (args[0].equals("-r")) {
                boolean inOrder = false;
                if(args[1].equals("-o")) {
                    inOrder= true;
                }
                Integer topicNum = Integer.valueOf(args[2]);

                WorkerConsumer workerConsumer = new WorkerConsumer();

                //for(int i =0;i < topicNum;i++) {
                    String msgTopic = "msgTop_" + 0;
                    if (inOrder) {
                        workerConsumer.startPushDealInOrder(msgTopic,"",0);
                    } else {

                    }
                //}
                while(true) {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {

                    }
                }

            }
        }
    }
}
