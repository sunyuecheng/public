package com.sct.mailsecurityscanserver.dao;

import com.sct.mailsecurityscanserver.entity.MailInfo;

import java.util.List;

public interface IMailInfoDao {
    public List<MailInfo> getMailInfoListByType(Integer type, Integer pageIndex, Integer pageSize);
    public boolean insertMailInfo(MailInfo mailInfo);
    public boolean setMailStatus(String id, Integer workStatus);
}
