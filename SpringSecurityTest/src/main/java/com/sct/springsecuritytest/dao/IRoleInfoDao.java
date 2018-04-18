package com.sct.springsecuritytest.dao;

import com.sct.springsecuritytest.entity.RoleInfo;

import java.util.List;

public interface IRoleInfoDao {
    List<RoleInfo> selectByUserId(String id);
}