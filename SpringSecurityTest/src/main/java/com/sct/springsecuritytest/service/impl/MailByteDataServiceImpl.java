package com.sct.mailsecurityscanserver.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sct.mailsecurityscanserver.commond.ClientCommondInfo;
import com.sct.mailsecurityscanserver.commond.customer.client.DownloadMailInfoListCmdInfo;
import com.sct.mailsecurityscanserver.dao.*;
import com.sct.mailsecurityscanserver.entity.MailDataDownloadRecordInfo;
import com.sct.mailsecurityscanserver.entity.SessionInfo;
import com.sct.mailsecurityscanserver.entity.TerminalInfo;
import com.sct.mailsecurityscanserver.initialize.InitializeData;
import com.sct.mailsecurityscanserver.service.IComponentsByteDataService;
import com.sct.mailsecurityscanserver.util.EncodeMd5Util;
import com.sct.mailsecurityscanserver.util.EncryptDesUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;
import java.util.Date;

import static com.sct.mailsecurityscanserver.define.CommonDefine.USER_ROLE_OPERATOR;
import static com.sct.mailsecurityscanserver.util.CommonFunction.intToByteArray;

@Service("MailByteDataService")
public class MailByteDataServiceImpl implements IComponentsByteDataService {

    private static final Logger logger = Logger.getLogger(MailByteDataServiceImpl.class);

    private static final Integer DEVICE_ID_LEN = 32;
    private static final Integer CPU_SERIAL_NUM_LEN = 32;
    private static final Integer HARD_DISK_SERIAL_NUM_LEN = 32;
    private static final Integer SYSTEM_TYPE_LEN = 4;
    private static final Integer SYSTEM_VERSION_LEN = 32;
    private static final Integer APP_VERSION_LEN = 32;
    private static final Integer CREATE_TIMESTAMP_LEN = 32;

    private static final Integer DATA_HEAD_LEN = DEVICE_ID_LEN + CPU_SERIAL_NUM_LEN + HARD_DISK_SERIAL_NUM_LEN
            + SYSTEM_TYPE_LEN + SYSTEM_VERSION_LEN + APP_VERSION_LEN + CREATE_TIMESTAMP_LEN;

    private static final Integer MAIL_NUM_LEN = 4;
    private static final Integer MAIL_ID_LEN = 32;
    private static final Integer SUBJECT_LEN = 4;
    private static final Integer BODY_LEN = 4;
    private static final Integer ATTACHMENT_NAME_LEN = 4;
    private static final Integer ATTACHMENT_MD5_LEN = 32;
    private static final Integer ATTACHMENT_LEN = 4;

    @Autowired
    @Qualifier("MailInfoDao")
    private IMailInfoDao mailInfoDao;

    @Resource
    private ShardedJedisPool shardedJedisPool;

    @Autowired
    @Qualifier("SessionInfoDao")
    private ISessionInfoDao sessionInfoDao;

    @Autowired
    @Qualifier("MailInfoListDao")
    private IMailInfoListDao mailInfoListDao;

    @Autowired
    @Qualifier("TerminalVersionInfoDao")
    private ITerminalVersionInfoDao terminalVersionInfoDao;

    @Autowired
    @Qualifier("TerminalInfoDao")
    private ITerminalInfoDao terminalInfoDao;

    @Autowired
    @Qualifier("MailDataDownloadRecordInfoDao")
    private IMailDataDownloadRecordInfoDao mailDataDownloadRecordInfoDao;


    @Override
    public byte[] execute(ClientCommondInfo clientCommondInfo, SessionInfo sessionInfo) {

        String errorInfo;

        if (clientCommondInfo == null || sessionInfo == null) {
            errorInfo = "Error param!";
            logger.error(errorInfo);
            return errorInfo.getBytes();
        }

        Integer type = clientCommondInfo.getType();
        if(type == null) {
            errorInfo = "Invaild cmd type!";
            logger.error(errorInfo);
            return errorInfo.getBytes();
        }

        byte[] retData = null;
        if (type==0) {
            String data = clientCommondInfo.getData();
            if(data!=null && !data.isEmpty()) {
                try {
                    DownloadMailInfoListCmdInfo downloadMailInfoListCmdInfo =
                            JSONObject.parseObject(data, DownloadMailInfoListCmdInfo.class);
                    retData = downloadMailInfoList(downloadMailInfoListCmdInfo, sessionInfo);
                    return retData;

                } catch (Exception e) {
                    errorInfo = "Convert data to object error!";
                    logger.error(errorInfo);
                    return errorInfo.getBytes();
                }
            } else {
                errorInfo = "Error param!";
                logger.error(errorInfo);
                return errorInfo.getBytes();
            }
        } else  {
            errorInfo = "Invaild cmd type!";
            logger.error(errorInfo);
            return errorInfo.getBytes();
        }
    }

