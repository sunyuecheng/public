package com.sct.mailsecurityscanserver.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sct.mailsecurityscanserver.commond.ClientCommondInfo;
import com.sct.mailsecurityscanserver.commond.ServerCommondInfo;
import com.sct.mailsecurityscanserver.commond.customer.client.OperatorLoginCmdInfo;
import com.sct.mailsecurityscanserver.dao.IOperatorInfoDao;
import com.sct.mailsecurityscanserver.dao.ISessionInfoDao;
import com.sct.mailsecurityscanserver.entity.OperatorInfo;
import com.sct.mailsecurityscanserver.entity.SessionInfo;
import com.sct.mailsecurityscanserver.initialize.InitializeData;
import com.sct.mailsecurityscanserver.service.IComponentsStrDataService;
import com.sct.mailsecurityscanserver.util.EncodeMd5Util;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;
import java.util.Date;

import static com.sct.mailsecurityscanserver.define.CommonDefine.*;
import static com.sct.mailsecurityscanserver.define.ErrorCodeDefine.*;

@Service("OperatorLoginService")
public class OperatorLoginServiceImpl implements IComponentsStrDataService {

    private static final Logger logger = Logger.getLogger(OperatorLoginServiceImpl.class);
    
    @Autowired
    @Qualifier("OperatorInfoDao")
    private IOperatorInfoDao operatorInfoDao;

    @Resource
    private ShardedJedisPool shardedJedisPool;

    @Autowired
    @Qualifier("SessionInfoDao")
    private ISessionInfoDao sessionInfoDao;

    @Override
    public ServerCommondInfo execute(ClientCommondInfo clientCommondInfo, SessionInfo sessionInfo) {
        ServerCommondInfo serverCommondInfo = new ServerCommondInfo();

        if (clientCommondInfo == null || sessionInfo == null) {
            serverCommondInfo.setCode(RESULT_PARAM_ERROR);
            serverCommondInfo.setMsg("Error param!");
            logger.error("Error param!");
            return serverCommondInfo;
        }

        Integer type = clientCommondInfo.getType();
        if(type == null) {
            serverCommondInfo.setCode(RESULT_INVALID_CMD_TYPE_ERROR);
            serverCommondInfo.setMsg("Invaild cmd type!");
            logger.error("Invaild cmd type!");
            return serverCommondInfo;
        }
        serverCommondInfo.setCmd(clientCommondInfo.getCmd());
        serverCommondInfo.setType(clientCommondInfo.getType());

        if (type==0) {
            String data = clientCommondInfo.getData();
            if(data!=null && !data.isEmpty()) {
                try {
                    OperatorLoginCmdInfo operatorLoginCmdInfo =
                            JSONObject.parseObject(data, OperatorLoginCmdInfo.class);
                    operatorLogin(operatorLoginCmdInfo, sessionInfo, serverCommondInfo);
                    return serverCommondInfo;

                } catch (Exception e) {
                    serverCommondInfo.setCode(RESULT_PARAM_ERROR);
                    serverCommondInfo.setMsg("Convert data to object error!");
                    logger.error("Convert data to object error!");
                    return serverCommondInfo;
                }
            } else {
                serverCommondInfo.setCode(RESULT_PARAM_ERROR);
                serverCommondInfo.setMsg("Error param!");
                logger.error("Error param!");
                return serverCommondInfo;
            }
        } else if(type==1) {
            operatorLogout(sessionInfo, serverCommondInfo);
        } else {
            serverCommondInfo.setCode(RESULT_INVALID_CMD_TYPE_ERROR);
            serverCommondInfo.setMsg("Invaild cmd type!");
            logger.error("Invaild cmd type!");
            return serverCommondInfo;
        }
        return serverCommondInfo;
    }
    
