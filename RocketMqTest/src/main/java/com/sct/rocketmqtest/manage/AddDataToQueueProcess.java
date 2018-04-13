package com.sct.rocketmqtest.manage;

import org.apache.log4j.Logger;

import java.io.Serializable;

public class AddDataToQueueProcess implements Runnable, Serializable {
    private static final Logger logger = Logger.getLogger(AddDataToQueueProcess.class);
    private Integer topicNum=null;
    private byte[] data = null;
    private Integer sendTimes = null;
    private Integer waitSeconds = null;
    private boolean inOrder = false;

    public AddDataToQueueProcess(Integer topicNum, Integer sendTimes, Integer waitSeconds,boolean inOrder, byte[] data) {
        this.topicNum = topicNum;
        this.sendTimes = sendTimes;
        this.data = data;
        this.waitSeconds = waitSeconds;
        this.inOrder = inOrder;
    }

    @Override
    public void run() {

        while ((sendTimes--) > 0) {
            for(int i =0;i < topicNum;i++) {
                String msgTopic = "msgTop_"+i;
                if(inOrder) {
                    if (!WorkerMsgProducer.getInstance().addDataToQueueInOrder(msgTopic, data, i)) {
                        logger.error("Add data to queue error,thread id(" + Thread.currentThread().getName() + ")!");
                        break;
                    }
                } else {
                    if (!WorkerMsgProducer.getInstance().addDataToQueue(msgTopic, data)) {
                        logger.error("Add data to queue error,thread id(" + Thread.currentThread().getName() + ")!");
                        break;
                    }
                }
                try {
                    Thread.sleep(waitSeconds * 1000);
                } catch (Exception e) {

                }
            }
        }
    }
}
