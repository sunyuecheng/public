package com.sct.mailsecurityscanserver.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sct.mailsecurityscanserver.commond.ClientCommondInfo;
import com.sct.mailsecurityscanserver.commond.ServerCommondInfo;
import com.sct.mailsecurityscanserver.commond.customer.client.CheckTerminalVersionCmdInfo;
import com.sct.mailsecurityscanserver.commond.customer.server.CheckTerminalVersionRetCmdInfo;
import com.sct.mailsecurityscanserver.dao.ISessionInfoDao;
import com.sct.mailsecurityscanserver.dao.ITerminalInfoDao;
import com.sct.mailsecurityscanserver.dao.ITerminalVersionInfoDao;
import com.sct.mailsecurityscanserver.entity.SessionInfo;
import com.sct.mailsecurityscanserver.entity.TerminalInfo;
import com.sct.mailsecurityscanserver.entity.TerminalVersionInfo;
import com.sct.mailsecurityscanserver.service.IComponentsStrDataService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;
import java.util.Date;

import static com.sct.mailsecurityscanserver.define.ErrorCodeDefine.*;

@Service("TerminalVersionService")
public class TerminalVersionServiceImpl implements IComponentsStrDataService {

    private static final Logger logger = Logger.getLogger(TerminalVersionServiceImpl.class);
    
    @Autowired
    @Qualifier("TerminalVersionInfoDao")
    private ITerminalVersionInfoDao terminalVersionInfoDao;

    @Autowired
    @Qualifier("TerminalInfoDao")
    private ITerminalInfoDao terminalInfoDao;

    @Resource
    private ShardedJedisPool shardedJedisPool;

    @Autowired
    @Qualifier("SessionInfoDao")
    private ISessionInfoDao sessionInfoDao;

