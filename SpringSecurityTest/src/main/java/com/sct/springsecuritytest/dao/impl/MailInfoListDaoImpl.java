package com.sct.mailsecurityscanserver.dao.impl;

import com.sct.mailsecurityscanserver.dao.IMailInfoListDao;
import com.sct.mailsecurityscanserver.entity.SessionInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.ShardedJedis;

@Repository("MailInfoListDao")
public class MailInfoListDaoImpl implements IMailInfoListDao {
    private static final Logger logger = Logger.getLogger(MailInfoListDaoImpl.class);

    @Override
    public boolean updateMailInfoListData(ShardedJedis shardedJedis, byte[] mailInfoListData) {
        if(mailInfoListData == null) {
            return false;
        }

        if(shardedJedis==null) {
            return false;
        }

        boolean ret = false;
        try {
            shardedJedis.set("MAIL_INFO_LIST".getBytes(), mailInfoListData);
            ret = true;
        } catch(Exception e) {
            logger.error("Save mail info list error,error info("+e.getMessage()+")!");
        }
        if (shardedJedis != null) {
            shardedJedis.close();
        }
        return ret;
    }

    @Override
    public byte[] getMailInfoListData(ShardedJedis shardedJedis) {
        if(shardedJedis==null) {
            return null;
        }
        byte[] mailInfoListData = null;

        try {
            mailInfoListData =shardedJedis.get("MAIL_INFO_LIST".getBytes());
        } catch(Exception e) {
            logger.error("Read mail info list error,error info("+e.getMessage()+")!");
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
        return mailInfoListData;
    }

}
