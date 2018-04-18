package com.sct.springsecuritytest.dao;

import com.sct.springsecuritytest.entity.UserInfo;

public interface IUserInfoDao {
    UserInfo selectUserInfoByName(String name);

    long countByName(String name);

    int insertUserInfo(UserInfo userInfo);

    int insertUserInfoSelective(UserInfo userInfo);

    UserInfo updateUserInfoByNameSelective(String name);
}