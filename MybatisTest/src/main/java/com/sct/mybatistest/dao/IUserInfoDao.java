package com.sct.mybatistest.dao;

import com.sct.mybatistest.entity.UserInfo;
import com.sct.mybatistest.entity.UserRoleInfo;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface IUserInfoDao {
    UserInfo selectUserInfoById(String id);

    UserInfo selectUserInfoByName(String name);

    List<UserInfo> selectUserInfoList(@Param("registerDate") Date registerDate, @Param("startIndex") int startIndex, @Param("pageSize") int pageSize);

    UserRoleInfo selectUserRoleInfoByName(String name);

    long countByName(String name);

    int insertUserInfo(UserInfo userInfo);

    int insertUserInfoSelective(UserInfo userInfo);

    UserInfo updateUserInfoByNameSelective(String name);
}