package com.sct.mailsecurityscanserver.util;

import java.security.MessageDigest;  

public class EncodeMd5Util {
    public static String makeMd5(String data) throws Exception  {
        if(data==null) {
            return null;
        } 
        MessageDigest md = MessageDigest.getInstance("MD5");  
        md.update(data.getBytes());
        byte buffer[] = md.digest();

        int i;   
        StringBuffer strBuf = new StringBuffer("");
        for (int offset = 0; offset < buffer.length; offset++) {
            i = buffer[offset];
            if (i < 0)  
                i += 256;  
            if (i < 16)  
                strBuf.append("0");
            strBuf.append(Integer.toHexString(i));
        }  
        return strBuf.toString();
    }
}
