package com.sct.mailsecurityscanserver.manage;

import com.alibaba.fastjson.JSON;

import static com.sct.mailsecurityscanserver.define.CommonDefine.*;
import static com.sct.mailsecurityscanserver.define.ErrorCodeDefine.*;

import com.sct.mailsecurityscanserver.commond.ServerCommondInfo;
import com.sct.mailsecurityscanserver.service.IComponentsStrDataService;
import com.sct.mailsecurityscanserver.initialize.InitializeBean;
import com.sct.mailsecurityscanserver.entity.SessionInfo;
import com.sct.mailsecurityscanserver.struct.RequestInfo;
import java.io.Serializable;

import org.apache.log4j.Logger;

public class StrDataWorkerProcess implements Runnable, Serializable {
    private static final Logger logger = Logger.getLogger(StrDataWorkerProcess.class);
    private RequestInfo requestInfo=null;

    public StrDataWorkerProcess(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    @Override
    public void run() {
        if (requestInfo != null) {
            ServerCommondInfo retCommondInfo = null;
            try {
                SessionInfo sessionInfo = requestInfo.getSessionInfo();

                Integer cmd = requestInfo.getCommondInfo().getCmd();
                Integer type = requestInfo.getCommondInfo().getType();

                IComponentsStrDataService bean = InitializeBean.getBean(getBeanByName(cmd));
                try {
                    retCommondInfo = bean.execute(requestInfo.getCommondInfo(), sessionInfo);
                } catch (Exception e) {
                    logger.error("Analyse request failed,error info("+ e.getMessage() +"),cmd info("+ JSON.toJSONString(requestInfo.getCommondInfo()) +"),session info("+ sessionInfo.toString() +")!");
                    retCommondInfo = new ServerCommondInfo();
                    retCommondInfo.setCmd(cmd);
                    retCommondInfo.setType(type);
                    retCommondInfo.setCode(RESULT_ANALYSE_REQUEST_ERROR);
                    retCommondInfo.setMsg(e.getMessage());
                }

            } catch (Exception e) {                    
                logger.error("Analyse request failed,error info("+ e.getMessage() +")!");
            }
            
            if (!ResultMsgProducer.getInstance().addStrDataQueue(requestInfo.getResponseTopic(), retCommondInfo, requestInfo.getResponseKey())){
                logger.error("Add response data to queue error!");
            }
        }
    }
        
    private static String getBeanByName(Integer cmd) {
        String beanName = "";
        switch (cmd) {
            case TERMINAL_VERSION_SERVICE_CMD:
                beanName = "TerminalVersionService";
                break;
            case DEVICE_SERVICE_CMD:
                beanName = "DeviceService";
                break;
            case MAIL_STR_DATA_SERVICE_CMD:
                beanName = "MailStrDataService";
                break;
            case MANAGER_SERVICE_CMD:
                beanName = "ManagerService";
                break;
            case OPERATOR_LOGIN_SERVICE_CMD:
                beanName = "OperatorLoginService";
                break;
            case OPERATOR_SERVICE_CMD:
                beanName = "OperatorService";
                break;
            default:
                break;
        }
        return beanName;
    }
}
