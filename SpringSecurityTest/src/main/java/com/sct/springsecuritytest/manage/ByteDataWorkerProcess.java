package com.sct.mailsecurityscanserver.manage;

import com.alibaba.fastjson.JSON;
import com.sct.mailsecurityscanserver.entity.SessionInfo;
import com.sct.mailsecurityscanserver.initialize.InitializeBean;
import com.sct.mailsecurityscanserver.service.IComponentsByteDataService;
import com.sct.mailsecurityscanserver.struct.RequestInfo;
import org.apache.log4j.Logger;

import java.io.Serializable;

import static com.sct.mailsecurityscanserver.define.CommonDefine.*;

public class ByteDataWorkerProcess implements Runnable, Serializable {
    private static final Logger logger = Logger.getLogger(ByteDataWorkerProcess.class);
    private RequestInfo requestInfo=null;

    public ByteDataWorkerProcess(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    @Override
    public void run() {
        if (requestInfo != null) {
            byte[] retCommondInfo = null;
            try {
                SessionInfo sessionInfo = requestInfo.getSessionInfo();

                Integer cmd = requestInfo.getCommondInfo().getCmd();
                Integer type = requestInfo.getCommondInfo().getType();

                IComponentsByteDataService bean = InitializeBean.getBean(getBeanByName(cmd));
                try {
                    retCommondInfo = bean.execute(requestInfo.getCommondInfo(), sessionInfo);
                } catch (Exception e) {
                    String errorInfo = "Analyse request failed,error info("+ e.getMessage() +"),cmd info("+
                            JSON.toJSONString(requestInfo.getCommondInfo()) +"),session info("+ sessionInfo.toString() +")!";
                    logger.error(errorInfo);
                    retCommondInfo = errorInfo.getBytes();
                }

            } catch (Exception e) {
                String errorInfo ="Analyse request failed,error info("+ e.getMessage() +")!";
                logger.error(errorInfo);
                retCommondInfo = errorInfo.getBytes();
            }
            
            if (!ResultMsgProducer.getInstance().addByteDataQueue(requestInfo.getResponseTopic(), retCommondInfo, requestInfo.getResponseKey())){
                String errorInfo = "Add response data to queue error!";
                logger.error(errorInfo);
                retCommondInfo = errorInfo.getBytes();
            }
        }
    }
        
    private static String getBeanByName(Integer cmd) {
        String beanName = "";
        switch (cmd) {
            case MAIL_BYTE_DATA_SERVICE_CMD:
                beanName = "MailByteDataService";
                break;
            default:
                break;
        }
        return beanName;
    }
}
