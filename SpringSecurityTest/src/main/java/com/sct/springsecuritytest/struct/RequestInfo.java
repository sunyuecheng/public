package com.sct.mailsecurityscanserver.struct;

import com.sct.mailsecurityscanserver.commond.ClientCommondInfo;
import com.sct.mailsecurityscanserver.entity.SessionInfo;

import java.io.Serializable;

public class RequestInfo implements Serializable{

    private String requestIp=null;
    private String responseTopic=null;
    private ClientCommondInfo commondInfo=null;
    private SessionInfo sessionInfo=null;
    private Integer responseType = 0;
    private String responseKey=null;

    public RequestInfo() {
    }

    public String getRequestIp() {
        return requestIp;
    }

    public void setRequestIp(String requestIp) {
        this.requestIp = requestIp;
    }

    public String getResponseTopic() {
        return responseTopic;
    }

    public void setResponseTopic(String responseTopic) {
        this.responseTopic = responseTopic;
    }    

    public String getResponseKey() {
        return responseKey;
    }

    public void setResponseKey(String responseKey) {
        this.responseKey = responseKey;
    }

    public ClientCommondInfo getCommondInfo() {
        return commondInfo;
    }

    public void setCommondInfo(ClientCommondInfo commondInfo) {
        this.commondInfo = commondInfo;
    }

    public SessionInfo getSessionInfo() {
        return sessionInfo;
    }

    public void setSessionInfo(SessionInfo sessionInfo) {
        this.sessionInfo = sessionInfo;
    }

    public Integer getResponseType() {
        return responseType;
    }

    public void setResponseType(Integer responseType) {
        this.responseType = responseType;
    }
}
