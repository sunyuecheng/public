package com.sct.mailsecurityscanserver.dao;

import com.sct.mailsecurityscanserver.entity.ScanResultInfo;
import com.sct.mailsecurityscanserver.entity.ScanTaskInfo;

import java.util.List;

public interface IScanTaskInfoDao {
    public ScanTaskInfo getScanTaskInfoById(String terminalId);
    public boolean insertScanTaskInfo(ScanTaskInfo scanTaskInfo, List<ScanResultInfo> scanResultInfoList);
}
