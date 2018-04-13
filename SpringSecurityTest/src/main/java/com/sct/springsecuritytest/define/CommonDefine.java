package com.sct.mailsecurityscanserver.define;

public interface CommonDefine {       

    public static final int TERMINAL_VERSION_SERVICE_CMD = 100;
    public static final int DEVICE_SERVICE_CMD = 101;
    public static final int MAIL_STR_DATA_SERVICE_CMD = 102;
    public static final int MAIL_BYTE_DATA_SERVICE_CMD = 103;
    public static final int MANAGER_SERVICE_CMD = 104;
    public static final int OPERATOR_LOGIN_SERVICE_CMD = 105;
    public static final int OPERATOR_SERVICE_CMD = 106;
    
    public static final String PHONE_NUM_FORMAT_CHAR = "1234567890+";
    
    public static final int MAX_PHONE_NUM_LEN = 14;
    public static final int MIN_PHONE_NUM_LEN = 11;

    public static final Integer USER_ROLE_ADMIN = 0;
    public static final Integer USER_ROLE_MANAGER = 1;
    public static final Integer USER_ROLE_OPERATOR = 2;

    
}
