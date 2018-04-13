package com.sct.redistest.dao.impl;

import com.sct.redistest.dao.IRedisInfoDao;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisCluster;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

@Repository("SpringRedisInfoDao")
public class SpringRedisClusterInfoDaoImpl implements IRedisInfoDao {
    private static final Logger logger = Logger.getLogger(SpringRedisClusterInfoDaoImpl.class);

    private JedisCluster jedisCluster = null;

    public JedisCluster getJedisCluster() {
        return jedisCluster;
    }

    public void setJedisCluster(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    @Override
    public boolean saveStrInfo(String key, String value) {
        if (StringUtils.isEmpty(key)) {
            logger.error("Invaild param!");
            return false;
        }

        try {
            jedisCluster.set(key, value);
        } catch (Exception e) {
            logger.error("Save str info error, error info(" + e.getMessage() + ")!");
        }
        return false;
    }

    @Override
    public String getStrInfo(String key) {
        if (StringUtils.isEmpty(key)) {
            logger.error("Invaild param!");
            return null;
        }

        String value = null;
        try {
            value = jedisCluster.get(key);
        } catch (Exception e) {
            logger.error("Get str info error, key(" + key + "),error info(" + e.getMessage() + ")!");
        }
        return value;
    }

    @Override
    public boolean alterStrByteInfo(String key, byte[] value, long offset) {
        if (StringUtils.isEmpty(key) || value == null || offset < 0) {
            logger.error("Invaild param!");
            return false;
        }

        try {
            String temp = jedisCluster.get(key);
            if (StringUtils.isEmpty(temp)) {
                logger.error("Invaild str value!");
                return false;
            }
            if (offset >= temp.length() * 8 || (value.length + offset) >= temp.length() * 8) {
                logger.error("Invaild param!");
                return false;
            }
            jedisCluster.setrange(key.getBytes(), offset, value);
        } catch (Exception e) {
            logger.error("Alter str byte info error, error info(" + e.getMessage() + ")!");
        }
        return false;
    }

    @Override
    public byte[] getStrByteInfo(String key, long offset, long size) {
        if (StringUtils.isEmpty(key) || offset < 0 || size <= 0) {
            logger.error("Invaild param!");
            return null;
        }

        try {
            String temp = jedisCluster.get(key);
            if (StringUtils.isEmpty(temp)) {
                logger.error("Invaild str value!");
                return null;
            }
            if (offset >= temp.length() || (size + offset) >= temp.length()) {
                logger.error("Invaild param!");
                return null;
            }
            return jedisCluster.getrange(key.getBytes(), offset, offset + size);

        } catch (Exception e) {
            logger.error("Get str byte info error, error info(" + e.getMessage() + ")!");
        }
        return null;
    }

    @Override
    public boolean deleteStrInfo(String key) {
        if (StringUtils.isEmpty(key)) {
            logger.error("Invaild param!");
            return false;
        }

        try {
            return jedisCluster.del(key) > 0;
        } catch (Exception e) {
            logger.error("Delete str info error, error info(" + e.getMessage() + ")!");
        }
        return false;
    }

    @Override
    public boolean saveBitMapInfo(String key, byte[] value) {
        if (StringUtils.isEmpty(key)) {
            logger.error("Invaild param!");
            return false;
        }

        try {
            jedisCluster.set(key.getBytes(), value);
        } catch (Exception e) {
            logger.error("Save bitmap info error, error info(" + e.getMessage() + ")!");
        }
        return false;
    }

    @Override
    public byte[] getBitMapInfo(String key) {
        if (StringUtils.isEmpty(key)) {
            logger.error("Invaild param!");
            return null;
        }

        byte[] value = null;
        try {

            value = jedisCluster.get(key.getBytes());
        } catch (Exception e) {
            logger.error("Get bitmap info error, key(" + key + "),error info(" + e.getMessage() + ")!");
        }
        return value;
    }

    @Override
    public boolean setBitMapBitInfo(String key, boolean value, long offset) {
        if (StringUtils.isEmpty(key) || offset < 0) {
            logger.error("Invaild param!");
            return false;
        }

        try {
            byte[] temp = jedisCluster.get(key.getBytes());
            if (temp == null || temp.length < 0) {
                logger.error("Invaild bitmap value!");
                return false;
            }
            if (offset >= temp.length * 8) {
                logger.error("Invaild param!");
                return false;
            }
            jedisCluster.setbit(key.getBytes(), offset, value);
        } catch (Exception e) {
            logger.error("Set bitmap bit info error, error info(" + e.getMessage() + ")!");
        }
        return false;
    }

    @Override
    public int getBitMapBitInfo(String key, long offset) {
        if (StringUtils.isEmpty(key) || offset < 0) {
            logger.error("Invaild param!");
            return -1;
        }

        try {
            byte[] temp = jedisCluster.get(key.getBytes());
            if (temp == null || temp.length < 0) {
                logger.error("Invaild bitmap value!");
                return -2;
            }
            if (offset >= temp.length * 8) {
                logger.error("Invaild param!");
                return -3;
            }
            return jedisCluster.getbit(key.getBytes(), offset) == true ? 1 : 0;

        } catch (Exception e) {
            logger.error("Get bitmap bit info error, error info(" + e.getMessage() + ")!");
        }
        return -4;
    }

    @Override
    public boolean deleteBitMapInfo(String key) {
        if (StringUtils.isEmpty(key)) {
            logger.error("Invaild param!");
            return false;
        }

        try {
            return jedisCluster.del(key) > 0;
        } catch (Exception e) {
            logger.error("Delete bitmap info error, error info(" + e.getMessage() + ")!");
        }
        return false;
    }

    @Override
    public boolean saveStrInfoList(String listName, List<String> valueList) {
        if (StringUtils.isEmpty(listName)) {
            logger.error("Invaild param!");
            return false;
        }
        if (valueList.isEmpty()) {
            return true;
        }

        try {
            for (int i = 0; i < valueList.size(); i++) {
                jedisCluster.rpush(listName, valueList.get(i));
            }
            return true;
        } catch (Exception e) {
            logger.error("Save str info list error, error info(" + e.getMessage() + ")!");
        }
        return false;
    }

    @Override
    public List<String> getStrInfoList(String listName, long beginIndex, long size) {
        if (StringUtils.isEmpty(listName) || beginIndex <= 0 || size < 0) {
            logger.error("Invaild param!");
            return null;
        }

        if (size == 0) {
            return new ArrayList<String>();
        }

        try {
            long totalSize = jedisCluster.llen(listName);
            if (beginIndex >= totalSize || beginIndex + size >= totalSize) {
                logger.error("Invaild param!");
                return null;
            }
            List<String> strInfoList = jedisCluster.lrange(listName, beginIndex, beginIndex + size);
            return strInfoList;
        } catch (Exception e) {
            logger.error("Get str info list error, error info(" + e.getMessage() + ")!");
        }
        return null;
    }

    @Override
    public String getStrInfoFromList(String listName, long index) {
        if (StringUtils.isEmpty(listName) || index < 0) {
            logger.error("Invaild param!");
            return null;
        }

        try {
            long totalSize = jedisCluster.llen(listName);
            if (index >= totalSize) {
                logger.error("Invaild param!");
                return null;
            }
            return jedisCluster.lindex(listName, index);
        } catch (Exception e) {
            logger.error("Get str info form list error, error info(" + e.getMessage() + ")!");
        }
        return null;
    }

    @Override
    public boolean deleteStrInfoFromList(String listName, long index) {
        if (StringUtils.isEmpty(listName) || index < 0) {
            logger.error("Invaild param!");
            return false;
        }

        try {
            long totalSize = jedisCluster.llen(listName);
            if (index >= totalSize) {
                logger.error("Invaild param!");
                return false;
            }
            jedisCluster.ltrim(listName, index, index + 1);
            return true;
        } catch (Exception e) {
            logger.error("Delete str info form list error, error info(" + e.getMessage() + ")!");
        }
        return false;
    }

    @Override
    public <T> boolean saveObjInfo(String key, T obj) {
        if (StringUtils.isEmpty(key) || obj == null) {
            logger.error("Invaild param!");
            return false;
        }

        try {
            Map data = objectToHashMap(obj);
            jedisCluster.hmset(key, data);
            return true;
        } catch (Exception e) {
            logger.error("Save obj info error, error info(" + e.getMessage() + ")!");
        }

        return false;
    }

    @Override
    public <T> T getObjInfo(String key, Class<T> cls) {
        if (StringUtils.isEmpty(key)) {
            logger.error("Invaild param!");
            return null;
        }

        T retObj = null;
        try {
            Field[] fields = cls.getDeclaredFields();
            retObj = cls.newInstance();
            for (Field field : fields) {
                String fieldName = field.getName();
                List<String> fieldValueList = jedisCluster.hmget(key, fieldName);
                if (fieldValueList.size() != 1) {
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
        } catch (Exception e) {
            logger.error("Get obj info error, error info(" + e.getMessage() + ")!");
        }
        return retObj;
    }

    @Override
    public String getObjItemInfo(String key, String itemName) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(itemName)) {
            logger.error("Invaild param!");
            return null;
        }

        String retObj = null;
        try {
            List<String> fieldValueList = jedisCluster.hmget(key, itemName);

            if (fieldValueList.size() != 1) {
                return null;
            }
            return fieldValueList.get(0);
        } catch (Exception e) {
            logger.error("Get obj item info error, error info(" + e.getMessage() + ")!");
        }
        return retObj;
    }

    @Override
    public boolean updateObjItemInfo(String key, String itemName, String item) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(itemName)) {
            logger.error("Invaild param!");
            return false;
        }

