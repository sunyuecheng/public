package com.sct.mailsecurityscanserver.dao;

import com.sct.mailsecurityscanserver.entity.TerminalVersionInfo;

public interface ITerminalVersionInfoDao {
    public Integer checkTerminalVersionInfo(Integer systemType, String systemVersion, String terminalVersion);
    public TerminalVersionInfo getUploadTerminalVersionInfo(Integer systemType, String systemVersion, String terminalVersion);
}
