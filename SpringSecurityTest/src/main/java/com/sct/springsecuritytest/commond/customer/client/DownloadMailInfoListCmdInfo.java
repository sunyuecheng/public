package com.sct.mailsecurityscanserver.commond.customer.client;

import java.util.List;

public class DownloadMailInfoListCmdInfo {
    private String terminalId = null;
    private String cpuSerialNum=null;
    private String hardDiskSerialNum=null;
    private Integer systemType = null;
    private String systemVersion = null;
    private String terminalVersion = null;

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getCpuSerialNum() {
        return cpuSerialNum;
    }

    public void setCpuSerialNum(String cpuSerialNum) {
        this.cpuSerialNum = cpuSerialNum;
    }

    public String getHardDiskSerialNum() {
        return hardDiskSerialNum;
    }

    public void setHardDiskSerialNum(String hardDiskSerialNum) {
        this.hardDiskSerialNum = hardDiskSerialNum;
    }

    public Integer getSystemType() {
        return systemType;
    }

    public void setSystemType(Integer systemType) {
        this.systemType = systemType;
    }

    public String getSystemVersion() {
        return systemVersion;
    }

    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion;
    }

    public String getTerminalVersion() {
        return terminalVersion;
    }

    public void setTerminalVersion(String terminalVersion) {
        this.terminalVersion = terminalVersion;
    }
}