        try {
            jedisCluster.hset(key, itemName, item);
            return true;
        } catch (Exception e) {
            logger.error("Update obj item info error, error info(" + e.getMessage() + ")!");
        }
        return false;
    }

    @Override
    public boolean deleteObjInfo(String key) {
        if (StringUtils.isEmpty(key)) {
            logger.error("Invaild param!");
            return false;
        }

        try {
            jedisCluster.del(key);
            return true;
        } catch (Exception e) {
            logger.error("Delete obj info error, error info(" + e.getMessage() + ")!");
        }
        return false;
    }

    private <T> Map<String, String> objectToHashMap(T obj) throws Exception {

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
                } else if (Long.class.equals(type) || long.class.equals(type)) {
                    value = String.valueOf(itemObj);
                } else if (Boolean.class.equals(type) || boolean.class.equals(type)) {
                    value = String.valueOf(itemObj);
                } else if (Date.class.equals(type)) {
                    value = String.valueOf(((Date) itemObj).getTime());
                } else if (Short.class.equals(type) || short.class.equals(type)) {
                    value = String.valueOf(itemObj);
                } else if (Float.class.equals(type) || float.class.equals(type)) {
                    value = String.valueOf(itemObj);
                } else if (String.class.equals(type)) {
                    value = (String) itemObj;
                } else {
                    throw new Exception("Unsupport value type,field name(" + fieldName + "),type(" + type.getName() + ")!");
                }

                if (value != null) {
                    hashMap.put(key, value);
                }
            }
        }
        return hashMap;
    }

}
