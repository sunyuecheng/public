package com.sct.springsecuritytest.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class EncryptDesUtil {
    private static final int DES_KEY_LEN = 24;
    private static final String KEY_ALGORITHM = "DESede";
    private static final String CIPHER_ALGORITHM = "DESede/ECB/NoPadding";

    public static byte[] makeKey() {
        byte[] key=new byte[DES_KEY_LEN];
        java.util.Random rand=new java.util.Random();
        for(int i=0;i<DES_KEY_LEN;i++) {
            key[i]=(byte)(rand.nextInt()%256);
        }
        return key;
    }

    public static byte[] encryptData(byte[] key,byte[] data) throws Exception {
        if(key==null||data==null) {
            return null;
        }
        byte[] tempData = null;
        if(data.length%8!=0) {
            tempData = new byte[(data.length/8+1)*8];
        } else {
            tempData = new byte[data.length];
        }
        System.arraycopy(data, 0, tempData, 0, data.length);

        SecretKey deskey = new SecretKeySpec(key, KEY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, deskey);

        return cipher.doFinal(tempData);
    }

    public static byte[] decryptData(byte[] key,byte[] data) throws Exception {
        if(key==null||data==null) {
            return null;
        }
        SecretKey deskey = new SecretKeySpec(key, KEY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, deskey);
        return cipher.doFinal(data);
    }
}  