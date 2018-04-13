package com.sct.mailsecurityscanserver.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

@Component
public class DeferredResultPool<T> {
    private int maxPoolSize=0;
    private Map<String,DeferredResult<T>> deferredResultMap=new HashMap<String,DeferredResult<T>>();

    public DeferredResultPool(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }
    
    public boolean checkPoolStatus() {
        return deferredResultMap.size()<=maxPoolSize;
    }
    
    public String addDeferredResult(DeferredResult<T> result) {
        if(result==null) {
            return null;
        }
        String key = UUID.randomUUID().toString().replaceAll("-", "");
        deferredResultMap.put(key, result);
        return key;        
    }
    
    public DeferredResult<T> getDeferredResult(String key) {
        if(key==null) {
            return null;
        }
        DeferredResult<T> result=deferredResultMap.get(key);
        deferredResultMap.remove(key);
        return result;        
    }
}
