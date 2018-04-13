package com.sct.mailsecurityscanserver.struct;

import com.sct.mailsecurityscanserver.commond.ServerCommondInfo;

import java.io.Serializable;

public class ResponseInfo implements Serializable{

    public static final int RESPONSE_TYPE_STR = 0;
    public static final int RESPONSE_TYPE_BYTE = 1;

    private String responseKey=null;
    private Integer responseType = RESPONSE_TYPE_STR;
    private ServerCommondInfo commondInfo=null;
    private byte[] commondData = null;

    public ResponseInfo() {
    }

    public ServerCommondInfo getCommondInfo() {
        return commondInfo;
    }

    public void setCommondInfo(ServerCommondInfo commondInfo) {
        this.commondInfo = commondInfo;
    }

    public String getResponseKey() {
        return responseKey;
    }

    public void setResponseKey(String responseKey) {
        this.responseKey = responseKey;
    }

    public Integer getResponseType() {
        return responseType;
    }

    public void setResponseType(Integer responseType) {
        this.responseType = responseType;
    }

    public byte[] getCommondData() {
        return commondData;
    }

    public void setCommondData(byte[] commondData) {
        this.commondData = commondData;
    }
}
