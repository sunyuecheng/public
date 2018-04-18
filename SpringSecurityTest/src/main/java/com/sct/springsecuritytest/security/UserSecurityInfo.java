package com.sct.springsecuritytest.security;

import com.sct.springsecuritytest.entity.RoleInfo;
import com.sct.springsecuritytest.entity.UserInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class UserSecurityInfo implements UserDetails {

    private String id;

    private String name;

    private String password;

    private String realName;

    private String idCard;

    private String phoneNum;

    private Date registerDate;

    private Integer status;

    private List<RoleSecurityInfo> roleSecurityInfoList;

    public UserSecurityInfo(UserInfo userInfo,  List<RoleInfo> roleInfoList) {
        this.id = userInfo.getId();
        this.name = userInfo.getName();
        this.password = userInfo.getPassword();
        this.realName = userInfo.getRealName();
        this.idCard = userInfo.getIdCard();
        this.phoneNum = userInfo.getPhoneNum();
        this.registerDate = userInfo.getRegisterDate();
        this.status = userInfo.getStatus();
        this.roleSecurityInfoList = new ArrayList<>();
        if(roleInfoList!=null) {
            for (RoleInfo roleInfo : roleInfoList) {
                this.roleSecurityInfoList.add(new RoleSecurityInfo(roleInfo.getId(),
                        roleInfo.getName(), roleInfo.getAuthority()));
            }
        }
    }

    public UserSecurityInfo() {
        super();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roleSecurityInfoList;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return status==1;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status==1;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return status==1;
    }

    @Override
    public boolean isEnabled() {
        return status==1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<RoleSecurityInfo> getRoleSecurityInfoList() {
        return roleSecurityInfoList;
    }

    public void setRoleSecurityInfoList(List<RoleSecurityInfo> roleSecurityInfoList) {
        this.roleSecurityInfoList = roleSecurityInfoList;
    }
}