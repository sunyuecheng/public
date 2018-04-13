package com.sct.jdbctest.dao;

import com.sct.jdbctest.entity.UserInfo;

import java.util.List;

public interface IUserInfoDao {
    public boolean insertUserInfo(UserInfo userInfo);
    public boolean deleteUserInfoById(String id);
    public UserInfo findUserInfoById(String id);
    public Long getUserInfoCount();
    public List<UserInfo> getUserInfoListByLastViewDate(Long beginDate, Long endDate, int pageIndex, int pageSize);
}
