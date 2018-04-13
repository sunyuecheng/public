package com.sct.mailsecurityscanserver.commond.customer.client;

public class OperatorLoginCmdInfo {
    private String account = null;
    private String key = null;
    private String terminalId = null;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }
}
