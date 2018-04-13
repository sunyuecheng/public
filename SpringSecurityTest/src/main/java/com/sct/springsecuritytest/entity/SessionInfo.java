package com.sct.mailsecurityscanserver.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SessionInfo implements Serializable {

    private String sessionId;
    private long createDate;
    private long lastAccessDate;
    private long timeOutInterval;

    private Map<String, String> keyData = null;

    public SessionInfo() {
        createDate = new Date().getTime();
        lastAccessDate = new Date().getTime();
        timeOutInterval = 24*60*60*1000;
        keyData = new HashMap<String, String>();
    }

    public SessionInfo(String sessionStr) {
        JSONObject json = (JSONObject) JSON.parseObject(sessionStr);
        sessionId = String.valueOf(json.get("SESSION_ID"));
        createDate = Long.parseLong(String.valueOf(json.get("CREATE_DATE")));
        lastAccessDate = Long.parseLong(String.valueOf(json.get("LAST_ACCESS_DATE")));
        timeOutInterval = Long.parseLong(String.valueOf(json.get("TIME_OUT_INTERVAL")));

        String keyDataStr = String.valueOf(json.get("KEY_DATA"));
        keyData = new HashMap<String, String>();
        JSONObject mapJson = (JSONObject) JSON.parseObject(keyDataStr);
        for (Map.Entry<String, Object> entry : mapJson.entrySet()) {
            keyData.put(entry.getKey(), (String)entry.getValue());
        }
    }

    public String toString() {
        String sessionStr=null;
        JSONObject json = new JSONObject();
        json.put("SESSION_ID", sessionId);
        json.put("CREATE_DATE", Long.toString(createDate));
        json.put("LAST_ACCESS_DATE", Long.toString(lastAccessDate));
        json.put("TIME_OUT_INTERVAL", Long.toString(timeOutInterval));

        JSONObject mapJson=new JSONObject();
        Iterator iter = keyData.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            mapJson.put((String)entry.getKey(), (String)entry.getValue());
        }
        json.put("KEY_DATA", mapJson.toJSONString());

        sessionStr=json.toJSONString();
        return sessionStr;
    }

    public void updateAccessDate() {
        lastAccessDate = new Date().getTime();
    }

    public boolean isSessionTimeOut() {
        if (timeOutInterval > 0) {
            long temp = new Date().getTime() - lastAccessDate;
            if (temp <= timeOutInterval) {
                return true;
            }
        } else {
            return true;
        }
        return false;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createData) {
        this.createDate = createData;
    }

    public long getAccessDate() {
        return lastAccessDate;
    }

    public void setAccessDate(long lastAccessData) {
        this.lastAccessDate = lastAccessData;
    }

    public long getTimeOutInterval() {
        return timeOutInterval;
    }

    public void setTimeOutInterval(long timeOutInterval) {
        this.timeOutInterval = timeOutInterval;
    }

    public void setAttribute(String key, String obj) {
        updateAccessDate();
        if (key != null && !key.isEmpty()) {
            keyData.put(key, obj);
        }
    }

    public String getAttribute(String key) {
        updateAccessDate();
        if (key != null && !key.isEmpty()) {
            return keyData.get(key);
        }
        return null;
    }

    public void removeAttribute(String key) {
        updateAccessDate();
        if (key != null && !key.isEmpty()) {
            keyData.remove(key);
        }
    }

}
