package com.sct.redistest.dao;

import java.util.List;

public interface IRedisInfoDao {
     public boolean saveStrInfo(String key,String value);
     public String getStrInfo(String key);
     public boolean alterStrByteInfo(String key,byte[] value,long offset);
     public byte[] getStrByteInfo(String key,long offset, long size);
     public boolean deleteStrInfo(String key);

     public boolean saveBitMapInfo(String key,byte[] value);
     public byte[] getBitMapInfo(String key);
     public boolean setBitMapBitInfo(String key,boolean value,long offset);
     public int getBitMapBitInfo(String key,long offset);
     public boolean deleteBitMapInfo(String key);

     public boolean saveStrInfoList(String listName,List<String> valueList);
     public List<String> getStrInfoList(String listName,long beginIndex,long size);
     public String getStrInfoFromList(String listName,long index);
     public boolean deleteStrInfoFromList(String listName,long index);

     public <T> boolean saveObjInfo(String key,T obj);
     public <T> T getObjInfo(String key , Class<T> cls);
     public String getObjItemInfo(String key,String itemName);
     public boolean updateObjItemInfo(String key,String itemName, String item);
     public boolean deleteObjInfo(String key);
}