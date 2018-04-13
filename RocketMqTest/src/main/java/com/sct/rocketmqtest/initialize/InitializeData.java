package com.sct.rocketmqtest.initialize;



public class InitializeData {
    private static String queueServerAddress=null;
    public static String getQueueServerAddress() {
        return queueServerAddress;
    }

    public static void setQueueServerAddress(String queueServerAddress) {
        InitializeData.queueServerAddress = queueServerAddress;
    }
}