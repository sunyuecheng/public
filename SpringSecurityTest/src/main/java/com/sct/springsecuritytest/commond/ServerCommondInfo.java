package com.sct.mailsecurityscanserver.commond;

import java.io.Serializable;

public class ServerCommondInfo implements Serializable {
    private Integer cmd = null;
    private Integer type = null;
    private Integer code = null;
    private String msg = null;
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

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
