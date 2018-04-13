package com.sct.mailsecurityscanserver.dao;

import com.sct.mailsecurityscanserver.entity.ScanResultInfo;

import java.util.List;

public interface IScanResultInfoDao {
    public List<ScanResultInfo> getScanResultInfoListById(String taskId);
    public boolean insertScanResultInfo(ScanResultInfo scanResultInfo);
}
