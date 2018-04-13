package com.sct.mailsecurityscanserver.dao;

import com.sct.mailsecurityscanserver.entity.MailBugInfo;

import java.util.List;

public interface IMailBugInfoDao {
    public List<MailBugInfo> getMailBugInfoList(Integer pageIndex, Integer pageSize);
    public MailBugInfo getMailBugInfoByMailId(String mailId);
    public boolean insertMailBugInfo(MailBugInfo mailBugInfo);
}