    private byte[] downloadMailInfoList(DownloadMailInfoListCmdInfo downloadMailInfoListCmdInfo, SessionInfo sessionInfo) {

        String errorInfo;

        String account = sessionInfo.getAttribute("ACCOUNT");
        String loginStatus = sessionInfo.getAttribute("LOGIN_STATUS");
        String loginDateStr = sessionInfo.getAttribute("LOGIN_DATE");

        if(account== null || account.isEmpty()
                ||loginStatus == null || loginStatus.isEmpty()
                ||loginDateStr == null || loginDateStr.isEmpty()) {
            errorInfo = "Invaild login info!";
            logger.error(errorInfo);
            return errorInfo.getBytes();
        }

        Long loginDate = Long.parseLong(loginDateStr);
        if(loginStatus.equals("0") || (new Date()).getTime()-loginDate >= InitializeData.getLoginTimeOutInterval()) {
            errorInfo ="User login timeout login info!";
            logger.error(errorInfo);
            return errorInfo.getBytes();
        }

        String userRole = sessionInfo.getAttribute("USER_ROLE");
        if(userRole== null) {
            errorInfo = "User has no permission to register manager!";
            logger.error(errorInfo);
            return errorInfo.getBytes();
        }

        byte[] mailInfoListData=mailInfoListDao.getMailInfoListData(shardedJedisPool.getResource());
        if(mailInfoListData == null || mailInfoListData.length<DATA_HEAD_LEN) {
            errorInfo = "Invaild mail info list data!";
            logger.error(errorInfo);
            return errorInfo.getBytes();
        }

        Integer status = terminalInfoDao.checkTerminalStatus(downloadMailInfoListCmdInfo.getTerminalId());

        if(status == 0) {
            errorInfo ="Terminal is disabled!";
            logger.error("Terminal is disabled,terminal id("+ downloadMailInfoListCmdInfo.getTerminalId()+")," +
                    "cpu serial num("+ downloadMailInfoListCmdInfo.getCpuSerialNum() +")," +
                    "hard disk serial num("+ downloadMailInfoListCmdInfo.getHardDiskSerialNum()+")!");
            return errorInfo.getBytes();
        }

        if(status == -2) {
            errorInfo = "There is no terminal info!";
            logger.error("There is no terminal info,terminal id("+ downloadMailInfoListCmdInfo.getTerminalId()+")," +
                    "cpu serial num("+ downloadMailInfoListCmdInfo.getCpuSerialNum() +")," +
                    "hard disk serial num("+ downloadMailInfoListCmdInfo.getHardDiskSerialNum()+")!");
            return errorInfo.getBytes();
        }

        if(status == -3) {
            errorInfo = "Query terminal info error!";
            logger.error("Query terminal info error,terminal id("+ downloadMailInfoListCmdInfo.getTerminalId()+")," +
                    "cpu serial num("+ downloadMailInfoListCmdInfo.getCpuSerialNum() +")," +
                    "hard disk serial num("+ downloadMailInfoListCmdInfo.getHardDiskSerialNum()+")!");
            return errorInfo.getBytes();
        }

        Integer versionStatus = terminalVersionInfoDao.checkTerminalVersionInfo(downloadMailInfoListCmdInfo.getSystemType(),
                downloadMailInfoListCmdInfo.getSystemVersion(), downloadMailInfoListCmdInfo.getTerminalVersion());

        if(versionStatus == null) {
            errorInfo = "Query terminal version info error!";
            logger.error("Query terminal version info error,system type("+ downloadMailInfoListCmdInfo.getSystemType()+")," +
                    "system version("+ downloadMailInfoListCmdInfo.getSystemVersion() +")," +
                    "terminal version("+ downloadMailInfoListCmdInfo.getTerminalVersion()+")!");
            return errorInfo.getBytes();
        }

        if(userRole.equals(USER_ROLE_OPERATOR.toString())) {
            String terminalInfoStr = sessionInfo.getAttribute("TERMINAL_INFO");
            if(terminalInfoStr == null || terminalInfoStr.isEmpty()) {
                errorInfo = "Invaild session terminal info!";
                logger.error(errorInfo);
                return errorInfo.getBytes();
            }
            try {
                TerminalInfo terminalInfo = JSON.parseObject(terminalInfoStr, TerminalInfo.class);
                if(terminalInfo == null || !terminalInfo.getId().equals(downloadMailInfoListCmdInfo.getTerminalId())) {
                    errorInfo = "Invaild session terminal info!";
                    logger.error(errorInfo);
                    return errorInfo.getBytes();
                }
            } catch (Exception e) {
                errorInfo = "Invaild session terminal info!";
                logger.error("Convert session terminal info error, error info("+e.getMessage()+")!");
                return errorInfo.getBytes();
            }
        } else {
            errorInfo = "Invaild session operator info!";
            logger.error(errorInfo);
            return errorInfo.getBytes();
        }

        int pos= 0;
        System.arraycopy(downloadMailInfoListCmdInfo.getTerminalId().getBytes(),0, mailInfoListData, pos,
                downloadMailInfoListCmdInfo.getTerminalId().length());
        pos += DEVICE_ID_LEN;
        System.arraycopy(downloadMailInfoListCmdInfo.getCpuSerialNum().getBytes(),0, mailInfoListData, pos,
                downloadMailInfoListCmdInfo.getCpuSerialNum().length());
        pos += CPU_SERIAL_NUM_LEN;
        System.arraycopy(downloadMailInfoListCmdInfo.getHardDiskSerialNum().getBytes(),0, mailInfoListData, pos,
                downloadMailInfoListCmdInfo.getHardDiskSerialNum().length());
        pos += HARD_DISK_SERIAL_NUM_LEN;

        int systemType = downloadMailInfoListCmdInfo.getSystemType();
        byte[] systemTypeByte = intToByteArray(systemType);
        System.arraycopy(systemTypeByte,0, mailInfoListData, pos, SYSTEM_TYPE_LEN);
        pos += SYSTEM_TYPE_LEN;

        System.arraycopy(downloadMailInfoListCmdInfo.getSystemVersion().getBytes(),0, mailInfoListData, pos,
                downloadMailInfoListCmdInfo.getSystemVersion().length());
        pos += SYSTEM_VERSION_LEN;

        System.arraycopy(downloadMailInfoListCmdInfo.getTerminalVersion().getBytes(),0, mailInfoListData, pos,
                downloadMailInfoListCmdInfo.getTerminalVersion().length());
        pos += APP_VERSION_LEN;

        Long createDate = (new Date()).getTime();
        String createTimestamp = createDate.toString();
        System.arraycopy(createTimestamp.getBytes(),0, mailInfoListData, pos,
                createTimestamp.length());

        byte[] encryptMailInfoListData = null;
        try {
            String key = EncodeMd5Util.makeMd5(account + downloadMailInfoListCmdInfo.getTerminalId());
            encryptMailInfoListData = EncryptDesUtil.encryptData(key.substring(0,24).getBytes(),mailInfoListData);
        } catch (Exception e) {
            errorInfo = "Encrypt mail info list data error!";
            logger.error("Encrypt mail info list data error, error info(" + e.getMessage() + ")!");
            return errorInfo.getBytes();
        }

        MailDataDownloadRecordInfo mailDataDownloadRecordInfo = new MailDataDownloadRecordInfo(account,
                downloadMailInfoListCmdInfo.getTerminalId(),MailDataDownloadRecordInfo.RECORD_TYPE_ONLINE,"",  new Date());

        if(!mailDataDownloadRecordInfoDao.insertMailDataDownloadRecordInfo(mailDataDownloadRecordInfo)) {
            errorInfo = "Record mail data download record info error!";
            logger.error(errorInfo);
            return errorInfo.getBytes();
        }

        return encryptMailInfoListData;
    }

}
