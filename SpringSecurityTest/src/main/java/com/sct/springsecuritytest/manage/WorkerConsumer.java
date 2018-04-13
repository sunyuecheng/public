package com.sct.mailsecurityscanserver.manage;

import com.sct.mailsecurityscanserver.struct.RequestInfo;
import com.sct.mailsecurityscanserver.initialize.InitializeData;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import com.sct.mailsecurityscanserver.initialize.InitializeBean;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import static com.sct.mailsecurityscanserver.struct.ResponseInfo.RESPONSE_TYPE_STR;


@Component
public class WorkerConsumer {

    private static final Logger logger = Logger.getLogger(WorkerConsumer.class);
    private static final DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("WorkerConsumer");

    private static WorkerConsumer instance;

    public static WorkerConsumer getInstance() {
        if (instance == null) {
            instance = new WorkerConsumer();
        }
        return instance;
    }

    public boolean startDeal() {
        boolean ret = true;
        try {
            consumer.setNamesrvAddr(InitializeData.getQueueServerAddress());

            consumer.subscribe(InitializeData.getQueueWorkerMsgTopic(), "");
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            consumer.registerMessageListener(
                    new MessageListenerConcurrently() {
                        @Override
                        public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext Context) {
                            Message msg = list.get(0);
                            try {
                                ApplicationContext ctx = InitializeBean.getApplicationContext();                                
                                ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) ctx.getBean("taskExecutor");

                                ByteArrayInputStream byteArray = new ByteArrayInputStream(msg.getBody());
                                ObjectInputStream objInput = new ObjectInputStream(byteArray);
                                RequestInfo requestInfo = (RequestInfo) objInput.readObject();
                                if (requestInfo != null) {
                                    if(taskExecutor.getActiveCount()<taskExecutor.getMaxPoolSize()) {
                                        if(requestInfo.getResponseType().equals(RESPONSE_TYPE_STR) ) {
                                            taskExecutor.execute(new StrDataWorkerProcess(requestInfo));
                                        } else {
                                            taskExecutor.execute(new ByteDataWorkerProcess(requestInfo));
                                        }
                                    } else { 
                                        return ConsumeConcurrentlyStatus.RECONSUME_LATER;  
                                    }    
                                } else {
                                    logger.error("Read request from queue failed!");
                                }
                            } catch (Exception e) {
                                logger.error("Get request object error,error info(" + e.getMessage() + ")!");
                            }
                            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                        }
                    }
            );
            consumer.start();
        } catch (MQClientException e) {
            logger.error("Start queue error,error info(" + e.getMessage() + ")!");
            ret = false;
        }
        return ret;
    }

    public void stopDeal() {
        consumer.shutdown();
    }
}
