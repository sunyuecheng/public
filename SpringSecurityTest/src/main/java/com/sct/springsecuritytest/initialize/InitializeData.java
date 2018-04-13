package com.sct.mailsecurityscanserver.initialize;


import com.sct.mailsecurityscanserver.util.DeferredResultPool;

public class InitializeData {
    private static long sessionInfoTimeOutInterval=60*60*24*1000;
    private static long loginRandomNumTimeOutInterval=5*60*1000;
    private static long loginTimeOutInterval=60*60*24*1000;
    private static long waitResponseTime=20;  
    private static String queueServerAddress=null;    
    private static String queueWorkerMsgTopic=null;
    private static String queueResultMsgTopic=null;
    private static int maxDeferredResultPoolSize=0;
    private static DeferredResultPool<String> strDeferredResultPool=null;
    private static DeferredResultPool<byte[] > byteDeferredResultPool=null;

    public static long getSessionInfoTimeOutInterval() {
        return sessionInfoTimeOutInterval;
    }

    public static void setSessionInfoTimeOutInterval(long sessionInfoTimeOutInterval) {
        InitializeData.sessionInfoTimeOutInterval = sessionInfoTimeOutInterval;
    }

    public static long getLoginRandomNumTimeOutInterval() {
        return loginRandomNumTimeOutInterval;
    }

    public static void setLoginRandomNumTimeOutInterval(long loginRandomNumTimeOutInterval) {
        InitializeData.loginRandomNumTimeOutInterval = loginRandomNumTimeOutInterval;
    }

    public static long getLoginTimeOutInterval() {
        return loginTimeOutInterval;
    }

    public static void setLoginTimeOutInterval(long loginTimeOutInterval) {
        InitializeData.loginTimeOutInterval = loginTimeOutInterval;
    }

    public static long getWaitResponseTime() {
        return waitResponseTime;
    }

    public static void setWaitResponseTime(long waitResponseTime) {
        InitializeData.waitResponseTime = waitResponseTime;
    }

    public static String getQueueServerAddress() {
        return queueServerAddress;
    }

    public static void setQueueServerAddress(String queueServerAddress) {
        InitializeData.queueServerAddress = queueServerAddress;
    }

    public static String getQueueWorkerMsgTopic() {
        return queueWorkerMsgTopic;
    }

    public static void setQueueWorkerMsgTopic(String queueWorkerMsgTopic) {
        InitializeData.queueWorkerMsgTopic = queueWorkerMsgTopic;
    }

    public static String getQueueResultMsgTopic() {
        return queueResultMsgTopic;
    }

    public static void setQueueResultMsgTopic(String queueResultMsgTopic) {
        InitializeData.queueResultMsgTopic = queueResultMsgTopic;
    }

    public static int getMaxDeferredResultPoolSize() {
        return maxDeferredResultPoolSize;
    }

    public static void setMaxDeferredResultPoolSize(int maxDeferredResultPoolSize) {
        InitializeData.maxDeferredResultPoolSize = maxDeferredResultPoolSize;
    }

    public static DeferredResultPool<String> getStrDeferredResultPool() {
        return strDeferredResultPool;
    }

    public static void setStrDeferredResultPool(DeferredResultPool<String> strDeferredResultPool) {
        InitializeData.strDeferredResultPool = strDeferredResultPool;
    }

    public static DeferredResultPool<byte[]> getByteDeferredResultPool() {
        return byteDeferredResultPool;
    }

    public static void setByteDeferredResultPool(DeferredResultPool<byte[]> byteDeferredResultPool) {
        InitializeData.byteDeferredResultPool = byteDeferredResultPool;
    }
}