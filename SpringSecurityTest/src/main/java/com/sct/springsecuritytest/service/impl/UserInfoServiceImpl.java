package com.sct.springsecuritytest.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sct.springsecuritytest.commond.ClientCommondInfo;
import com.sct.springsecuritytest.commond.ServerCommondInfo;
import com.sct.springsecuritytest.commond.request.UserInfoCmdInfo;
import com.sct.springsecuritytest.dao.IUserInfoDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.sct.springsecuritytest.define.ErrorCodeDefine.*;

@Service("UserInfoService")
public class UserInfoServiceImpl implements com.sct.springsecuritytest.service.IComponentsDataService {

    private static final Logger logger = Logger.getLogger(UserInfoServiceImpl.class);
    
    @Autowired
    private IUserInfoDao userInfoDao;

    @Override
    public ServerCommondInfo execute(ClientCommondInfo clientCommondInfo) {
        ServerCommondInfo serverCommondInfo = new ServerCommondInfo();

        if (clientCommondInfo == null) {
            serverCommondInfo.setCode(RESULT_PARAM_ERROR);
            serverCommondInfo.setMsg("Error param.");
            logger.error("Error param.");
            return serverCommondInfo;
        }

        Integer type = clientCommondInfo.getType();
        if(type == null) {
            serverCommondInfo.setCode(RESULT_INVALID_CMD_TYPE_ERROR);
            serverCommondInfo.setMsg("Invaild cmd type.");
            logger.error("Invaild cmd type.");
            return serverCommondInfo;
        }
        serverCommondInfo.setCmd(clientCommondInfo.getCmd());
        serverCommondInfo.setType(clientCommondInfo.getType());

        if (type==0) {
            String data = clientCommondInfo.getData();
            if(data!=null && !data.isEmpty()) {
                try {
                    UserInfoCmdInfo userInfoCmdInfo =
                            JSONObject.parseObject(data, UserInfoCmdInfo.class);


                    return serverCommondInfo;

                } catch (Exception e) {
                    serverCommondInfo.setCode(RESULT_PARAM_ERROR);
                    serverCommondInfo.setMsg("Convert data to object error.");
                    logger.error("Convert data to object error.");
                    return serverCommondInfo;
                }
            } else {
                serverCommondInfo.setCode(RESULT_PARAM_ERROR);
                serverCommondInfo.setMsg("Error param.");
                logger.error("Error param.");
                return serverCommondInfo;
            }
        } else {
            serverCommondInfo.setCode(RESULT_INVALID_CMD_TYPE_ERROR);
            serverCommondInfo.setMsg("Invaild cmd type.");
            logger.error("Invaild cmd type.");
            return serverCommondInfo;
        }
    }


}
