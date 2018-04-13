package com.sct.rocketmqtest.manage;

import com.sct.rocketmqtest.initialize.InitializeData;
import org.apache.log4j.Logger;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;

import java.util.Date;
import java.util.List;


public class WorkerMsgProducer {   
    
    private static final Logger logger = Logger.getLogger(WorkerMsgProducer.class);   
    private static DefaultMQProducer producer = new DefaultMQProducer("WorkerMsgProducer"); 
    
    private static WorkerMsgProducer instance;    
    public static WorkerMsgProducer getInstance() {
        if (instance == null) {
            instance = new WorkerMsgProducer();
        }
        return instance;
    }
    
    public boolean startQueue() {
        boolean ret=true;
        try {    
            producer.setNamesrvAddr(InitializeData.getQueueServerAddress());
            producer.start();
        } catch(MQClientException e) {
            logger.error("Start queue error,error info("+e.getMessage()+")!");
            ret=false;
        }
        return ret;
    }
    
    public void stopQueue() {
        producer.shutdown(); 
    }            
    
    public boolean addDataToQueue(String msgTopic, byte [] data) {
        if(data == null) {
            return false;
        }

        boolean ret=true;
        try {

            String keys = String.valueOf((new Date()).getTime());
            Message msg = new Message(msgTopic,"", keys,data);
            SendResult sendResult = producer.send(msg);
            if(sendResult.getSendStatus()!=SendStatus.SEND_OK) {
                logger.error("Send message error,error info("+sendResult.getSendStatus()+")!");
                ret=false;
            }
            logger.info("Send message ok, message topic("+msgTopic +"), "
                    + "message keys("+keys+ "), "
                    + "thread id("+ Thread.currentThread().getName() +")!");
        } catch (Exception e) {              
            logger.error("Send message error,error info("+e.getMessage()+")!");
            ret=false;
        }
        return ret;
    }

    public boolean addDataToQueueInOrder(String msgTopic, byte [] data, Integer orderId) {
        if(data == null) {
            return false;
        }

        boolean ret=true;
        try {

            String keys = String.valueOf((new Date()).getTime()) + "_" + orderId;
            Message msg = new Message(msgTopic,"", keys, data);
            SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    Integer id = (Integer) arg;
                    int index = id % mqs.size();
                    return mqs.get(index);
                }
            }, orderId);
            if(sendResult.getSendStatus()!=SendStatus.SEND_OK) {
                logger.error("Send message error,error info("+sendResult.getSendStatus()+")!");
                ret=false;
            }
            logger.info("Send message ok, message topic("+msgTopic +"), "
                    + "message keys("+keys+ "), "
                    + "thread id("+ Thread.currentThread().getName() +")!");
        } catch (Exception e) {
            logger.error("Send message error,error info("+e.getMessage()+")!");
            ret=false;
        }
        return ret;
    }

}
