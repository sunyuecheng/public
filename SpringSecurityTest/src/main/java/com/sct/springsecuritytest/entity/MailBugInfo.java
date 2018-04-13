package com.sct.mailsecurityscanserver.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "t_mail_bug_info")
public class MailBugInfo {

    @Id
    @Column(name = "id")
    private String id = null;

    @Column(name = "mail_id")
    private String mailId = null;

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

    public String getMailId() {
        return mailId;
    }

    public void setMailId(String mailId) {
        this.mailId = mailId;
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
