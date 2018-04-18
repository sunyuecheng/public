package com.sct.springsecuritytest.initialize;


public class InitializeData {
    private static long tokenTimeOutInterval =60*60*24*1000;
    private static long waitResponseTime=20;
    private static String tokenSecretKey="SecretKey";

    public static long getTokenTimeOutInterval() {
        return tokenTimeOutInterval;
    }

    public static void setTokenTimeOutInterval(long tokenTimeOutInterval) {
        InitializeData.tokenTimeOutInterval = tokenTimeOutInterval;
    }

    public static long getWaitResponseTime() {
        return waitResponseTime;
    }

    public static void setWaitResponseTime(long waitResponseTime) {
        InitializeData.waitResponseTime = waitResponseTime;
    }

    public static String getTokenSecretKey() {
        return tokenSecretKey;
    }

    public static void setTokenSecretKey(String tokenSecretKey) {
        InitializeData.tokenSecretKey = tokenSecretKey;
    }
}