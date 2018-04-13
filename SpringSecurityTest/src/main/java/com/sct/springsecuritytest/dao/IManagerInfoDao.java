package com.sct.mailsecurityscanserver.dao;

import com.sct.mailsecurityscanserver.entity.ManagerInfo;

public interface IManagerInfoDao {
    public ManagerInfo getManagerInfo(String account);
}
