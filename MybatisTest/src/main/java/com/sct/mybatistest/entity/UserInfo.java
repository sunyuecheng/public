package com.sct.mybatistest.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_user_info")
public class UserInfo {
    @Id
    //@GeneratedValue(strategy = GenerationType.NONE)
    private String id;

    private String name;
    private String password;

    @Column(name = "real_name")
    private String realName;

    @Column(name = "id_card")
    private String idCard;

    @Column(name = "phone_num")
    private String phoneNum;

    @Column(name = "register_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date registerDate;

    @Column(name = "last_view_date")
    @Temporal(TemporalType.TIME)
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
