package com.sct.springsecuritytest.controller;

import com.sct.springsecuritytest.initialize.InitializeData;
import com.sct.springsecuritytest.commond.ClientCommondInfo;
import com.sct.springsecuritytest.manage.WorkerProcess;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {
    private static final Logger logger = Logger.getLogger(UserController.class);

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;
    
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/json", method = RequestMethod.POST)
    public @ResponseBody DeferredResult<String> jsonController( HttpServletRequest request, HttpServletResponse response,
                                                            @RequestBody ClientCommondInfo clientCommondInfo ) {
        final DeferredResult<String> deferredResult = new DeferredResult<String>(InitializeData.getWaitResponseTime()*1000);

        try {
            Integer cmd = clientCommondInfo.getCmd();
            Integer type = clientCommondInfo.getType();

            if (cmd != null && type != null) {
                if(taskExecutor.getActiveCount()<taskExecutor.getMaxPoolSize()) {
                    taskExecutor.execute(new WorkerProcess(clientCommondInfo, deferredResult));
                }
            } else {                   
                logger.error("Get client request data error.");
                deferredResult.setResult("Get client request data error.");
            }
        } catch (Exception e) {
            logger.error("Analyse client request data error,error info(" + e.getMessage() + ").");
            deferredResult.setResult("Add client request data to queue error.");
        }
 
        return deferredResult;
    }

}