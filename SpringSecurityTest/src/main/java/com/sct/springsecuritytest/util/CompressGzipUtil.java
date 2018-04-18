package com.sct.springsecuritytest.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class CompressGzipUtil {
    public static byte[] compressData(byte[] data) throws Exception {
        if(data==null) {
            return null;
        }
        ByteArrayOutputStream outPut = new ByteArrayOutputStream();   
        GZIPOutputStream gos = new GZIPOutputStream(outPut);
        gos.write(data, 0, data.length);
        gos.flush();
        gos.close();
  
        byte[] output = outPut.toByteArray();
  
        outPut.flush();  
        outPut.close();  
  
        return output;
    }
    
    public static byte[] decompressData(byte[] data) throws Exception {
        if(data==null) {
            return null;
        }
        
        ByteArrayInputStream inPut = new ByteArrayInputStream(data);
        GZIPInputStream gis = new GZIPInputStream(inPut);  
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        int tempDataLen =  0;
        byte tempData[] = new byte[1024];
        while ((tempDataLen = gis.read(tempData, 0, 1024)) != -1) {
            baos.write(tempData, 0, tempDataLen);
        }
        gis.close();  
        
        byte [] output = baos.toByteArray();
        baos.flush();  
        baos.close(); 
  
        return output;
    }
}
