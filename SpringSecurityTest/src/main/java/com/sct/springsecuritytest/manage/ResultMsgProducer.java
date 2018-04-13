package com.sct.mailsecurityscanserver.manage;

import com.sct.mailsecurityscanserver.commond.ServerCommondInfo;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;  
import org.apache.rocketmq.client.producer.SendResult;  
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import com.sct.mailsecurityscanserver.initialize.InitializeData;
import com.sct.mailsecurityscanserver.struct.ResponseInfo;
import org.apache.log4j.Logger;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import static com.sct.mailsecurityscanserver.struct.ResponseInfo.RESPONSE_TYPE_BYTE;

public class ResultMsgProducer {   
    
    private static final Logger logger = Logger.getLogger(ResultMsgProducer.class);   
    private static DefaultMQProducer producer = new DefaultMQProducer("ResultMsgProducer"); 
    
    private static ResultMsgProducer instance;    
    public static ResultMsgProducer getInstance() {
        if (instance == null) {
            instance = new ResultMsgProducer();
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
    
    synchronized public boolean addStrDataQueue(String responseTopic, ServerCommondInfo serverCommondInfo, String responseKey) {
        if(serverCommondInfo==null || responseKey==null) {
            return false;
        }   
        
        ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setCommondInfo(serverCommondInfo);
        responseInfo.setResponseKey(responseKey);
        
        boolean ret=true;
        try {         
            ByteArrayOutputStream byteArray =new ByteArrayOutputStream();
            ObjectOutputStream ObjOutput = new ObjectOutputStream(byteArray);
            ObjOutput.writeObject(responseInfo);
            byte[] body = byteArray.toByteArray();
            
            Message msg = new Message(responseTopic,body);                
            SendResult result = producer.send(msg);
            if(result.getSendStatus()!=SendStatus.SEND_OK) {
                logger.error("Send message error,error info("+result.getSendStatus()+")!");
                ret=false;
            }
        } catch (Exception e) {              
            logger.error("Send message error,error info("+e.getMessage()+")!");
            ret=false;
        }
        return ret;
    }

    synchronized public boolean addByteDataQueue(String responseTopic, byte[] serverCommondInfo, String responseKey) {
        if(serverCommondInfo==null || responseKey==null) {
            return false;
        }

        ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setCommondData(serverCommondInfo);
        responseInfo.setResponseType(RESPONSE_TYPE_BYTE);
        responseInfo.setResponseKey(responseKey);

        boolean ret=true;
        try {
            ByteArrayOutputStream byteArray =new ByteArrayOutputStream();
            ObjectOutputStream ObjOutput = new ObjectOutputStream(byteArray);
            ObjOutput.writeObject(responseInfo);
            byte[] body = byteArray.toByteArray();

            Message msg = new Message(responseTopic,body);
            SendResult result = producer.send(msg);
            if(result.getSendStatus()!=SendStatus.SEND_OK) {
                logger.error("Send message error,error info("+result.getSendStatus()+")!");
                ret=false;
            }
        } catch (Exception e) {
            logger.error("Send message error,error info("+e.getMessage()+")!");
            ret=false;
        }
        return ret;
    }
}
