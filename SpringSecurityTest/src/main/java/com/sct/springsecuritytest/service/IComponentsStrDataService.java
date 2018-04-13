package com.sct.mailsecurityscanserver.service;

import com.alibaba.fastjson.JSONObject;
import com.sct.mailsecurityscanserver.commond.ClientCommondInfo;
import com.sct.mailsecurityscanserver.commond.ServerCommondInfo;
import com.sct.mailsecurityscanserver.entity.SessionInfo;

public interface IComponentsStrDataService {

    public ServerCommondInfo execute(ClientCommondInfo clientCommondInfo, SessionInfo sessionInfo);
}
