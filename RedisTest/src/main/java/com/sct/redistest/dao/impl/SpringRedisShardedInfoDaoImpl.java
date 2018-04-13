package com.sct.redistest.dao.impl;

import com.sct.redistest.dao.IRedisInfoDao;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;
import redis.clients.jedis.ShardedJedisPool;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

@Repository("SpringRedisShardedInfoDao")
public class SpringRedisShardedInfoDaoImpl implements IRedisInfoDao {
    private static final Logger logger = Logger.getLogger(SpringRedisShardedInfoDaoImpl.class);

    private ShardedJedisPool shardedJedisPool = null;

    public ShardedJedisPool getShardedJedisPool() {
        return shardedJedisPool;
    }

    public void setShardedJedisPool(ShardedJedisPool shardedJedisPool) {
        this.shardedJedisPool = shardedJedisPool;
    }

    @Override
    public boolean saveStrInfo(String key, String value) {
        if(StringUtils.isEmpty(key)) {
            logger.error("Invaild param!");
            return false;
        }

        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            if (jedis!=null) {
                jedis.set(key, value);
            }
        } catch (Exception e) {
            logger.error("Save str info error, error info("+ e.getMessage() +")!");
        } finally {
            if(jedis!= null) {
                jedis.close();
            }
        }
        return false;
    }

    @Override
    public String getStrInfo(String key) {
        if(StringUtils.isEmpty(key)) {
            logger.error("Invaild param!");
            return null;
        }

        ShardedJedis jedis = null;
        String value = null;
        try {
            jedis = shardedJedisPool.getResource();
            if (jedis!=null) {
                value = jedis.get(key);
            }
        } catch (Exception e) {
            logger.error("Get str info error, key("+key+"),error info("+ e.getMessage() +")!");
        } finally {
            if(jedis!= null) {
                jedis.close();
            }
        }
        return value;
    }

    @Override
    public boolean alterStrByteInfo(String key, byte[] value, long offset) {
        if(StringUtils.isEmpty(key) || value==null || offset <0) {
            logger.error("Invaild param!");
            return false;
        }

        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            if (jedis!=null) {
                String temp = jedis.get(key);
                if(StringUtils.isEmpty(temp)) {
                    logger.error("Invaild str value!");
                    return false;
                }
                if(offset >= temp.length()*8 || (value.length+offset) >=temp.length()*8) {
                    logger.error("Invaild param!");
                    return false;
                }
                jedis.setrange(key.getBytes(), offset, value);
            }
        } catch (Exception e) {
            logger.error("Alter str byte info error, error info("+ e.getMessage() +")!");
        } finally {
            if(jedis!= null) {
                jedis.close();
            }
        }
        return false;
    }

    @Override
    public byte[] getStrByteInfo(String key,long offset, long size) {
        if(StringUtils.isEmpty(key) || offset <0 || size <=0) {
            logger.error("Invaild param!");
            return null;
        }

        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            if (jedis!=null) {
                String temp = jedis.get(key);
                if(StringUtils.isEmpty(temp)) {
                    logger.error("Invaild str value!");
                    return null;
                }
                if(offset >= temp.length() || (size+offset) >=temp.length()) {
                    logger.error("Invaild param!");
                    return null;
                }
                return jedis.getrange(key.getBytes(), offset, offset+size);
            }
        } catch (Exception e) {
            logger.error("Get str byte info error, error info("+ e.getMessage() +")!");
        } finally {
            if(jedis!= null) {
                jedis.close();
            }
        }
        return null;
    }

    @Override
    public boolean deleteStrInfo(String key) {
        if(StringUtils.isEmpty(key)) {
            logger.error("Invaild param!");
            return false;
        }

        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            if (jedis!=null) {
                return jedis.del(key)>0;
            }
        } catch (Exception e) {
            logger.error("Delete str info error, error info("+ e.getMessage() +")!");
        } finally {
            if(jedis!= null) {
                jedis.close();
            }
        }
        return false;
    }

    @Override
    public boolean saveBitMapInfo(String key, byte[] value) {
        if(StringUtils.isEmpty(key)) {
            logger.error("Invaild param!");
            return false;
        }

        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            if (jedis!=null) {
                jedis.set(key.getBytes(), value);
            }
        } catch (Exception e) {
            logger.error("Save bitmap info error, error info("+ e.getMessage() +")!");
        } finally {
            if(jedis!= null) {
                jedis.close();
            }
        }
        return false;
    }

    @Override
    public byte[] getBitMapInfo(String key) {
        if(StringUtils.isEmpty(key)) {
            logger.error("Invaild param!");
            return null;
        }

        ShardedJedis jedis = null;
        byte[] value = null;
        try {
            jedis = shardedJedisPool.getResource();
            if (jedis!=null) {
                value = jedis.get(key.getBytes());
            }
        } catch (Exception e) {
            logger.error("Get bitmap info error, key("+key+"),error info("+ e.getMessage() +")!");
        } finally {
            if(jedis!= null) {
                jedis.close();
            }
        }
        return value;
    }

    @Override
    public boolean setBitMapBitInfo(String key, boolean value, long offset) {
        if(StringUtils.isEmpty(key) || offset <0) {
            logger.error("Invaild param!");
            return false;
        }

        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            if (jedis!=null) {
                byte[] temp = jedis.get(key.getBytes());
                if(temp==null || temp.length<0) {
                    logger.error("Invaild bitmap value!");
                    return false;
                }
                if(offset >= temp.length*8) {
                    logger.error("Invaild param!");
                    return false;
                }
                jedis.setbit(key.getBytes(), offset, value);
            }
        } catch (Exception e) {
            logger.error("Set bitmap bit info error, error info("+ e.getMessage() +")!");
        } finally {
            if(jedis!= null) {
                jedis.close();
            }
        }
        return false;
    }

    @Override
    public int getBitMapBitInfo(String key, long offset) {
        if(StringUtils.isEmpty(key) || offset <0) {
            logger.error("Invaild param!");
            return -1;
        }

        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            if (jedis!=null) {
                byte[] temp = jedis.get(key.getBytes());
                if(temp==null || temp.length<0) {
                    logger.error("Invaild bitmap value!");
                    return -2;
                }
                if(offset >= temp.length*8) {
                    logger.error("Invaild param!");
                    return -3;
                }
                return jedis.getbit(key.getBytes(), offset) == true ? 1:0;
            }
        } catch (Exception e) {
            logger.error("Get bitmap bit info error, error info("+ e.getMessage() +")!");
        } finally {
            if(jedis!= null) {
                jedis.close();
            }
        }
        return -4;
    }

    @Override
    public boolean deleteBitMapInfo(String key) {
        if(StringUtils.isEmpty(key)) {
            logger.error("Invaild param!");
            return false;
        }

        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            if (jedis!=null) {
                return jedis.del(key)>0;
            }
        } catch (Exception e) {
            logger.error("Delete bitmap info error, error info("+ e.getMessage() +")!");
        } finally {
            if(jedis!= null) {
                jedis.close();
            }
        }
        return false;
    }

    @Override
    public boolean saveStrInfoList(String listName, List<String> valueList) {
        if(StringUtils.isEmpty(listName) ) {
            logger.error("Invaild param!");
            return false;
        }
        if(valueList.isEmpty()) {
            return true;
        }

        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            if (jedis!=null) {
                ShardedJedisPipeline pipeline = jedis.pipelined();
                for(int i=0; i< valueList.size();i++) {
                    pipeline.rpush(listName,valueList.get(i));
                }
                pipeline.sync();
                return true;
            }
        } catch (Exception e) {
            logger.error("Save str info list error, error info("+ e.getMessage() +")!");
        } finally {
            if(jedis!= null) {
                jedis.close();
            }
        }
        return false;
    }

    @Override
    public List<String> getStrInfoList(String listName, long beginIndex, long size) {
        if(StringUtils.isEmpty(listName) || beginIndex<=0 || size<0) {
            logger.error("Invaild param!");
            return null;
        }

        if(size==0) {
            return new ArrayList<String>();
        }

        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            if (jedis!=null) {
                long totalSize = jedis.llen(listName);
                if(beginIndex>=totalSize || beginIndex + size>=totalSize) {
                    logger.error("Invaild param!");
                    return null;
                }
                List<String> strInfoList = jedis.lrange(listName,beginIndex,beginIndex+size);
                return strInfoList;
            }
        } catch (Exception e) {
            logger.error("Get str info list error, error info("+ e.getMessage() +")!");
        } finally {
            if(jedis!= null) {
                jedis.close();
            }
        }
        return null;
    }

    @Override
    public String getStrInfoFromList(String listName, long index) {
        if(StringUtils.isEmpty(listName) || index<0) {
            logger.error("Invaild param!");
            return null;
        }

        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            if (jedis!=null) {
                long totalSize = jedis.llen(listName);
                if(index>=totalSize ) {
                    logger.error("Invaild param!");
                    return null;
                }
                return jedis.lindex(listName,index);
            }
        } catch (Exception e) {
            logger.error("Get str info form list error, error info("+ e.getMessage() +")!");
        } finally {
            if(jedis!= null) {
                jedis.close();
            }
        }
        return null;
    }

    @Override
    public boolean deleteStrInfoFromList(String listName, long index) {
        if(StringUtils.isEmpty(listName) || index<0) {
            logger.error("Invaild param!");
            return false;
        }

        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            if (jedis!=null) {
                long totalSize = jedis.llen(listName);
                if(index>=totalSize ) {
                    logger.error("Invaild param!");
                    return false;
                }
                jedis.ltrim(listName,index,index+1);
                return true;
            }
        } catch (Exception e) {
            logger.error("Delete str info form list error, error info("+ e.getMessage() +")!");
        } finally {
            if(jedis!= null) {
                jedis.close();
            }
        }
        return false;
    }

    @Override
    public <T> boolean saveObjInfo(String key, T obj) {
        if(StringUtils.isEmpty(key) || obj==null) {
            logger.error("Invaild param!");
            return false;
        }

        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            if (jedis!=null) {
                Map data = objectToHashMap(obj);
                jedis.hmset(key, data);
                return true;
            }
        } catch (Exception e) {
            logger.error("Save obj info error, error info("+ e.getMessage() +")!");
        } finally {
            if(jedis!= null) {
                jedis.close();
            }
        }
        return false;
    }

    @Override
    public <T> T getObjInfo(String key, Class<T> cls) {
        if(StringUtils.isEmpty(key)) {
            logger.error("Invaild param!");
            return null;
        }

        ShardedJedis jedis = null;
        T retObj = null;
        try {
            jedis = shardedJedisPool.getResource();
            if (jedis!=null) {
                Field[] fields = cls.getDeclaredFields();
                retObj = cls.newInstance();
                for (Field field : fields) {
                    String fieldName = field.getName();
                    List<String> fieldValueList = jedis.hmget(key,fieldName);
                    if(fieldValueList.size()!=1) {
                        String fieldValue = fieldValueList.get(0);

                        if (fieldName != null && fieldValue != null) {
                            PropertyDescriptor pd = new PropertyDescriptor(fieldName, cls);
                            Method setMethod = pd.getWriteMethod();

                            Class type = field.getType();
                            if (Integer.class.equals(type) || int.class.equals(type)) {
                                Integer integerVal = Integer.valueOf(fieldValue);
                                setMethod.invoke(retObj, integerVal);
                            } else if (Long.class.equals(type) || long.class.equals(type)) {
                                Long longVal = Long.valueOf(fieldValue);
                                setMethod.invoke(retObj, longVal);
                            } else if (Boolean.class.equals(type) || boolean.class.equals(type)) {
                                Boolean boolVal = Boolean.getBoolean(fieldValue);
                                setMethod.invoke(retObj, boolVal);
                            } else if (Date.class.equals(type)) {
                                Long longVal = Long.valueOf(fieldValue);
                                Date date = new Date(longVal);
                                setMethod.invoke(retObj, date);
                            } else if (Short.class.equals(type) || short.class.equals(type)) {
                                setMethod.invoke(retObj, new Short(fieldValue));
                            } else if (Float.class.equals(type) || float.class.equals(type)) {
                                setMethod.invoke(retObj, new Float(fieldValue));
                            } else if (String.class.equals(type)) {
                                setMethod.invoke(retObj, fieldValue);
                            } else {
                                throw new Exception("Unsupport value type,field name(" + fieldName + "),type(" + type.getName() + ")!");
                            }

                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Get obj info error, error info("+ e.getMessage() +")!");
        } finally {
            if(jedis!= null) {
                jedis.close();
            }
        }
        return retObj;
    }

    @Override
    public String getObjItemInfo(String key, String itemName) {
        if(StringUtils.isEmpty(key) || StringUtils.isEmpty(itemName)) {
            logger.error("Invaild param!");
            return null;
        }

        ShardedJedis jedis = null;
        String retObj = null;
        try {
            jedis = shardedJedisPool.getResource();
            List<String> fieldValueList = jedis.hmget(key,itemName);

            if(fieldValueList.size()!=1) {
                return null;
            }
            return fieldValueList.get(0);
        } catch (Exception e) {
            logger.error("Get obj item info error, error info("+ e.getMessage() +")!");
        } finally {
            if(jedis!= null) {
                jedis.close();
            }
        }
        return retObj;
    }

    @Override
    public boolean updateObjItemInfo(String key, String itemName, String item) {
        if(StringUtils.isEmpty(key) || StringUtils.isEmpty(itemName)) {
            logger.error("Invaild param!");
            return false;
        }

        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            jedis.hset(key,itemName,item);
            return true;
        } catch (Exception e) {
            logger.error("Update obj item info error, error info("+ e.getMessage() +")!");
        } finally {
            if(jedis!= null) {
                jedis.close();
            }
        }
        return false;
    }

    @Override
    public boolean deleteObjInfo(String key) {
        if(StringUtils.isEmpty(key)) {
            logger.error("Invaild param!");
            return false;
        }

        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
            jedis.del(key);
            return true;
        } catch (Exception e) {
            logger.error("Delete obj info error, error info("+ e.getMessage() +")!");
        } finally {
            if(jedis!= null) {
                jedis.close();
            }
        }
        return false;
    }

    private <T> Map<String,String> objectToHashMap(T obj) throws Exception {

        Class clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();

        Map<String, String> hashMap = new HashMap<String, String>();

        for (Field field : fields) {
            String fieldName = field.getName();
            if (fieldName != null) {
                PropertyDescriptor pd = new PropertyDescriptor(fieldName, clazz);
                Method getMethod = pd.getReadMethod();

                Object itemObj = getMethod.invoke(clazz, new Object[]{});

                Class type = field.getType();

                String key = fieldName;
                String value = null;
                if (Integer.class.equals(type) || int.class.equals(type)) {
                    value = String.valueOf(itemObj);
                } else if (Long.class.equals(type)||long.class.equals(type)) {
                    value = String.valueOf(itemObj);
                } else if (Boolean.class.equals(type) || boolean.class.equals(type)) {
                    value = String.valueOf(itemObj);
                } else if (Date.class.equals(type)) {
                    value = String.valueOf(((Date)itemObj).getTime());
                } else if (Short.class.equals(type) || short.class.equals(type)) {
                    value = String.valueOf(itemObj);
                } else if (Float.class.equals(type)||float.class.equals(type)) {
                    value = String.valueOf(itemObj);
                } else if (String.class.equals(type)){
                    value = (String)itemObj;
                } else {
                    throw new Exception("Unsupport value type,field name("+fieldName +"),type("+type.getName()+")!");
                }

                if(value!=null) {
                    hashMap.put(key,value);
                }
            }
        }
        return hashMap;
    }

}
