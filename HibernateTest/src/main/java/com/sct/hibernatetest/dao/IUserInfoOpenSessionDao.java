package com.sct.hibernatetest.dao;

import com.sct.hibernatetest.entity.UserInfo;
import org.hibernate.Session;

import java.util.List;

public interface IUserInfoOpenSessionDao {
    public boolean insertUserInfo(UserInfo userInfo);
    public boolean deleteUserInfoById(String id);
    public UserInfo findUserInfoById(String id);
    public boolean updateUserInfo(UserInfo userInfo);
    public Long getUserInfoCount();
    public List<UserInfo> getUserInfoListByLastViewDate(Long beginDate, Long endDate, int pageIndex, int pageSize);
}
