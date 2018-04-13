package com.sct.mailsecurityscanserver.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "t_scan_task_info")
public class ScanTaskInfo {

    @Id
    @Column(name = "id")
    private String id = null;

    @Column(name = "operator_account")
    private String operatorAccount =  null;

    @Column(name = "organization_name")
    private String organizationName = null;

    @Column(name = "department_name")
    private String departmentName = null;

    @Column(name = "scan_system_name")
    private String scanSystemName = null;

    @Column(name = "scan_date")
    private Date scanDate = null;

    @Column(name = "scan_type")
    private Integer scanType = null;

    @Column(name = "mail_web_server_address")
    private String mailWebServerAddress = null;

    @Column(name = "mail_server_address")
    private String mailServerAddress = null;

    @Column(name = "imap_port")
    private Integer imapPort =  null;

    @Column(name = "encrypt_status")
    private Integer encryptStatus =  null;

    @Column(name = "pop3_port")
    private Integer pop3Port =  null;

    @Column(name = "smtp_port")
    private Integer smtpPort =  null;

    @Column(name = "mail_sender_address")
    private String mailSenderAddress = null;

    @Column(name = "mail_receiver_address")
    private String mailReceiverAddress = null;

    @Column(name = "terminal_id")
    private String terminalId = null;

    @Column(name = "cpu_serial_num")
    private String cpuSerialNum = null;

    @Column(name = "hard_disk_serial_num")
    private String hardDiskSerialNum = null;

    @Column(name = "system_type")
    private Integer systemType = null;

    @Column(name = "system_version")
    private String systemVersion = null;

    @Column(name = "terminal_version")
    private String terminalVersion = null;

    @Column(name = "task_status")
    private Integer taskStatus = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperatorAccount() {
        return operatorAccount;
    }

    public void setOperatorAccount(String operatorAccount) {
        this.operatorAccount = operatorAccount;
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

    public Date getScanDate() {
        return scanDate;
    }

    public void setScanDate(Date scanDate) {
        this.scanDate = scanDate;
    }

    public Integer getScanType() {
        return scanType;
    }

    public void setScanType(Integer scanType) {
        this.scanType = scanType;
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

    public Integer getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }
}
