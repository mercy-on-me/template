package com.cy.pojo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @program: SSM
 * @description: User 实体类
 * @author: cy
 * @create: 2019-04-08 17:44
 **/
public class User implements Serializable {

    private String id;// int(11) NOT NULL AUTO_INCREMENT COMMENT '用户 ID',
    private String username;// varchar(64) NOT NULL COMMENT '用户姓名',
    private String sex;// varchar(32) NOT NULL COMMENT '性别',
    private String birthday;// timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '生日',
    private String address;// varchar(256) NOT NULL COMMENT '用户地址',
    private int  cat_id;// int(11) DEFAULT NULL COMMENT '汽车 ID',

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday='" + birthday + '\'' +
                ", address='" + address + '\'' +
                ", cat_id='" + cat_id + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }
}
