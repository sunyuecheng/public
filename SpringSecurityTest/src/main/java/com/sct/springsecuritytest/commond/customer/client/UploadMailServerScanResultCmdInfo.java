package com.sct.mailsecurityscanserver.commond.customer.client;

import java.util.List;

public class UploadMailServerScanResultCmdInfo {
    private String taskId = null;

    private String account =  null;
    private String organizationName = null;
    private String departmentName = null;
    private String scanSystemName = null;
    private Long scanDate = null;
    private Integer scanType = null;
    private String mailWebServerAddress = null;
    private String mailServerAddress = null;
    private Integer imapPort = null;
    private Integer encryptStatus = null;
    private Integer pop3Port = null;
    private Integer smtpPort = null;
    private String mailSenderAddress = null;
    private String mailReceiverAddress = null;

    private String terminalId = null;
    private String cpuSerialNum=null;
    private String hardDiskSerialNum=null;
    private Integer systemType = null;
    private String systemVersion = null;
    private String terminalVersion = null;

    private String step1ScanParam = null;
    private String step1MailServerName = null;
    private String step1MailServerVersion = null;
    private List<String> step2ReceiveMailIdList = null;
    private List<String> step3ReceiveMailIdList = null;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getScanSystemName() {
        return scanSystemName;
    }

    public void setScanSystemName(String scanSystemName) {
        this.scanSystemName = scanSystemName;
    }

    public Long getScanDate() {
        return scanDate;
    }

    public void setScanDate(Long scanDate) {
        this.scanDate = scanDate;
    }

    public Integer getScanType() {
        return scanType;
    }

    public void setScanType(Integer scanType) {
        this.scanType = scanType;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getMailWebServerAddress() {
        return mailWebServerAddress;
    }

    public void setMailWebServerAddress(String mailWebServerAddress) {
        this.mailWebServerAddress = mailWebServerAddress;
    }

    public String getMailServerAddress() {
        return mailServerAddress;
    }

    public void setMailServerAddress(String mailServerAddress) {
        this.mailServerAddress = mailServerAddress;
    }

    public Integer getImapPort() {
        return imapPort;
    }

    public void setImapPort(Integer imapPort) {
        this.imapPort = imapPort;
    }

    public Integer getEncryptStatus() {
        return encryptStatus;
    }

    public void setEncryptStatus(Integer encryptStatus) {
        this.encryptStatus = encryptStatus;
    }

    public Integer getPop3Port() {
        return pop3Port;
    }

    public void setPop3Port(Integer pop3Port) {
        this.pop3Port = pop3Port;
    }

    public Integer getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(Integer smtpPort) {
        this.smtpPort = smtpPort;
    }

    public String getMailSenderAddress() {
        return mailSenderAddress;
    }

    public void setMailSenderAddress(String mailSenderAddress) {
        this.mailSenderAddress = mailSenderAddress;
    }

    public String getMailReceiverAddress() {
        return mailReceiverAddress;
    }

    public void setMailReceiverAddress(String mailReceiverAddress) {
        this.mailReceiverAddress = mailReceiverAddress;
    }

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

    public String getStep1ScanParam() {
        return step1ScanParam;
    }

    public void setStep1ScanParam(String step1ScanParam) {
        this.step1ScanParam = step1ScanParam;
    }

    public String getStep1MailServerName() {
        return step1MailServerName;
    }

    public void setStep1MailServerName(String step1MailServerName) {
        this.step1MailServerName = step1MailServerName;
    }

    public String getStep1MailServerVersion() {
        return step1MailServerVersion;
    }

    public void setStep1MailServerVersion(String step1MailServerVersion) {
        this.step1MailServerVersion = step1MailServerVersion;
    }

    public List<String> getStep2ReceiveMailIdList() {
        return step2ReceiveMailIdList;
    }

    public void setStep2ReceiveMailIdList(List<String> step2ReceiveMailIdList) {
        this.step2ReceiveMailIdList = step2ReceiveMailIdList;
    }

    public List<String> getStep3ReceiveMailIdList() {
        return step3ReceiveMailIdList;
    }

    public void setStep3ReceiveMailIdList(List<String> step3ReceiveMailIdList) {
        this.step3ReceiveMailIdList = step3ReceiveMailIdList;
    }
}
