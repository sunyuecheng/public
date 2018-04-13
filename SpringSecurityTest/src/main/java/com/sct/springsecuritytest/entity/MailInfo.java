package com.sct.mailsecurityscanserver.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_mail_info")
public class MailInfo {

    public static final int MAIL_TYPE_COUNT = 2;

    public static final int MAIL_TYPE_XSS = 0;
    public static final int MAIL_TYPE_ATTACHMENT = 1;

    @Id
    @Column(name = "id")
    private String id = null;

    @Column(name = "type")
    private Integer type = null;

    @Column(name = "subject")
    private String subject = null;

    @Column(name = "body")
    private String body = null;

    @Column(name = "attachment")
    private byte[] attachment = null;

    @Column(name = "attachment_file_name")
    private String attachmentFileName = null;

    @Column(name = "attachment_file_size")
    private Integer attachmentFileSize = null;

    @Column(name = "attachment_file_md5")
    private String attechmentFileMd5 = null;

    @Column(name = "work_status")
    private Integer workStatus = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public byte[] getAttachment() {
        return attachment;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    public String getAttachmentFileName() {
        return attachmentFileName;
    }

    public void setAttachmentFileName(String attachmentFileName) {
        this.attachmentFileName = attachmentFileName;
    }

    public Integer getAttachmentFileSize() {
        return attachmentFileSize;
    }

    public void setAttachmentFileSize(Integer attachmentFileSize) {
        this.attachmentFileSize = attachmentFileSize;
    }

    public String getAttechmentFileMd5() {
        return attechmentFileMd5;
    }

    public void setAttechmentFileMd5(String attechmentFileMd5) {
        this.attechmentFileMd5 = attechmentFileMd5;
    }

    public Integer getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(Integer workStatus) {
        this.workStatus = workStatus;
    }
}
