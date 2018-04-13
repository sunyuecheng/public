package com.sct.mailsecurityscanserver.controller;

import com.sct.mailsecurityscanserver.dao.ISessionInfoDao;
import com.sct.mailsecurityscanserver.entity.SessionInfo;
import com.sct.mailsecurityscanserver.initialize.InitializeData;
import com.sct.mailsecurityscanserver.commond.ClientCommondInfo;
import com.sct.mailsecurityscanserver.manage.WorkerMsgProducer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class TerminalController {
    private static final Logger logger = Logger.getLogger(TerminalController.class);

    @Resource
    private ShardedJedisPool shardedJedisPool;

    @Autowired
    @Qualifier("SessionInfoDao")
    private ISessionInfoDao sessionInfoDao;
    
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/terminal_json", method = RequestMethod.POST)
    public @ResponseBody DeferredResult<String> jsonController( HttpServletRequest request, HttpServletResponse response,
                                                            @RequestBody ClientCommondInfo clientCommondInfo ) {
        final DeferredResult<String> deferredResult = new DeferredResult<String>(InitializeData.getWaitResponseTime()*1000); 
                
        try {
            Integer cmd = clientCommondInfo.getCmd();
            Integer type = clientCommondInfo.getType();

            String sessionId = clientCommondInfo.getSessionId();
            SessionInfo sessionInfo = sessionInfoDao.getSessionInfo(shardedJedisPool.getResource(), sessionId);
            if (cmd != null && type != null) {
                if (!WorkerMsgProducer.getInstance().addStrDataQueue(request.getRemoteAddr(), clientCommondInfo, sessionInfo, deferredResult)) {
                    logger.error("Add client request data to queue error!");
                    deferredResult.setResult("Add client request data to queue error!");
                }    
            } else {                   
                logger.error("Get client request data error!");
                deferredResult.setResult("Get client request data error!");
            }
        } catch (Exception e) {
            logger.error("Analyse client request data error,error info(" + e.getMessage() + ")!");
            deferredResult.setResult("Add client request data to queue error!");
        }
 
        return deferredResult;
    }

    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/terminal_data", method = RequestMethod.POST)
    public @ResponseBody DeferredResult<byte[]> dataController( HttpServletRequest request, HttpServletResponse response,
                                                            @RequestBody ClientCommondInfo clientCommondInfo ) {
        final DeferredResult<byte[]> deferredResult = new DeferredResult<byte[]>(InitializeData.getWaitResponseTime()*1000);

        try {
            Integer cmd = clientCommondInfo.getCmd();
            Integer type = clientCommondInfo.getType();

            String sessionId = clientCommondInfo.getSessionId();
            SessionInfo sessionInfo = sessionInfoDao.getSessionInfo(shardedJedisPool.getResource(), sessionId);
            if (cmd != null && type != null) {
                if (!WorkerMsgProducer.getInstance().addByteDataQueue(request.getRemoteAddr(), clientCommondInfo, sessionInfo, deferredResult)) {
                    String errorInfo = "Add client request data to queue error!";
                    logger.error(errorInfo);
                    deferredResult.setResult(errorInfo.getBytes());
                }
            } else {
                String errorInfo = "Get client request data error!";
                logger.error(errorInfo);
                deferredResult.setResult(errorInfo.getBytes());
            }
        } catch (Exception e) {
            String errorInfo ="Analyse client request data error,error info(" + e.getMessage() + ")!";
            logger.error(errorInfo);
            deferredResult.setResult(errorInfo.getBytes());
        }

        return deferredResult;
    }
    
}