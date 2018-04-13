package com.sct.mailsecurityscanserver.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sct.mailsecurityscanserver.commond.ClientCommondInfo;
import com.sct.mailsecurityscanserver.commond.ServerCommondInfo;
import com.sct.mailsecurityscanserver.commond.customer.client.UploadMailServerScanResultCmdInfo;
import com.sct.mailsecurityscanserver.commond.manager.client.*;
import com.sct.mailsecurityscanserver.dao.*;
import com.sct.mailsecurityscanserver.entity.*;
import com.sct.mailsecurityscanserver.initialize.InitializeData;
import com.sct.mailsecurityscanserver.service.IComponentsStrDataService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.sct.mailsecurityscanserver.define.CommonDefine.USER_ROLE_ADMIN;
import static com.sct.mailsecurityscanserver.define.CommonDefine.USER_ROLE_OPERATOR;
import static com.sct.mailsecurityscanserver.define.ErrorCodeDefine.*;
import static com.sct.mailsecurityscanserver.entity.MailInfo.MAIL_TYPE_ATTACHMENT;
import static com.sct.mailsecurityscanserver.entity.MailInfo.MAIL_TYPE_COUNT;
import static com.sct.mailsecurityscanserver.entity.MailInfo.MAIL_TYPE_XSS;
import static com.sct.mailsecurityscanserver.util.CommonFunction.intToByteArray;

@Service("MailStrDataService")
public class MailStrDataServiceImpl implements IComponentsStrDataService {

    private static final Logger logger = Logger.getLogger(MailStrDataServiceImpl.class);

    private static final Integer DEVICE_ID_LEN = 32;
    private static final Integer CPU_SERIAL_NUM_LEN = 32;
    private static final Integer HARD_DISK_SERIAL_NUM_LEN = 32;
    private static final Integer SYSTEM_TYPE_LEN = 4;
    private static final Integer SYSTEM_VERSION_LEN = 32;
    private static final Integer APP_VERSION_LEN = 32;

    private static final Integer DATA_HEAD_LEN = DEVICE_ID_LEN + CPU_SERIAL_NUM_LEN + HARD_DISK_SERIAL_NUM_LEN
            + SYSTEM_TYPE_LEN + SYSTEM_VERSION_LEN + APP_VERSION_LEN;

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

    @Autowired
    @Qualifier("MailBugInfoDao")
    private IMailBugInfoDao mailBugInfoDao;

    @Autowired
    @Qualifier("MailServerBugInfoDao")
    private IMailServerBugInfoDao mailServerBugInfoDao;

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
    @Qualifier("ScanTaskInfoDao")
    private IScanTaskInfoDao scanTaskInfoDao;