    @Override
    public ServerCommondInfo execute(ClientCommondInfo clientCommondInfo, SessionInfo sessionInfo) {
        ServerCommondInfo serverCommondInfo = new ServerCommondInfo();

        if (clientCommondInfo == null) {
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
                    CheckTerminalVersionCmdInfo checkTerminalVersionCmdInfo =
                            JSONObject.parseObject(data, CheckTerminalVersionCmdInfo.class);
                    checkTerminalVersionInfo(checkTerminalVersionCmdInfo, sessionInfo, serverCommondInfo);
                    return serverCommondInfo;

                } catch (Exception e) {
                    serverCommondInfo.setCode(RESULT_PARAM_ERROR);
                    serverCommondInfo.setMsg("Convert data to object error!");
                    logger.error("Convert data to object error!");
                    return serverCommondInfo;
                }
            }  else {
                serverCommondInfo.setCode(RESULT_PARAM_ERROR);
                serverCommondInfo.setMsg("Error param!");
                logger.error("Error param!");
                return serverCommondInfo;
            }
        } else {
            serverCommondInfo.setCode(RESULT_INVALID_CMD_TYPE_ERROR);
            serverCommondInfo.setMsg("Invaild cmd type!");
            logger.error("Invaild cmd type!");
            return serverCommondInfo;
        }
    }
    
    private void checkTerminalVersionInfo(CheckTerminalVersionCmdInfo checkTerminalVersionCmdInfo, SessionInfo sessionInfo,
                                     ServerCommondInfo serverCommondInfo) {
        Integer status = terminalInfoDao.checkTerminalStatus(checkTerminalVersionCmdInfo.getTerminalId());

        if(status == 0) {
            serverCommondInfo.setCode(RESULT_TERMINAL_DISABLED_ERROR);
            serverCommondInfo.setMsg("Terminal is disabled!");
            logger.error("Terminal is disabled,terminal id("+ checkTerminalVersionCmdInfo.getTerminalId()+")," +
                    "cpu serial num("+ checkTerminalVersionCmdInfo.getCpuSerialNum() +")," +
                    "hard disk serial num("+checkTerminalVersionCmdInfo.getHardDiskSerialNum()+")!");
            return;
        }

        if(status == -2) {
            serverCommondInfo.setCode(RESULT_TERMINAL_NOT_EXIST_ERROR);
            serverCommondInfo.setMsg("There is no terminal info!");
            logger.error("There is no terminal info,terminal id("+ checkTerminalVersionCmdInfo.getTerminalId()+")," +
                    "cpu serial num("+ checkTerminalVersionCmdInfo.getCpuSerialNum() +")," +
                    "hard disk serial num("+checkTerminalVersionCmdInfo.getHardDiskSerialNum()+")!");
            return;
        }

        if(status == -3) {
            serverCommondInfo.setCode(RESULT_SQL_ERROR);
            serverCommondInfo.setMsg("Query terminal info error!");
            logger.error("Query terminal info error,terminal id("+ checkTerminalVersionCmdInfo.getTerminalId()+")," +
                    "cpu serial num("+ checkTerminalVersionCmdInfo.getCpuSerialNum() +")," +
                    "hard disk serial num("+checkTerminalVersionCmdInfo.getHardDiskSerialNum()+")!");
            return;
        }

        Integer versionStatus = terminalVersionInfoDao.checkTerminalVersionInfo(checkTerminalVersionCmdInfo.getSystemType(),
                checkTerminalVersionCmdInfo.getSystemVersion(),checkTerminalVersionCmdInfo.getTerminalVersion());

        if(versionStatus == null) {
            serverCommondInfo.setCode(RESULT_SQL_ERROR);
            serverCommondInfo.setMsg("Query terminal version info error!");
            logger.error("Query terminal version info error,system type("+ checkTerminalVersionCmdInfo.getSystemType()+")," +
                    "system version("+ checkTerminalVersionCmdInfo.getSystemVersion() +")," +
                    "terminal version("+checkTerminalVersionCmdInfo.getTerminalVersion()+")!");
            return;
        }

        CheckTerminalVersionRetCmdInfo checkTerminalVersionRetCmdInfo = new CheckTerminalVersionRetCmdInfo();
        if(versionStatus.equals(TerminalVersionInfo.TERMINAL_VERSION_NEWEST)) {
            checkTerminalVersionRetCmdInfo.setVersionStatus(versionStatus);
        } else {
            TerminalVersionInfo retTerminalVersionInfo = terminalVersionInfoDao.getUploadTerminalVersionInfo(checkTerminalVersionCmdInfo.getSystemType(),
                    checkTerminalVersionCmdInfo.getSystemVersion(),checkTerminalVersionCmdInfo.getTerminalVersion());

            if(retTerminalVersionInfo==null) {
                serverCommondInfo.setCode(RESULT_INVALID_TERMINAL_VERSION_ERROR);
                serverCommondInfo.setMsg("There is no upload terminal version info!");
                logger.error("There is no upload terminal version info,system type("+ checkTerminalVersionCmdInfo.getSystemType()+")," +
                        "system version("+ checkTerminalVersionCmdInfo.getSystemVersion() +")," +
                        "terminal version("+checkTerminalVersionCmdInfo.getTerminalVersion()+")!");
                return;
            }

            checkTerminalVersionRetCmdInfo.setCurrentVersion(retTerminalVersionInfo.getTerminalVersion());
            checkTerminalVersionRetCmdInfo.setVersionStatus(versionStatus);
            checkTerminalVersionRetCmdInfo.setPacketSize(retTerminalVersionInfo.getPacketSize());
            checkTerminalVersionRetCmdInfo.setPacketMd5(retTerminalVersionInfo.getPacketMd5());
            checkTerminalVersionRetCmdInfo.setDownloadUrl(retTerminalVersionInfo.getDownloadUrl());
        }

        if(!terminalInfoDao.updateTerminalVersion(checkTerminalVersionCmdInfo.getTerminalId(), checkTerminalVersionCmdInfo.getTerminalVersion())) {
            serverCommondInfo.setCode(RESULT_SQL_ERROR);
            serverCommondInfo.setMsg("Update terminal version info error!");
            logger.error("Update terminal version info error!");
            return;
        }

        TerminalInfo terminalInfo = terminalInfoDao.getTerminalInfoById(checkTerminalVersionCmdInfo.getTerminalId());
        sessionInfo= sessionInfoDao.createSessionInfo(shardedJedisPool.getResource(), terminalInfo);
        if(sessionInfo == null) {
            serverCommondInfo.setCode(RESULT_CREATE_SESSION_INFO_ERROR);
            serverCommondInfo.setMsg("Create session info error!");
            logger.error("Create session info error!");
            return;
        }

        if(versionStatus.equals(TerminalVersionInfo.TERMINAL_VERSION_NEWEST) ||
                        versionStatus.equals(TerminalVersionInfo.TERMINAL_VERSION_HAVE_NEW_NO_NEED_UPDATE)) {
            java.util.Random random = new java.util.Random();
            Integer randomNum = random.nextInt();
            sessionInfo.setAttribute("LOGIN_RANDOM_NUM", randomNum.toString());
            sessionInfo.setAttribute("LOGIN_RANDOM_NUM_CREATE_DATE", String.valueOf((new Date()).getTime()));

            if(sessionInfoDao.updateSessionInfo(shardedJedisPool.getResource(), sessionInfo)==false) {
                serverCommondInfo.setCode(RESULT_UPDATE_SESSION_INFO_ERROR);
                serverCommondInfo.setMsg("Update session info error!");
                logger.error("Update session info error!");
                return;
            }
            checkTerminalVersionRetCmdInfo.setRandomNum(randomNum);
            checkTerminalVersionRetCmdInfo.setSessionId(sessionInfo.getSessionId());
        }

        serverCommondInfo.setCode(RESULT_OK);
        serverCommondInfo.setMsg("");
        serverCommondInfo.setData(JSON.toJSONString(checkTerminalVersionRetCmdInfo));
        return;
    }    
    

}
