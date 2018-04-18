package com.sct.springsecuritytest.commond;

import java.io.Serializable;

public class ClientCommondInfo implements Serializable {
    private Integer cmd = null;
    private Integer type = null;
    private String sessionId = null;
    private String data = null;

    public Integer getCmd() {
        return cmd;
    }

    public void setCmd(Integer cmd) {
        this.cmd = cmd;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
