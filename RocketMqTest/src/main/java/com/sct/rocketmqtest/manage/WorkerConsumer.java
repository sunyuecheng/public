package com.sct.rocketmqtest.manage;

import com.sct.rocketmqtest.initialize.InitializeData;
import org.apache.log4j.Logger;
import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.springframework.stereotype.Component;

import java.util.*;

import static org.apache.rocketmq.client.consumer.PullStatus.FOUND;


@Component
public class WorkerConsumer {

    private static final Logger logger = Logger.getLogger(WorkerConsumer.class);
    private DefaultMQPushConsumer pushConsumer = new DefaultMQPushConsumer("WorkerPushConsumer");
    private DefaultMQPullConsumer pullConsumer = new DefaultMQPullConsumer("WorkerPullConsumer");
    private static String msgTopics = null;
    private static String tags = null;

    private static final Map<MessageQueue, Long> OFFSE_TABLE = new HashMap<MessageQueue, Long>();


    public boolean startPushDeal(String msgTopic, String tag) {
        this.msgTopics = msgTopic;
        this.tags = tag;

        boolean ret = true;
        try {
            pushConsumer.setNamesrvAddr(InitializeData.getQueueServerAddress());

            pushConsumer.subscribe(msgTopic, tag);
            pushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            pushConsumer.registerMessageListener(
                    new MessageListenerConcurrently() {
                        @Override
                        public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext Context) {
                            for(int i=0;i<list.size();i++) {
                                logger.error("Message topic("+list.get(i).getTopic()+"), "
                                        + "message tag("+list.get(i).getTags()+ "), "
                                        + "message keys("+list.get(i).getKeys()+ "), "
                                        + "message count(" + i + "),"
                                        + "message receive time(" + (new Date()).getTime() + ") "
                                        + "thread id("+ Thread.currentThread().getName() +")!");

                            }
                            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                        }
                    }
            );
            pushConsumer.start();
        } catch (MQClientException e) {
            logger.error("Start push deal queue error,error info(" + e.getMessage() + ")!");
            ret = false;
        }
        return ret;
    }

    public boolean startPushDealInOrder(String msgTopic, String tag, Integer orderId) {
        this.msgTopics = msgTopic;
        this.tags = tag;

        boolean ret = true;
        try {
            pushConsumer.setNamesrvAddr(InitializeData.getQueueServerAddress());

            pushConsumer.subscribe(msgTopic, tag);
            pushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            pushConsumer.registerMessageListener(new MessageListenerOrderly() {

                @Override
                public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext context) {
                    context.setAutoCommit(false);
                    for(int i=0;i<list.size();i++) {
                        logger.error("Message topic("+list.get(i).getTopic()+"), "
                                + "message tag("+list.get(i).getTags()+ "), "
                                + "message keys("+list.get(i).getKeys()+ "), "
                                + "message count(" + i + "),"
                                + "message receive time(" + (new Date()).getTime() + ") "
                                + "thread id("+ Thread.currentThread().getName() +")!");

                    }
                    return ConsumeOrderlyStatus.SUCCESS;
                }
            });

            pushConsumer.start();
        } catch (MQClientException e) {
            logger.error("Start push deal in order queue error,error info(" + e.getMessage() + ")!");
            ret = false;
        }
        return ret;
    }

    public boolean startPullDeal(String msgTopic, String tag) {
        this.msgTopics = msgTopic;
        this.tags = tag;

        boolean ret = true;
        try {
            pullConsumer.setNamesrvAddr(InitializeData.getQueueServerAddress());

            pullConsumer.start();
            Set<MessageQueue> mqs = pullConsumer.fetchSubscribeMessageQueues(msgTopic);
            for (MessageQueue mq : mqs) {
                while (true) {
                    try {
                        PullResult pullResult =
                                pullConsumer.pullBlockIfNotFound(mq, null, getMessageQueueOffset(mq), 1);

                        putMessageQueueOffset(mq, pullResult.getNextBeginOffset());
                        if(pullResult.getPullStatus()==FOUND) {
                            List<MessageExt> list = pullResult.getMsgFoundList();
                            for(int i=0;i<list.size();i++) {
                                logger.error("Message topic("+list.get(i).getTopic()+"), "
                                        + "message tag("+list.get(i).getTags()+ "), "
                                        + "message keys("+list.get(i).getKeys()+ "), "
                                        + "message count(" + i + "),"
                                        + "message receive time(" + (new Date()).getTime() + ") "
                                        + "thread id("+ Thread.currentThread().getName() +")!");

                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (MQClientException e) {
            logger.error("Start pull deal queue error,error info(" + e.getMessage() + ")!");
            ret = false;
        }
        return ret;
    }

    public void stopDeal() {

        pushConsumer.shutdown();
        pullConsumer.shutdown();
    }

    private static long getMessageQueueOffset(MessageQueue mq) {
        Long offset = OFFSE_TABLE.get(mq);
        if (offset != null)
            return offset;

        return 0;
    }

    private static void putMessageQueueOffset(MessageQueue mq, long offset) {
        OFFSE_TABLE.put(mq, offset);
    }
}
