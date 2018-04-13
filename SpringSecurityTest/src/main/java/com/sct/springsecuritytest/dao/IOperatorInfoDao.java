package com.sct.mailsecurityscanserver.dao;

import com.sct.mailsecurityscanserver.entity.OperatorInfo;

public interface IOperatorInfoDao {
    public OperatorInfo getOperatorInfo(String account);
}
