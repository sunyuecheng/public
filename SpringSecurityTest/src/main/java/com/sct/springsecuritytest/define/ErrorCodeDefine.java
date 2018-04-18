package com.sct.springsecuritytest.define;

public interface ErrorCodeDefine {
    public static final Integer RESULT_OK = 0;
    public static final Integer RESULT_ANALYSE_REQUEST_ERROR = 1;
    public static final Integer RESULT_PARAM_ERROR = 2;
    public static final Integer RESULT_SQL_ERROR = 4;
    public static final Integer RESULT_CONVERT_DATA_ERROR = 4;
    public static final Integer RESULT_INVALID_CMD_TYPE_ERROR = 5;
    public static final Integer RESULT_CREATE_JWT_TOKEN_ERROR = 5;
    public static final Integer RESULT_USER_AUTHENTICATION_ERROR = 5;
    
}
