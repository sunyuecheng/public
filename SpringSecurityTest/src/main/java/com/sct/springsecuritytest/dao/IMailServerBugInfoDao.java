package com.sct.mailsecurityscanserver.dao;

import com.sct.mailsecurityscanserver.entity.MailServerBugInfo;

public interface IMailServerBugInfoDao {
    public MailServerBugInfo getMailServerBugInfo(String mailServerName, String mailServerVersion);
}
