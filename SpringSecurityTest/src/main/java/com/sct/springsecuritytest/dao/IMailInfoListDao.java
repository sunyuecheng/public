package com.sct.mailsecurityscanserver.dao;

import redis.clients.jedis.ShardedJedis;

public interface IMailInfoListDao {
     public boolean updateMailInfoListData(ShardedJedis shardedJedis, byte[] mailInfoListData);
     public byte[] getMailInfoListData(ShardedJedis shardedJedis);
}