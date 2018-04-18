package com.sct.springsecuritytest.commond.request;

public class UserLoginCmdInfo {
    private String userName = null;
    private String pwd = null;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