    private void operatorLogin(OperatorLoginCmdInfo operatorLoginCmdInfo, SessionInfo sessionInfo,
                                     ServerCommondInfo serverCommondInfo) {
        OperatorInfo operatorInfo = operatorInfoDao.getOperatorInfo(operatorLoginCmdInfo.getAccount());

        if(operatorInfo == null) {
            serverCommondInfo.setCode(RESULT_SQL_ERROR);
            serverCommondInfo.setMsg("Query operator info error!");
            logger.error("Query operator info error,account("+ operatorLoginCmdInfo.getAccount()+")!");
            return;
        }

        if(operatorInfo.getWorkStatus()==0) {
            serverCommondInfo.setCode(RESULT_OPERATOR_DISABLED_ERROR);
            serverCommondInfo.setMsg("Operator is disabled!");
            logger.error("Operator is disabled,account("+ operatorLoginCmdInfo.getAccount()+")!");
            return;
        }

        String loginRandomNumStr = sessionInfo.getAttribute("LOGIN_RANDOM_NUM");
        String loginRandomNumCreateDateStr = sessionInfo.getAttribute("LOGIN_RANDOM_NUM_CREATE_DATE");

        if(loginRandomNumStr == null || loginRandomNumStr.isEmpty()
                || loginRandomNumCreateDateStr == null || loginRandomNumCreateDateStr.isEmpty()) {
            serverCommondInfo.setCode(RESULT_READ_SESSION_INFO_ERROR);
            serverCommondInfo.setMsg("Read login random num error!");
            logger.error("Read login random num error!");
            return;
        }

        Integer loginRandomNum = null;
        Long loginRandomNumCreateDate = null;
        String key = null;
        try {
            loginRandomNum = Integer.valueOf(loginRandomNumStr);
            loginRandomNumCreateDate = Long.valueOf(loginRandomNumCreateDateStr);

            Long intervalTime = (new Date()).getTime()-loginRandomNumCreateDate;
            if(intervalTime >= InitializeData.getLoginRandomNumTimeOutInterval()) {
                serverCommondInfo.setCode(RESULT_LOGIN_RANDOM_NUM_TIMEOUT_ERROR);
                serverCommondInfo.setMsg("Login random num is timeout!");
                logger.error("Login random num is timeout!");
                return;
            }
            key = EncodeMd5Util.makeMd5(operatorInfo.getPwd() + loginRandomNum);
            if(!key.equalsIgnoreCase(operatorLoginCmdInfo.getKey())) {
                serverCommondInfo.setCode(RESULT_LOGIN_KEY_ERROR);
                serverCommondInfo.setMsg("Login key is invaild!");
                logger.error("Login key is invaild!");
                return;
            }

            sessionInfo.setAttribute("ACCOUNT", operatorInfo.getAccount());
            sessionInfo.setAttribute("LOGIN_STATUS", "1");
            sessionInfo.setAttribute("LOGIN_DATE", String.valueOf((new Date()).getTime()));
            sessionInfo.setAttribute("USER_ROLE", USER_ROLE_OPERATOR.toString());
            if(sessionInfoDao.updateSessionInfo(shardedJedisPool.getResource(), sessionInfo)==false) {
                serverCommondInfo.setCode(RESULT_UPDATE_SESSION_INFO_ERROR);
                serverCommondInfo.setMsg("Update session info error!");
                logger.error("Update session info error!");
                return;
            }

            serverCommondInfo.setCode(RESULT_OK);
            serverCommondInfo.setMsg("");
            return;

        } catch (Exception e) {
            serverCommondInfo.setCode(RESULT_CONVERT_DATA_ERROR);
            serverCommondInfo.setMsg("Convert operator login key error!");
            logger.error("Convert operator login key error!");
            return;
        }
    }

    private void operatorLogout(SessionInfo sessionInfo, ServerCommondInfo serverCommondInfo) {

        sessionInfo.setAttribute("ACCOUNT", "");
        sessionInfo.setAttribute("LOGIN_RANDOM_NUM", "0");
        sessionInfo.setAttribute("LOGIN_RANDOM_NUM_CREATE_DATE", "0");
        sessionInfo.setAttribute("LOGIN_DATE", "0");
        sessionInfo.setAttribute("LOGIN_STATUS", "0");
        if(sessionInfoDao.updateSessionInfo(shardedJedisPool.getResource(), sessionInfo)==false) {
            serverCommondInfo.setCode(RESULT_UPDATE_SESSION_INFO_ERROR);
            serverCommondInfo.setMsg("Update session info error!");
            logger.error("Update session info error!");
            return;
        }

        serverCommondInfo.setCode(RESULT_OK);
        serverCommondInfo.setMsg("");
        return;
    }


}
