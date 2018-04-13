package com.sct.mybatistest.service.impl;

import com.sct.mybatistest.dao.impl.UserInfoCurrentSessionTxDao;
import com.sct.mybatistest.entity.UserInfo;
import com.sct.mybatistest.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service("UserInfoService")
public class UserInfoService implements IUserInfoService{

    @Autowired
    private UserInfoCurrentSessionTxDao userInfoCurrentSessionTxDao;

    @Transactional(propagation= Propagation.REQUIRES_NEW,
            isolation= Isolation.READ_COMMITTED,
            readOnly=true, timeout=3)
    public int doSomeThing() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId("8");
        userInfo.setName("test9");
        userInfo.setRegisterDate(new Date());
        userInfoCurrentSessionTxDao.insertUserInfo(userInfo);

        return 0;
    }
}
