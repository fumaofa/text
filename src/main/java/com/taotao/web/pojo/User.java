package com.taotao.web.pojo;

import java.util.Date;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class User {

    private Long id;
    
    @NotBlank//验证字符串不能为null,且必须大于0
    @Length(min=6,max=20,message="用户名必须6~20个字符！")
    private String username;
    
    @NotBlank//验证字符串不能为null,且必须大于0
    @Length(min=6,max=20,message="密码必须6~20个字符！")
    @JsonIgnore //序列化json时忽略该字段
    private String password;
    
    @NotBlank
    @Length(min=11,max=11,message="手机号码不正确！")
    private String phone;
    
    private String email;

    private Date created;

    private Date updated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

}
