package com.sct.mailsecurityscanserver.util;

import org.apache.commons.codec.binary.Base64;

public class EncodeBase64Util {
    public static String encodeData(byte[] data) {
        if(data==null) {
            return null;
        } 
        
        return new String(Base64.encodeBase64(data));
    }  
    public static byte[] decodeData(byte[] data) {
        if(data==null) {
            return null;
        } 
        
        return Base64.decodeBase64(data);
    }  
}
