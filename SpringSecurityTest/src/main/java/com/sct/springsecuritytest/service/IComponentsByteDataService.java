package com.sct.mailsecurityscanserver.service;

import com.sct.mailsecurityscanserver.commond.ClientCommondInfo;
import com.sct.mailsecurityscanserver.commond.ServerCommondInfo;
import com.sct.mailsecurityscanserver.entity.SessionInfo;

public interface IComponentsByteDataService {

    public byte[] execute(ClientCommondInfo clientCommondInfo, SessionInfo sessionInfo);
}
