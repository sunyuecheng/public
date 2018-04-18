package com.sct.springsecuritytest.service;

import com.sct.springsecuritytest.commond.ClientCommondInfo;
import com.sct.springsecuritytest.commond.ServerCommondInfo;

public interface IComponentsDataService {

    public ServerCommondInfo execute(ClientCommondInfo clientCommondInfo);
}
