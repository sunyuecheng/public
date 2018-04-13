package com.sct.mailsecurityscanserver.entity;

import javax.persistence.*;

@Entity
@Table(name = "t_scan_result_info")
public class ScanResultInfo {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id = null;

    @Column(name = "task_id")
    private String taskId =  null;

    @Column(name = "scan_step")
    private Integer scanStep = null;

    @Column(name = "result_detail")
    private String resultDetail = null;

    @Column(name = "mail_id")
    private String mailId = null;

    @Column(name = "scan_param")
    private String scanParam = null;

    @Column(name = "scan_mail_server_name")
    private String scanMailServerName = null;

    @Column(name = "scan_mail_server_version")
    private String scanMailServerVersion = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Integer getScanStep() {
        return scanStep;
    }

    public void setScanStep(Integer scanStep) {
        this.scanStep = scanStep;
    }

    public String getResultDetail() {
        return resultDetail;
    }

    public void setResultDetail(String resultDetail) {
        this.resultDetail = resultDetail;
    }

    public String getMailId() {
        return mailId;
    }

    public void setMailId(String mailId) {
        this.mailId = mailId;
    }

    public String getScanParam() {
        return scanParam;
    }

    public void setScanParam(String scanParam) {
        this.scanParam = scanParam;
    }

    public String getScanMailServerName() {
        return scanMailServerName;
    }

    public void setScanMailServerName(String scanMailServerName) {
        this.scanMailServerName = scanMailServerName;
    }

    public String getScanMailServerVersion() {
        return scanMailServerVersion;
    }

    public void setScanMailServerVersion(String scanMailServerVersion) {
        this.scanMailServerVersion = scanMailServerVersion;
    }
}
