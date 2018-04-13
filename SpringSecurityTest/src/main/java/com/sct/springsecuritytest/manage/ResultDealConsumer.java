package com.sct.mailsecurityscanserver.manage;

import com.alibaba.fastjson.JSON;
import com.sct.mailsecurityscanserver.util.DeferredResultPool;
import com.sct.mailsecurityscanserver.initialize.InitializeData;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import com.sct.mailsecurityscanserver.struct.ResponseInfo;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import static com.sct.mailsecurityscanserver.struct.ResponseInfo.RESPONSE_TYPE_STR;


@Component
public class ResultDealConsumer {

    private static final Logger logger = Logger.getLogger(ResultDealConsumer.class);
    private static final DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("ResultDealConsumer");

    private static ResultDealConsumer instance;

    public static ResultDealConsumer getInstance() {
        if (instance == null) {
            instance = new ResultDealConsumer();
        }
        return instance;
    }

    public boolean startDeal() {
        boolean ret = true;
        try {
            consumer.setNamesrvAddr(InitializeData.getQueueServerAddress());

            consumer.subscribe(InitializeData.getQueueResultMsgTopic(), "");
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            consumer.registerMessageListener(
                    new MessageListenerConcurrently() {
                        @Override
                        public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext Context) {
                            Message msg = list.get(0);
                            try {                  
                                ByteArrayInputStream byteArray = new ByteArrayInputStream(msg.getBody());
                                ObjectInputStream objInput = new ObjectInputStream(byteArray);
                                ResponseInfo responseInfo = (ResponseInfo) objInput.readObject();
                                if (responseInfo != null) {
                                    String responseKey = responseInfo.getResponseKey();
                                    if(responseInfo.getResponseType()==RESPONSE_TYPE_STR) {
                                        DeferredResult<String> result = InitializeData.getStrDeferredResultPool().getDeferredResult(responseKey);
                                        if (result != null) {
                                            result.setResult(JSON.toJSONString(responseInfo.getCommondInfo()));
                                        } else {
                                            logger.error("Get response from pool failed!");
                                        }
                                    } else {
                                        DeferredResult<byte[]> result = InitializeData.getByteDeferredResultPool().getDeferredResult(responseKey);
                                        if (result != null) {
                                            result.setResult(responseInfo.getCommondData());
                                        } else {
                                            logger.error("Get response from pool failed!");
                                        }
                                    }
                                } else {
                                    logger.error("Read response from queue failed!");
                                }
                            } catch (Exception e) {
                                logger.error("Get response object error,error info(" + e.getMessage() + ")!");
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
