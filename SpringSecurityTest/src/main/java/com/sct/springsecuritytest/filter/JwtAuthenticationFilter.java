package com.sct.springsecuritytest.filter;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sct.springsecuritytest.commond.ClientCommondInfo;
import com.sct.springsecuritytest.commond.ServerCommondInfo;
import com.sct.springsecuritytest.commond.request.UserLoginCmdInfo;
import com.sct.springsecuritytest.initialize.InitializeData;
import com.sct.springsecuritytest.security.RoleSecurityInfo;
import com.sct.springsecuritytest.security.UserSecurityInfo;
import com.sct.springsecuritytest.util.JwtTokenUtil;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static com.sct.springsecuritytest.define.CommonDefine.*;
import static com.sct.springsecuritytest.define.ErrorCodeDefine.*;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final Logger logger = Logger.getLogger(JwtAuthenticationFilter.class);

    public JwtAuthenticationFilter(String url, AuthenticationManager authManager) {
        super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest,
                                                HttpServletResponse httpServletResponse)
            throws AuthenticationException, IOException, ServletException {
        ClientCommondInfo clientCommondInfo = new ObjectMapper()
                .readValue(httpServletRequest.getInputStream(), ClientCommondInfo.class);
        if (clientCommondInfo == null) {
            throw new AuthenticationServiceException("Invaild request info.");
        }

        if (clientCommondInfo == null) {
            logger.error("Invaild request info.");
            throw new AuthenticationServiceException("Invaild request info.");
        }

        Integer type = clientCommondInfo.getType();
        if (type == null) {
            logger.error("Invaild cmd type.");
            throw new AuthenticationServiceException("Invaild cmd type.");
        }

        if (type == 0) {
            String data = clientCommondInfo.getData();
            UserLoginCmdInfo userLoginCmdInfo = null;
            if (data != null && !data.isEmpty()) {
                try {
                    userLoginCmdInfo =
                            JSONObject.parseObject(data, UserLoginCmdInfo.class);

                } catch (Exception e) {
                    logger.error("Convert data to object error, error info("+e.getMessage()+").");
                    throw new AuthenticationServiceException("Convert data to object error");
                }
                try {
                    return getAuthenticationManager().authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    userLoginCmdInfo.getUserName(),
                                    userLoginCmdInfo.getPwd(),
                                    Collections.<GrantedAuthority>emptyList()) );

                } catch (Exception e) {
                    logger.error("Authenticate user info error, error info("+e.getMessage()+").");
                    throw new AuthenticationServiceException(e.getMessage());
                }
            } else {
                logger.error("Error param!");
                throw new AuthenticationServiceException("Invaild cmd type.");
            }
        }


        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        //super.successfulAuthentication(request, response, chain, authResult);

        ServerCommondInfo serverCommondInfo = new ServerCommondInfo();
        serverCommondInfo.setCmd(USER_LOGIN_SERVICE_CMD);
        serverCommondInfo.setType(0);

        try {
            Map<String, Object> claimMap = new HashMap<>();
            UserSecurityInfo userSecurityInfo = (UserSecurityInfo)authResult.getPrincipal();
            if(userSecurityInfo == null) {
                throw new Exception("Invaild principal info.");
            }

            claimMap.put("ID", userSecurityInfo.getId());
            claimMap.put("NAME", userSecurityInfo.getName());
            claimMap.put("REAL_NAME", userSecurityInfo.getRealName());
            claimMap.put("ID_CARD", userSecurityInfo.getIdCard());
            claimMap.put("PHONE_NUM",userSecurityInfo.getPhoneNum());
            claimMap.put("REGISTER_DATE",userSecurityInfo.getRegisterDate());
            claimMap.put("STATUS",userSecurityInfo.getStatus());
            claimMap.put("ROLE_SECURITY_INFO_LIST",userSecurityInfo.getRoleSecurityInfoList());

            String jwtToken = JwtTokenUtil.createToken(null, claimMap, null, "Server",
                    InitializeData.getTokenTimeOutInterval(), InitializeData.getTokenSecretKey());

            response.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + jwtToken);
            serverCommondInfo.setCode(RESULT_OK);
        } catch (Exception e) {
            logger.error("Create jwt token error,error info("+e.getMessage()+").");
            serverCommondInfo.setCode(RESULT_CREATE_JWT_TOKEN_ERROR);
            serverCommondInfo.setMsg("Create jwt token error.");
        }
        String serverCommondInfoStr = JSONObject.toJSONString(serverCommondInfo);
        response.setContentType("application/json; charset=utf-8");

        PrintWriter printWriter = null;
        try {
            printWriter = response.getWriter();
            printWriter.append(serverCommondInfoStr);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed)
            throws IOException, ServletException {
        //super.unsuccessfulAuthentication(request, response, failed);

        ServerCommondInfo serverCommondInfo = new ServerCommondInfo();
        serverCommondInfo.setCmd(USER_LOGIN_SERVICE_CMD);
        serverCommondInfo.setType(0);
        serverCommondInfo.setCode(RESULT_USER_AUTHENTICATION_ERROR);
        serverCommondInfo.setMsg(failed.getMessage());

        String serverCommondInfoStr = JSONObject.toJSONString(serverCommondInfo);

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json; charset=utf-8");

        PrintWriter printWriter = null;
        try {
            printWriter = response.getWriter();
            printWriter.append(serverCommondInfoStr);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
    }
}