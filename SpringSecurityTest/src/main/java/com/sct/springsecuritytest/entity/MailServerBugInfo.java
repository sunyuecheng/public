package com.sct.mailsecurityscanserver.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "t_mail_server_bug_info")
public class MailServerBugInfo {

    @Id
    @Column(name = "id")
    private String id = null;

    @Column(name = "server_name")
    private String serverName = null;

    @Column(name = "server_description")
    private String serverDescription = null;

    @Column(name = "server_version")
    private String serverVersion = null;

    @Column(name = "bug_detail")
    private String bugDetail = null;

    @Column(name = "level")
    private Integer level = null;

    @Column(name = "is_public")
    private Boolean isPublic = null;

    @Column(name = "public_id")
    private String publicId = null;

    @Column(name = "public_date")
    private Date publicDate = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerDescription() {
        return serverDescription;
    }

    public void setServerDescription(String serverDescription) {
        this.serverDescription = serverDescription;
    }

    public String getServerVersion() {
        return serverVersion;
    }

    public void setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
    }

    public String getBugDetail() {
        return bugDetail;
    }

    public void setBugDetail(String bugDetail) {
        this.bugDetail = bugDetail;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public Date getPublicDate() {
        return publicDate;
    }

    public void setPublicDate(Date publicDate) {
        this.publicDate = publicDate;
    }
}
