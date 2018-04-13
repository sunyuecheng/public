package com.sct.mailsecurityscanserver.entity;

import javax.persistence.*;

@Entity
@Table(name = "t_terminal_version_info")
public class TerminalVersionInfo {

    public static final int TERMINAL_VERSION_NEWEST = 0;
    public static final int TERMINAL_VERSION_HAVE_NEW_NO_NEED_UPDATE = 1;
    public static final int TERMINAL_VERSION_HAVE_NEW_NEED_UPDATE = 2;

    public static final int SYSTEM_TYPE_WINDOWS = 0;
    public static final int SYSTEM_TYPE_LINUX = 1;
    public static final int SYSTEM_TYPE_MACOS = 2;
    public static final int SYSTEM_TYPE_ANDROID = 3;
    public static final int SYSTEM_TYPE_IOS = 4;

    @Id
    @Column(name = "terminal_version")
    private String terminalVersion = null;

    @Column(name = "system_type")
    private Integer systemType = 0;

    @Column(name = "system_version")
    private String systemVersion = null;

    @Column(name = "version_status")
    private Integer versionStatus = null;

    @Column(name = "update_version")
    private String updateVersion = null;

    @Column(name = "support_min_system_version")
    private String supportMinSystemVersion = null;

    @Column(name = "support_max_system_version")
    private String supportMaxSystemVersion = null;

    @Column(name = "support_device_resolution")
    private String supportDeviceResolution = null;

    @Column(name = "packet_size")
    private Integer packetSize = 0;

    @Column(name = "packet_md5")
    private String packetMd5 = null;

    @Column(name = "download_url")
    private String downloadUrl = null;

    public String getTerminalVersion() {
        return terminalVersion;
    }

    public void setTerminalVersion(String terminalVersion) {
        this.terminalVersion = terminalVersion;
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

    public Integer getVersionStatus() {
        return versionStatus;
    }

    public void setVersionStatus(Integer versionStatus) {
        this.versionStatus = versionStatus;
    }

    public String getUpdateVersion() {
        return updateVersion;
    }

    public void setUpdateVersion(String updateVersion) {
        this.updateVersion = updateVersion;
    }

    public String getSupportMinSystemVersion() {
        return supportMinSystemVersion;
    }

    public void setSupportMinSystemVersion(String supportMinSystemVersion) {
        this.supportMinSystemVersion = supportMinSystemVersion;
    }

    public String getSupportMaxSystemVersion() {
        return supportMaxSystemVersion;
    }

    public void setSupportMaxSystemVersion(String supportMaxSystemVersion) {
        this.supportMaxSystemVersion = supportMaxSystemVersion;
    }

    public String getSupportDeviceResolution() {
        return supportDeviceResolution;
    }

    public void setSupportDeviceResolution(String supportDeviceResolution) {
        this.supportDeviceResolution = supportDeviceResolution;
    }

    public Integer getPacketSize() {
        return packetSize;
    }

    public void setPacketSize(Integer packetSize) {
        this.packetSize = packetSize;
    }

    public String getPacketMd5() {
        return packetMd5;
    }

    public void setPacketMd5(String packetMd5) {
        this.packetMd5 = packetMd5;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
