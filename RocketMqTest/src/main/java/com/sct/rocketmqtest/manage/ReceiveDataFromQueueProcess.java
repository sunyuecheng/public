package com.sct.rocketmqtest.manage;

import org.apache.log4j.Logger;
import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.common.message.MessageQueue;

import java.io.Serializable;

public class ReceiveDataFromQueueProcess implements Runnable, Serializable {
    private static final Logger logger = Logger.getLogger(ReceiveDataFromQueueProcess.class);
    private DefaultMQPullConsumer pullConsumer = null;
    private MessageQueue mq=null;
    private long nextBeginOffset = 0;

    public ReceiveDataFromQueueProcess(DefaultMQPullConsumer pullConsumer,MessageQueue mq, long nextBeginOffset) {
        this.pullConsumer = pullConsumer;
        this.mq = mq;
        this.nextBeginOffset = nextBeginOffset;
    }

    @Override
    public void run() {
        while (true) {
            try {
                PullResult pullResult =
                        pullConsumer.pullBlockIfNotFound(mq, null, nextBeginOffset, 1);
                nextBeginOffset =  pullResult.getNextBeginOffset();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
