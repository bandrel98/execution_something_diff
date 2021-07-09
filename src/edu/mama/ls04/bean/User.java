package edu.mama.ls04.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

public class User {
    //品牌
    private String id;
    //型号
    private String mobile;
    //座数
    @JsonIgnore
    private String password;
    //上市时间
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
    private Date date;

    public User() {
    }

    public User(String id, String mobile, Date date) {
        this.id = id;
        this.mobile = mobile;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
