package com.sct.springsecuritytest.security;

import org.springframework.security.core.GrantedAuthority;

public class RoleSecurityInfo implements GrantedAuthority {
    private Integer id;

    private String name;

    private String authority;

    public RoleSecurityInfo(Integer id, String name, String authority) {
        this.id = id;
        this.name = name;
        this.authority = authority;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority == null ? null : authority.trim();
    }
}