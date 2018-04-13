package com.sct.mailsecurityscanserver.util;

import com.sct.mailsecurityscanserver.struct.HttpResponseInfo;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpRequestUtil {    
    private Map<String, String> urlParams;
    private Map<String, String> httpPropertys;
    
    public HttpRequestUtil() {
    }
 
    public void setUrlParams(Map<String, String> urlParams) {
        this.urlParams = urlParams;
    }
    
    public void setHttpPropertys(Map<String, String> httpPropertys) {
        this.httpPropertys = httpPropertys;
    }

    public HttpResponseInfo sendGetRequest(String urlString) throws Exception {
        return send(urlString, "GET", urlParams, httpPropertys, null);
    }

    public HttpResponseInfo sendPostRequest(String urlString, byte[] data) throws Exception {
        return send(urlString, "POST", urlParams, httpPropertys, data);
    }
    
    private HttpResponseInfo send(String urlString, String method,
                                  Map<String, String> urlParams, Map<String, String> httpPropertys, byte[] data) throws Exception {
        HttpURLConnection urlConnection = null;

        if ( urlParams != null) {
            StringBuffer params = new StringBuffer();
            int i = 0;
            for (String key : urlParams.keySet()) {
                if (i == 0) {
                    params.append("?");
                } else {
                    params.append("&");
                }
                params.append(key).append("=").append(urlParams.get(key));
                i++;
            }
            urlString += params;
        }
        
        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod(method);
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setUseCaches(false);
        urlConnection.setConnectTimeout(5000);
        urlConnection.setReadTimeout(5000);

        if (httpPropertys != null) {
            for (String key : httpPropertys.keySet()) {
                urlConnection.addRequestProperty(key, httpPropertys.get(key));
            }
        }

        if (method.equalsIgnoreCase("POST") && data != null) {
            urlConnection.getOutputStream();
            urlConnection.getOutputStream().write(data);
            urlConnection.getOutputStream().flush();
            urlConnection.getOutputStream().close();
        }

        return readReturnContent(urlString, urlConnection);
    }

    private HttpResponseInfo readReturnContent(String urlString, HttpURLConnection urlConnection) throws Exception {
        HttpResponseInfo httpResponseInfo = new HttpResponseInfo();
        try {
            httpResponseInfo.setResponseCode(urlConnection.getResponseCode());
            httpResponseInfo.setFile(urlConnection.getURL().getFile());
            httpResponseInfo.setHost(urlConnection.getURL().getHost());
            httpResponseInfo.setPath(urlConnection.getURL().getPath());
            httpResponseInfo.setProtocol(urlConnection.getURL().getProtocol());
            httpResponseInfo.setQuery(urlConnection.getURL().getQuery());
            httpResponseInfo.setRef(urlConnection.getURL().getRef());
            httpResponseInfo.setUserInfo(urlConnection.getURL().getUserInfo());

            httpResponseInfo.setResponseMessage(urlConnection.getResponseMessage());
            httpResponseInfo.setContentType(urlConnection.getContentType());
            httpResponseInfo.setRequestMethod(urlConnection.getRequestMethod());
            httpResponseInfo.setConnectTimeout(urlConnection.getConnectTimeout());
            httpResponseInfo.setReadTimeout(urlConnection.getReadTimeout());

            InputStream inputStream = null;
            if(urlConnection.getResponseCode()!= HttpResponseInfo.HTTP_OK) {
                inputStream = urlConnection.getErrorStream();
            } else {
                inputStream = urlConnection.getInputStream();
            }

            int totalReadBufferSize=0;
            int readBufferSize=1024;
            int readedBufferSize=0;
            byte[] readBuffer=new byte[readBufferSize];
            byte[] totalReadBuffer=null;
            while((readedBufferSize=inputStream.read(readBuffer,0,readBufferSize))!=-1) {
                byte[] tempBuffer=totalReadBuffer;
                totalReadBuffer = new byte[totalReadBufferSize+readedBufferSize];
                if(tempBuffer!=null) {
                    System.arraycopy(tempBuffer, 0, totalReadBuffer, 0, totalReadBufferSize);
                }
                System.arraycopy(readBuffer,0,totalReadBuffer,totalReadBufferSize,readedBufferSize);
                totalReadBufferSize+=readedBufferSize;
            }

            if(totalReadBuffer!=null&&totalReadBufferSize>0) {
                httpResponseInfo.setContent(totalReadBuffer);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        return httpResponseInfo;
    }
            
}
