package com.sct.springsecuritytest.service.impl;

import com.sct.springsecuritytest.dao.IRoleInfoDao;
import com.sct.springsecuritytest.dao.IUserInfoDao;
import com.sct.springsecuritytest.entity.RoleInfo;
import com.sct.springsecuritytest.entity.UserInfo;
import com.sct.springsecuritytest.security.UserSecurityInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = Logger.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private IUserInfoDao userInfoDao;

    @Autowired
    private IRoleInfoDao roleInfoDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo userInfo = userInfoDao.selectUserInfoByName(username);

        if (userInfo == null) {
            throw new UsernameNotFoundException(String.format("No user found with username(%s).", username));
        } else {
            List<RoleInfo> roleInfoList = roleInfoDao.selectByUserId(userInfo.getId());
            return new UserSecurityInfo(userInfo, roleInfoList);
        }
    }


}
