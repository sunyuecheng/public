package com.sct.mailsecurityscanserver.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_mail_data_download_record_info")
public class MailDataDownloadRecordInfo {

    public static final int RECORD_TYPE_ONLINE = 0;
    public static final int RECORD_TYPE_OUTLINE = 1;

    public MailDataDownloadRecordInfo() {
    }

    public MailDataDownloadRecordInfo(String operatorAccount, String terminalId, Integer type, String managerId, Date downloadDate) {
        this.operatorAccount = operatorAccount;
        this.terminalId = terminalId;
        this.type = type;
        this.managerId = managerId;
        this.downloadDate = downloadDate;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id = null;

    @Column(name = "operator_account")
    private String operatorAccount = null;

    @Column(name = "terminal_id")
    private String terminalId = null;

    @Column(name = "type")
    private Integer type = null;

    @Column(name = "manager_id")
    private String managerId = null;

    @Column(name = "download_date")
    private Date downloadDate = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOperatorAccount() {
        return operatorAccount;
    }

    public void setOperatorAccount(String operatorAccount) {
        this.operatorAccount = operatorAccount;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public Date getDownloadDate() {
        return downloadDate;
    }

    public void setDownloadDate(Date downloadDate) {
        this.downloadDate = downloadDate;
    }
}
