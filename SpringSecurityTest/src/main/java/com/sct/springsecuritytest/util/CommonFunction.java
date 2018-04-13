package com.sct.mailsecurityscanserver.util;

public class CommonFunction {
    public static String buildPwd() {
        return "1111";
    }

    public static byte[] intToByteArray(int num) {
        return new byte[] {
            (byte) ((num >> 24) & 0xFF),
            (byte) ((num >> 16) & 0xFF),
            (byte) ((num >> 8) & 0xFF),
            (byte) (num & 0xFF)
        };
    }
}
