package com.sct.mailsecurityscanserver.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_operator_info")
public class OperatorInfo {
    public static final int SELF_OPERATOR = 0;
    public static final int COMPANY_OPERATOR = 1;

    public static final int ORGANIZATION_TYPE_COMPANY = 0;
    public static final int ORGANIZATION_TYPE_GOVERNMENT = 1;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id = null;

    @Column(name = "account")
    private String account =  null;

    @Column(name = "pwd")
    private String pwd =  null;

    @Column(name = "name")
    private String name =  null;

    @Column(name = "type")
    private Integer type = null;

    @Column(name = "organization_name")
    private String organizationName = null;

    @Column(name = "organization_type")
    private Integer organizationType = null;

    @Column(name = "department_name")
    private String departmentName = null;

    @Column(name = "phone_num")
    private String phoneNum = null;

    @Column(name = "register_date")
    private Date registerDate = null;

    @Column(name = "validity_date")
    private Date validityDate = null;

    @Column(name = "remark")
    private String remark = null;

    @Column(name = "work_status")
    private Integer workStatus = null;

    @Column(name = "register_manager_id")
    private Integer registerManagerId = null;

    public static int getSelfOperator() {
        return SELF_OPERATOR;
    }

    public static int getCompanyOperator() {
        return COMPANY_OPERATOR;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Integer getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(Integer organizationType) {
        this.organizationType = organizationType;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
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

    public Date getValidityDate() {
        return validityDate;
    }

    public void setValidityDate(Date validityDate) {
        this.validityDate = validityDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(Integer workStatus) {
        this.workStatus = workStatus;
    }

    public Integer getRegisterManagerId() {
        return registerManagerId;
    }

    public void setRegisterManagerId(Integer registerManagerId) {
        this.registerManagerId = registerManagerId;
    }
}
