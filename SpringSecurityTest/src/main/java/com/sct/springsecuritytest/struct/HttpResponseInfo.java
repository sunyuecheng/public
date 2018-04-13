package com.sct.mailsecurityscanserver.struct;

public class HttpResponseInfo {
    private String file = ""; 
    private String host = ""; 
    private String path = ""; 
    private String protocol = ""; 
    private String query = "";   
    private String ref = "";   
    private String userInfo = ""; 
    private int responseCode = 0; 
    private String responseMessage = ""; 
    private String contentType = ""; 
    private String requestMethod = ""; 
    private int connectTimeout = 0; 
    private int readTimeout = 0; 
    private byte[] content=null;
    
    public static final int HTTP_OK = 200;

    public String getFile() {
        return file;
    }

    public void setFile(String sFile) {
        this.file = sFile;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String sHost) {
        this.host = sHost;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String sPath) {
        this.path = sPath;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String sProtocol) {
        this.protocol = sProtocol;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String sQuery) {
        this.query = sQuery;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String sRef) {
        this.ref = sRef;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String sUserInfo) {
        this.userInfo = sUserInfo;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int iResponseCode) {
        this.responseCode = iResponseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String sResponseMessage) {
        this.responseMessage = sResponseMessage;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String sContentType) {
        this.contentType = sContentType;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String sRequestMethod) {
        this.requestMethod = sRequestMethod;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int iConnectTimeout) {
        this.connectTimeout = iConnectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int iReadTimeout) {
        this.readTimeout = iReadTimeout;
    }
    
    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] btContent) {
        this.content = btContent;
    }
}
