package com.sct.mailsecurityscanserver.dao;

import com.sct.mailsecurityscanserver.entity.*;
import redis.clients.jedis.ShardedJedis;

public interface ISessionInfoDao {
     public SessionInfo createSessionInfo(ShardedJedis shardedJedis, TerminalInfo terminalInfo);
     public SessionInfo getSessionInfo(ShardedJedis shardedJedis, String sessionId);
     public boolean updateSessionInfo(ShardedJedis shardedJedis, SessionInfo sessionInfo);
     public boolean deleteSessionInfo(ShardedJedis shardedJedis, String sessionId);
}