    @Autowired
    @Qualifier("ScanResultInfoDao")
    private IScanResultInfoDao scanResultInfoDao;

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
                    UploadMailServerScanResultCmdInfo uploadMailServerScanResultCmdInfo =
                            JSONObject.parseObject(data, UploadMailServerScanResultCmdInfo.class);
                    uploadMailServerScanResult(uploadMailServerScanResultCmdInfo, sessionInfo, serverCommondInfo);
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
        } else  {
            serverCommondInfo.setCode(RESULT_INVALID_CMD_TYPE_ERROR);
            serverCommondInfo.setMsg("Invaild cmd type!");
            logger.error("Invaild cmd type!");
            return serverCommondInfo;
        }
    }

    private void uploadMailServerScanResult(UploadMailServerScanResultCmdInfo uploadMailServerScanResultCmdInfo, SessionInfo sessionInfo,
                                            ServerCommondInfo serverCommondInfo) {

        String account = sessionInfo.getAttribute("ACCOUNT");
        String loginStatus = sessionInfo.getAttribute("LOGIN_STATUS");
        String loginDateStr = sessionInfo.getAttribute("LOGIN_DATE");

        if (account == null || account.isEmpty()
                || loginStatus == null || loginStatus.isEmpty()
                || loginDateStr == null || loginDateStr.isEmpty()) {
            serverCommondInfo.setCode(RESULT_INVAILD_LOGIN_INFO_ERROR);
            serverCommondInfo.setMsg("Invaild login info!");
            logger.error("Invaild login info!");
            return;
        }

        Long loginDate = Long.parseLong(loginDateStr);
        if (loginStatus.equals("0") || (new Date()).getTime() - loginDate >= InitializeData.getLoginTimeOutInterval()) {
            serverCommondInfo.setCode(RESULT_LOGIN_TIMEOUT_ERROR);
            serverCommondInfo.setMsg("User login timeout login info!");
            logger.error("User login timeout login info!");
            return;
        }

        String userRole = sessionInfo.getAttribute("USER_ROLE");
        if (userRole == null) {
            serverCommondInfo.setCode(RESULT_USER_PERMISSION_ERROR);
            serverCommondInfo.setMsg("User has no permission to enable operator!");
            logger.error("User has no permission to enable operator!");
            return;
        }

        Integer status = terminalInfoDao.checkTerminalStatus(uploadMailServerScanResultCmdInfo.getTerminalId());

        if (status == 0) {
            serverCommondInfo.setCode(RESULT_TERMINAL_DISABLED_ERROR);
            serverCommondInfo.setMsg("Terminal is disabled!");
            logger.error("Terminal is disabled,terminal id(" + uploadMailServerScanResultCmdInfo.getTerminalId() + ")," +
                    "cpu serial num(" + uploadMailServerScanResultCmdInfo.getCpuSerialNum() + ")," +
                    "hard disk serial num(" + uploadMailServerScanResultCmdInfo.getHardDiskSerialNum() + ")!");
            return;
        }

        if (status == -2) {
            serverCommondInfo.setCode(RESULT_TERMINAL_NOT_EXIST_ERROR);
            serverCommondInfo.setMsg("There is no terminal info!");
            logger.error("There is no terminal info,terminal id(" + uploadMailServerScanResultCmdInfo.getTerminalId() + ")," +
                    "cpu serial num(" + uploadMailServerScanResultCmdInfo.getCpuSerialNum() + ")," +
                    "hard disk serial num(" + uploadMailServerScanResultCmdInfo.getHardDiskSerialNum() + ")!");
            return;
        }

        if (status == -3) {
            serverCommondInfo.setCode(RESULT_SQL_ERROR);
            serverCommondInfo.setMsg("Query terminal info error!");
            logger.error("Query terminal info error,terminal id(" + uploadMailServerScanResultCmdInfo.getTerminalId() + ")," +
                    "cpu serial num(" + uploadMailServerScanResultCmdInfo.getCpuSerialNum() + ")," +
                    "hard disk serial num(" + uploadMailServerScanResultCmdInfo.getHardDiskSerialNum() + ")!");
            return;
        }

        Integer versionStatus = terminalVersionInfoDao.checkTerminalVersionInfo(uploadMailServerScanResultCmdInfo.getSystemType(),
                uploadMailServerScanResultCmdInfo.getSystemVersion(), uploadMailServerScanResultCmdInfo.getTerminalVersion());

        if (versionStatus == null) {
            serverCommondInfo.setCode(RESULT_SQL_ERROR);
            serverCommondInfo.setMsg("Query terminal version info error!");
            logger.error("Query terminal version info error,system type(" + uploadMailServerScanResultCmdInfo.getSystemType() + ")," +
                    "system version(" + uploadMailServerScanResultCmdInfo.getSystemVersion() + ")," +
                    "terminal version(" + uploadMailServerScanResultCmdInfo.getTerminalVersion() + ")!");
            return;
        }

        if (userRole == USER_ROLE_OPERATOR.toString()) {
            String terminalInfoStr = sessionInfo.getAttribute("TERMINAL_INFO");
            if (terminalInfoStr == null || terminalInfoStr.isEmpty()) {
                serverCommondInfo.setCode(RESULT_READ_SESSION_INFO_ERROR);
                serverCommondInfo.setMsg("Invaild session terminal info!");
                logger.error("Invaild session terminal info,system type(" + uploadMailServerScanResultCmdInfo.getSystemType() + ")," +
                        "system version(" + uploadMailServerScanResultCmdInfo.getSystemVersion() + ")," +
                        "terminal version(" + uploadMailServerScanResultCmdInfo.getTerminalVersion() + ")!");
                return;
            }
            try {
                TerminalInfo terminalInfo = JSON.parseObject(terminalInfoStr, TerminalInfo.class);
                if (terminalInfo == null || !terminalInfo.getId().equals(uploadMailServerScanResultCmdInfo.getTerminalId())) {
                    serverCommondInfo.setCode(RESULT_READ_SESSION_INFO_ERROR);
                    serverCommondInfo.setMsg("Invaild session terminal info!");
                    logger.error("Invaild session terminal info,system type(" + uploadMailServerScanResultCmdInfo.getSystemType() + ")," +
                            "system version(" + uploadMailServerScanResultCmdInfo.getSystemVersion() + ")," +
                            "terminal version(" + uploadMailServerScanResultCmdInfo.getTerminalVersion() + ")!");
                    return;
                }
            } catch (Exception e) {
                serverCommondInfo.setCode(RESULT_READ_SESSION_INFO_ERROR);
                serverCommondInfo.setMsg("Convert session terminal info error!");
                logger.error("Convert session terminal info error,system type(" + uploadMailServerScanResultCmdInfo.getSystemType() + ")," +
                        "system version(" + uploadMailServerScanResultCmdInfo.getSystemVersion() + ")," +
                        "terminal version(" + uploadMailServerScanResultCmdInfo.getTerminalVersion() + ")!");
                return;
            }
        }

        ScanTaskInfo scanTaskInfo = new ScanTaskInfo();
        scanTaskInfo.setId(uploadMailServerScanResultCmdInfo.getTaskId());
        scanTaskInfo.setOperatorAccount(uploadMailServerScanResultCmdInfo.getAccount());
        scanTaskInfo.setOrganizationName(uploadMailServerScanResultCmdInfo.getOrganizationName());
        scanTaskInfo.setDepartmentName(uploadMailServerScanResultCmdInfo.getDepartmentName());
        scanTaskInfo.setScanSystemName(uploadMailServerScanResultCmdInfo.getScanSystemName());
        scanTaskInfo.setScanDate(new Date(uploadMailServerScanResultCmdInfo.getScanDate()));
        scanTaskInfo.setScanType(uploadMailServerScanResultCmdInfo.getScanType());
        scanTaskInfo.setMailWebServerAddress(uploadMailServerScanResultCmdInfo.getMailWebServerAddress());
        scanTaskInfo.setMailServerAddress(uploadMailServerScanResultCmdInfo.getMailServerAddress());
        scanTaskInfo.setImapPort(uploadMailServerScanResultCmdInfo.getImapPort());
        scanTaskInfo.setEncryptStatus(uploadMailServerScanResultCmdInfo.getEncryptStatus());
        scanTaskInfo.setPop3Port(uploadMailServerScanResultCmdInfo.getPop3Port());
        scanTaskInfo.setSmtpPort(uploadMailServerScanResultCmdInfo.getSmtpPort());
        scanTaskInfo.setMailSenderAddress(uploadMailServerScanResultCmdInfo.getMailSenderAddress());
        scanTaskInfo.setMailReceiverAddress(uploadMailServerScanResultCmdInfo.getMailReceiverAddress());
        scanTaskInfo.setTerminalId(uploadMailServerScanResultCmdInfo.getTerminalId());
        scanTaskInfo.setCpuSerialNum(uploadMailServerScanResultCmdInfo.getCpuSerialNum());
        scanTaskInfo.setHardDiskSerialNum(uploadMailServerScanResultCmdInfo.getHardDiskSerialNum());
        scanTaskInfo.setSystemType(uploadMailServerScanResultCmdInfo.getSystemType());
        scanTaskInfo.setSystemVersion(uploadMailServerScanResultCmdInfo.getSystemVersion());
        scanTaskInfo.setTerminalVersion(uploadMailServerScanResultCmdInfo.getTerminalVersion());
        scanTaskInfo.setTaskStatus(0);

        List<ScanResultInfo> scanResultInfoList = new ArrayList<ScanResultInfo>();
        ScanResultInfo scanResultInfo = new ScanResultInfo();
        scanResultInfo.setTaskId(uploadMailServerScanResultCmdInfo.getTaskId());
        scanResultInfo.setScanStep(0);
        scanResultInfo.setScanParam(uploadMailServerScanResultCmdInfo.getStep1ScanParam());

        if (uploadMailServerScanResultCmdInfo.getStep1MailServerName() != null
                && uploadMailServerScanResultCmdInfo.getStep1MailServerName().isEmpty()
                && uploadMailServerScanResultCmdInfo.getStep1MailServerVersion() != null
                && uploadMailServerScanResultCmdInfo.getStep1MailServerVersion().isEmpty()) {

            MailServerBugInfo mailServerBugInfo =
                    mailServerBugInfoDao.getMailServerBugInfo(uploadMailServerScanResultCmdInfo.getStep1MailServerName(),
                            uploadMailServerScanResultCmdInfo.getStep1MailServerVersion());

            if (mailServerBugInfo != null) {
                //scanResultInfo.setResultDetail(mailServerBugInfo.getId());
                scanResultInfo.setScanMailServerName(uploadMailServerScanResultCmdInfo.getStep1MailServerName());
                scanResultInfo.setScanMailServerVersion(uploadMailServerScanResultCmdInfo.getStep1MailServerVersion());
            }
        }
        scanResultInfoList.add(scanResultInfo);

        for(int i=0;i<uploadMailServerScanResultCmdInfo.getStep2ReceiveMailIdList().size();i++) {
            scanResultInfo = new ScanResultInfo();
            scanResultInfo.setTaskId(uploadMailServerScanResultCmdInfo.getTaskId());
            scanResultInfo.setScanStep(1);

            MailBugInfo mailBugInfo =
                    mailBugInfoDao.getMailBugInfoByMailId(uploadMailServerScanResultCmdInfo.getStep2ReceiveMailIdList().get(i));

            if(mailBugInfo == null) {
                serverCommondInfo.setCode(RESULT_SQL_ERROR);
                serverCommondInfo.setMsg("Query mail bug info error!");
                logger.error("Query mail bug info error,mail id("+
                        uploadMailServerScanResultCmdInfo.getStep2ReceiveMailIdList().get(i)+")!");
                return;
            }
            scanResultInfo.setMailId(uploadMailServerScanResultCmdInfo.getStep2ReceiveMailIdList().get(i));
            //scanResultInfo.setResultDetail(mailBugInfo.getId());
            scanResultInfoList.add(scanResultInfo);
        }

        for(int i=0;i<uploadMailServerScanResultCmdInfo.getStep3ReceiveMailIdList().size();i++) {
            scanResultInfo = new ScanResultInfo();
            scanResultInfo.setTaskId(uploadMailServerScanResultCmdInfo.getTaskId());
            scanResultInfo.setScanStep(2);

            MailBugInfo mailBugInfo =
                    mailBugInfoDao.getMailBugInfoByMailId(uploadMailServerScanResultCmdInfo.getStep3ReceiveMailIdList().get(i));

            if(mailBugInfo == null) {
                serverCommondInfo.setCode(RESULT_SQL_ERROR);
                serverCommondInfo.setMsg("Query mail bug info error!");
                logger.error("Query mail bug info error,mail id("+
                        uploadMailServerScanResultCmdInfo.getStep3ReceiveMailIdList().get(i)+")!");
                return;
            }
            scanResultInfo.setMailId(uploadMailServerScanResultCmdInfo.getStep3ReceiveMailIdList().get(i));
            //scanResultInfo.setResultDetail(mailBugInfo.getId());
            scanResultInfoList.add(scanResultInfo);
        }

        if(!scanTaskInfoDao.insertScanTaskInfo(scanTaskInfo, scanResultInfoList)) {
            serverCommondInfo.setCode(RESULT_SQL_ERROR);
            serverCommondInfo.setMsg("Save scan result info error!");
            logger.error("Save scan result info error!!");
            return;
        }

        serverCommondInfo.setCode(RESULT_OK);
        serverCommondInfo.setMsg("");
        return;
    }

}
