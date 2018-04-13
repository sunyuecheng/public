package com.sct.mailsecurityscanserver.entity;


import javax.persistence.Column;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_terminal_info")
public class TerminalInfo {
    public static final int SYSTEM_TYPE_WINDOWS = 0;
    public static final int SYSTEM_TYPE_LINUX = 1;
    public static final int SYSTEM_TYPE_IOS = 2;
    public static final int SYSTEM_TYPE_ANDROID = 3;

    @Id
    @Column(name = "id")
    private String id = null;

    @Column(name = "cpu_serial_num")
    private String cpuSerialNum = null;

    @Column(name = "hard_disk_serial_num")
    private String hardDiskSerialNum = null;

    @Column(name = "system_type")
    private Integer systemType = 0;

    @Column(name = "system_version")
    private String systemVersion = null;

    @Column(name = "register_date")
    private Date registerDate = null;

    @Column(name = "work_status")
    private Integer workStatus = null;

    @Column(name = "register_manager_id")
    private Integer registerManagerId = null;

    @Column(name = "terminal_version")
    private String terminalVersion = null;

    public static int getSystemTypeWindows() {
        return SYSTEM_TYPE_WINDOWS;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public Integer getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(Integer workStatus) {
        this.workStatus = workStatus;
    }

    public Integer getRegisterManagerId() {
        return registerManagerId;
    }

    public void setRegisterManagerId(Integer registerManagerId) {
        this.registerManagerId = registerManagerId;
    }

    public String getTerminalVersion() {
        return terminalVersion;
    }

    public void setTerminalVersion(String terminalVersion) {
        this.terminalVersion = terminalVersion;
    }
}
