package com.sct.mailsecurityscanserver.dao.impl;

import com.alibaba.fastjson.JSON;
import com.sct.mailsecurityscanserver.dao.ISessionInfoDao;
import com.sct.mailsecurityscanserver.entity.*;
import com.sct.mailsecurityscanserver.initialize.InitializeData;
import com.sct.mailsecurityscanserver.util.EncodeMd5Util;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.ShardedJedis;

@Repository("SessionInfoDao")
public class SessionInfoDaoImpl implements ISessionInfoDao{    
    private static final Logger logger = Logger.getLogger(SessionInfoDaoImpl.class);

    @Override
    public SessionInfo createSessionInfo(ShardedJedis shardedJedis, TerminalInfo terminalInfo) {
        if(terminalInfo == null) {
            return null;
        }

        SessionInfo sessionInfo=null;
        if(shardedJedis==null) {
            return null;
        }

        try {
            sessionInfo=new SessionInfo();

            String sessionId = EncodeMd5Util.makeMd5(("SESSION_ID:" + terminalInfo.getId()));
            sessionInfo.setSessionId(sessionId);
            sessionInfo.setTimeOutInterval(InitializeData.getSessionInfoTimeOutInterval());
            sessionInfo.setAttribute("TERMINAL_INFO", JSON.toJSONString(terminalInfo));

            String sessionInfoStr=sessionInfo.toString();
            shardedJedis.set(sessionInfo.getSessionId(), sessionInfoStr);
        } catch(Exception e) {
            logger.error("Create session error,error info("+e.getMessage()+")!");
        }
        if (shardedJedis != null) {
            shardedJedis.close();
        }
        return sessionInfo;
    }
    
    @Override
    public SessionInfo getSessionInfo(ShardedJedis shardedJedis, String sessionId) {
        if (sessionId==null||"00000000000000000000000000000000".equals(sessionId)) {
            return null;
        }
        SessionInfo sessionInfo=null;          
        if(shardedJedis==null) {
            return null;
        }    
        
        try {
            String sessionInfoStr=shardedJedis.get(sessionId);
            if(sessionInfoStr != null && !sessionInfoStr.isEmpty()) {
                sessionInfo=new SessionInfo(sessionInfoStr);
            }
        } catch(Exception e) {
            logger.error("Read session error,error info("+e.getMessage()+")!");
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
        return sessionInfo;
    }
    
    @Override
    public boolean updateSessionInfo(ShardedJedis shardedJedis, SessionInfo sessionInfo) {
        if(sessionInfo==null) {
            return false;
        }      
        if(shardedJedis==null) {
            return false;
        }    
        
        boolean ret=false;
        try {
            String sessionInfoStr=sessionInfo.toString();
            shardedJedis.set(sessionInfo.getSessionId(), sessionInfoStr);
            ret=true;
        } catch(Exception e) {
            logger.error("Update session error,error info("+e.getMessage()+")!");
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
        return ret;
    }
    
    @Override
    public boolean deleteSessionInfo(ShardedJedis shardedJedis, String sessionId) {
        if (sessionId==null||"00000000000000000000000000000000".equals(sessionId)) {
            return false;
        } 
        if(shardedJedis==null) {
            return false;
        }    
        
        boolean ret=false;
        try {
            ret=shardedJedis.del(sessionId)>0;
        } catch (Exception e) {
            logger.error("Delete session error,error info("+e.getMessage()+")!");
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
        return ret;
    }
}
