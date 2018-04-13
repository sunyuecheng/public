package com.sct.mailsecurityscanserver.define;

public interface ErrorCodeDefine {
    public static final Integer RESULT_OK = 0;
    public static final Integer RESULT_ANALYSE_REQUEST_ERROR = 1;
    public static final Integer RESULT_PARAM_ERROR = 2;
    public static final Integer RESULT_SQL_ERROR = 4;
    public static final Integer RESULT_CONVERT_DATA_ERROR = 4;
    public static final Integer RESULT_INVALID_CMD_TYPE_ERROR = 5;
    public static final Integer RESULT_INVALID_APP_VERSION_ERROR = 5;
    public static final Integer RESULT_INVALID_TERMINAL_VERSION_ERROR = 5;
    public static final Integer RESULT_TERMINAL_NOT_EXIST_ERROR = 6;
    public static final Integer RESULT_TERMINAL_DISABLED_ERROR = 6;
    public static final Integer RESULT_DEVICE_DISABLED_ERROR = 6;
    public static final Integer RESULT_DEVICE_NOT_EXIST_ERROR = 6;
    public static final Integer RESULT_LOGIN_RANDOM_NUM_TIMEOUT_ERROR = 6;
    public static final Integer RESULT_OPERATOR_DISABLED_ERROR = 6;
    public static final Integer RESULT_USER_PERMISSION_ERROR = 9;
    public static final Integer RESULT_INVAILD_LOGIN_INFO_ERROR = 9;
    public static final Integer RESULT_LOGIN_TIMEOUT_ERROR = 9;
    public static final Integer RESULT_GET_MAIL_INFO_LIST_ERROR = 0;
    public static final Integer RESULT_SAVE_MAIL_INFO_LIST_ERROR = 0;

    public static final Integer RESULT_LOGIN_KEY_ERROR = 6;
    public static final Integer RESULT_TERMINAL_IS_EXISTED_ERROR = 7;
    public static final Integer RESULT_DEVICE_IS_EXISTED_ERROR = 9;
    public static final Integer RESULT_MANAGER_NOT_LOGIN_ERROR = 7;
    public static final Integer RESULT_MANAGER_DISABLED_ERROR = 9;

    public static final Integer RESULT_SESSION_INFO_TIMEOUT_ERROR = 6;
    public static final Integer RESULT_CREATE_SESSION_INFO_ERROR = 7;
    public static final Integer RESULT_READ_SESSION_INFO_ERROR = 8;
    public static final Integer RESULT_UPDATE_SESSION_INFO_ERROR = 9;
    public static final Integer RESULT_SET_CURRENT_TRAVEL_INFO_ERROR = 10;
    
    
}
