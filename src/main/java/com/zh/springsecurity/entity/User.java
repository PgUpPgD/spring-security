package com.zh.springsecurity.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("user")
public class User extends Model<User> implements Serializable {
    private String id;
    private String username;
    private String password;
    private String fullname;
    private String permission;

    public User(){}

    public User(String id, String username, String password, String fullname, String permission) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.permission = permission;
    }
}
