package com.sct.springsecuritytest.dao;

import com.sct.springsecuritytest.domain.UserRoleInfoKey;

public interface UserRoleInfoMapper {
    int deleteByPrimaryKey(UserRoleInfoKey key);

    int insert(UserRoleInfoKey record);

    int insertSelective(UserRoleInfoKey record);
}