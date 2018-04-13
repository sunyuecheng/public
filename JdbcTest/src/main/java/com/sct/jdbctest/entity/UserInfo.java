package com.sct.jdbctest.entity;

import com.sct.jdbctest.dao.util.Column;

import java.util.Date;

public class UserInfo {
    @Column("id")
    private String id;

    @Column("name")
    private String name;

    @Column("password")
    private String password;

    @Column("real_name")
    private String realName;

    @Column("id_card")
    private String idCard;

    @Column("phone_num")
    private String phoneNum;

    @Column("register_date")
    private Date registerDate;

    @Column("last_view_date")
    private Date lastViewDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Date getLastViewDate() {
        return lastViewDate;
    }

    public void setLastViewDate(Date lastViewDate) {
        this.lastViewDate = lastViewDate;
    }
}
