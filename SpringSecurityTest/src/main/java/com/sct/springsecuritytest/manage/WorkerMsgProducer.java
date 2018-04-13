package com.sct.mailsecurityscanserver.manage;

import com.sct.mailsecurityscanserver.commond.ClientCommondInfo;
import com.sct.mailsecurityscanserver.util.DeferredResultPool;
import com.sct.mailsecurityscanserver.entity.SessionInfo;
import com.sct.mailsecurityscanserver.initialize.InitializeData;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;  
import org.apache.rocketmq.client.producer.SendResult;  
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import com.sct.mailsecurityscanserver.struct.RequestInfo;
import org.apache.log4j.Logger;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import org.springframework.web.context.request.async.DeferredResult;

import static com.sct.mailsecurityscanserver.struct.ResponseInfo.*;

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
    
    synchronized public boolean addStrDataQueue(String requestIp, ClientCommondInfo commondInfo, SessionInfo sessionInfo, DeferredResult<String> responseResult) {
        if(commondInfo == null || responseResult==null) {
            return false;
        }      
        
        if(!InitializeData.getStrDeferredResultPool().checkPoolStatus()) {
            logger.error("Deferred result pool is full!");
            return false;
        }
        String responseKey=InitializeData.getStrDeferredResultPool().addDeferredResult(responseResult);
        if(responseKey==null) {
            logger.error("Add deferred result to pool failed!");
            return false;
        }
        
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setRequestIp(requestIp);
        requestInfo.setResponseTopic(InitializeData.getQueueResultMsgTopic());
        requestInfo.setCommondInfo(commondInfo);
        requestInfo.setSessionInfo(sessionInfo);
        requestInfo.setResponseType(RESPONSE_TYPE_STR);
        requestInfo.setResponseKey(responseKey);
        
        boolean ret=true;
        try {         
            ByteArrayOutputStream byteArray =new ByteArrayOutputStream();
            ObjectOutputStream ObjOutput = new ObjectOutputStream(byteArray);
            ObjOutput.writeObject(requestInfo);
            byte[] body = byteArray.toByteArray();
            
            Message msg = new Message(InitializeData.getQueueWorkerMsgTopic(),body);                
            SendResult sendResult = producer.send(msg);
            if(sendResult.getSendStatus()!=SendStatus.SEND_OK) {
                logger.error("Send message error,error info("+sendResult.getSendStatus()+")!");
                ret=false;
            }
        } catch (Exception e) {              
            logger.error("Send message error,error info("+e.getMessage()+")!");
            ret=false;
        }
        return ret;
    }

    synchronized public boolean addByteDataQueue(String requestIp, ClientCommondInfo commondInfo, SessionInfo sessionInfo, DeferredResult<byte[]> responseResult) {
        if(commondInfo == null || responseResult==null) {
            return false;
        }

        if(!InitializeData.getByteDeferredResultPool().checkPoolStatus()) {
            logger.error("Deferred result pool is full!");
            return false;
        }
        String responseKey=InitializeData.getByteDeferredResultPool().addDeferredResult(responseResult);
        if(responseKey==null) {
            logger.error("Add deferred result to pool failed!");
            return false;
        }

        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setRequestIp(requestIp);
        requestInfo.setResponseTopic(InitializeData.getQueueResultMsgTopic());
        requestInfo.setCommondInfo(commondInfo);
        requestInfo.setSessionInfo(sessionInfo);
        requestInfo.setResponseType(RESPONSE_TYPE_BYTE);
        requestInfo.setResponseKey(responseKey);

        boolean ret=true;
        try {
            ByteArrayOutputStream byteArray =new ByteArrayOutputStream();
            ObjectOutputStream ObjOutput = new ObjectOutputStream(byteArray);
            ObjOutput.writeObject(requestInfo);
            byte[] body = byteArray.toByteArray();

            Message msg = new Message(InitializeData.getQueueWorkerMsgTopic(),body);
            SendResult sendResult = producer.send(msg);
            if(sendResult.getSendStatus()!=SendStatus.SEND_OK) {
                logger.error("Send message error,error info("+sendResult.getSendStatus()+")!");
                ret=false;
            }
        } catch (Exception e) {
            logger.error("Send message error,error info("+e.getMessage()+")!");
            ret=false;
        }
        return ret;
    }
}
