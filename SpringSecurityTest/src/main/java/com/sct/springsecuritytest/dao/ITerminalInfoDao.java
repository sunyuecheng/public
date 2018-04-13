package com.sct.mailsecurityscanserver.dao;

import com.sct.mailsecurityscanserver.entity.TerminalInfo;
import com.sct.mailsecurityscanserver.entity.TerminalVersionInfo;

public interface ITerminalInfoDao {
    public Integer checkTerminalInfo(String terminalId, String cpuSerialNum, String hardDiskSerialNum);
    public Integer checkTerminalStatus(String terminalId);
    public TerminalInfo getTerminalInfoById(String terminalId);
    public boolean updateTerminalVersion(String terminalId,String terminalVersion);
}
