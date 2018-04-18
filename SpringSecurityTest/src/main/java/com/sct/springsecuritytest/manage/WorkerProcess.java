package com.sct.springsecuritytest.manage;

import com.alibaba.fastjson.JSON;

import static com.sct.springsecuritytest.define.CommonDefine.*;
import static com.sct.springsecuritytest.define.ErrorCodeDefine.*;

import com.sct.springsecuritytest.commond.ClientCommondInfo;
import com.sct.springsecuritytest.commond.ServerCommondInfo;
import com.sct.springsecuritytest.initialize.InitializeBean;
import java.io.Serializable;

import com.sct.springsecuritytest.service.IComponentsDataService;
import org.apache.log4j.Logger;
import org.springframework.web.context.request.async.DeferredResult;

public class WorkerProcess implements Runnable, Serializable {
    private static final Logger logger = Logger.getLogger(WorkerProcess.class);

    private ClientCommondInfo commondInfo=null;
    private DeferredResult<String> deferredResult;

    public WorkerProcess(ClientCommondInfo commondInfo, DeferredResult<String> deferredResult) {
        this.commondInfo = commondInfo;
        this.deferredResult = deferredResult;
    }

    @Override
    public void run() {
        if (commondInfo != null && deferredResult!=null) {
            ServerCommondInfo retCommondInfo = null;
            Integer cmd = commondInfo.getCmd();
            Integer type = commondInfo.getType();
            try {
                IComponentsDataService bean = InitializeBean.getBean(getBeanByName(cmd));
                try {
                    retCommondInfo = bean.execute(commondInfo);
                } catch (Exception e) {
                    logger.error("Analyse request failed,error info("+ e.getMessage() +"),cmd info("+ JSON.toJSONString(commondInfo) +")!");
                    retCommondInfo = new ServerCommondInfo();
                    retCommondInfo.setCmd(cmd);
                    retCommondInfo.setType(type);
                    retCommondInfo.setCode(RESULT_ANALYSE_REQUEST_ERROR);
                    retCommondInfo.setMsg(e.getMessage());
                }

            } catch (Exception e) {                    
                logger.error("Analyse request failed,error info("+ e.getMessage() +")!");
                retCommondInfo = new ServerCommondInfo();
                retCommondInfo.setCmd(cmd);
                retCommondInfo.setType(type);
                retCommondInfo.setCode(RESULT_ANALYSE_REQUEST_ERROR);
                retCommondInfo.setMsg(e.getMessage());
            }

            if(retCommondInfo!=null) {
                deferredResult.setResult(JSON.toJSONString(retCommondInfo));
            }
        }
    }
        
    private static String getBeanByName(Integer cmd) {
        String beanName = "";
        switch (cmd) {
            case USER_INFO_SERVICE_CMD:
                beanName = "UserInfoService";
                break;
            default:
                break;
        }
        return beanName;
    }
}
