package com.sct.springsecuritytest.filter;


import com.alibaba.fastjson.JSONObject;
import com.sct.springsecuritytest.commond.ServerCommondInfo;
import com.sct.springsecuritytest.initialize.InitializeData;
import com.sct.springsecuritytest.security.RoleSecurityInfo;
import com.sct.springsecuritytest.security.UserSecurityInfo;
import com.sct.springsecuritytest.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.sct.springsecuritytest.define.CommonDefine.*;
import static com.sct.springsecuritytest.define.ErrorCodeDefine.RESULT_USER_AUTHENTICATION_ERROR;

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {
        String authHeader = request.getHeader(HEADER_STRING);
        if (authHeader != null && authHeader.startsWith(TOKEN_PREFIX)) {
            final String authToken = authHeader.substring(TOKEN_PREFIX.length());

            try {
                Claims claims = JwtTokenUtil.parseToken(authToken, InitializeData.getTokenSecretKey());
                String id = (String) claims.get("ID");
                String name = (String) claims.get("NAME");
                String realName = (String) claims.get("REAL_NAME");
                String idCard = (String) claims.get("ID_CARD");
                String phoneNum = (String) claims.get("PHONE_NUM");
                Date registerDate = new Date((Long)claims.get("REGISTER_DATE"));
                Integer status = (Integer) claims.get("STATUS");
                List<RoleSecurityInfo> roleSecurityInfoList =(List<RoleSecurityInfo>)claims.get("ROLE_SECURITY_INFO_LIST");

                UserSecurityInfo userSecurityInfo = new UserSecurityInfo();
                userSecurityInfo.setId(id);
                userSecurityInfo.setName(name);
                userSecurityInfo.setRealName(realName);
                userSecurityInfo.setIdCard(idCard);
                userSecurityInfo.setPhoneNum(phoneNum);
                userSecurityInfo.setRegisterDate(registerDate);
                userSecurityInfo.setStatus(status);
                userSecurityInfo.setRoleSecurityInfoList(roleSecurityInfoList);

                if (SecurityContextHolder.getContext().getAuthentication() == null) {

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userSecurityInfo, null);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(
                            request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            } catch (Exception e) {
                ServerCommondInfo serverCommondInfo = new ServerCommondInfo();
                serverCommondInfo.setCmd(SYSTEM_ERROR_SERVICE_CMD);
                serverCommondInfo.setType(0);
                serverCommondInfo.setCode(RESULT_USER_AUTHENTICATION_ERROR);
                serverCommondInfo.setMsg(e.getMessage());

                String serverCommondInfoStr = JSONObject.toJSONString(serverCommondInfo);

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json; charset=utf-8");

                PrintWriter printWriter = null;
                try {
                    printWriter = response.getWriter();
                    printWriter.append(serverCommondInfoStr);
                } catch (IOException eq) {
                    eq.printStackTrace();
                } finally {
                    if (printWriter != null) {
                        printWriter.close();
                    }
                }
                return;
            }
        }

        chain.doFilter(request, response);
    }
}

