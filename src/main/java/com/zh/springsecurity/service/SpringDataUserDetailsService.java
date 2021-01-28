package com.zh.springsecurity.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zh.springsecurity.dao.LoginService;
import com.zh.springsecurity.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class SpringDataUserDetailsService implements UserDetailsService {
    
    @Autowired
    private LoginService loginService;

    //根据 账号查询用户信息
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = loginService.selectOne(Wrappers.<User>query().lambda().eq(User::getUsername, username));
        //将来连接数据库根据账号查询用户信息
        if(StringUtils.isEmpty(user)){
            throw new UsernameNotFoundException("用户名不存在");
        }
        //查询用户的权限
        String[] permissions = new String[]{"A"};
        String permission = user.getPermission();
        if (permission != null && !"".equals(permission)){
            permissions = permission.split(",");
        }
        return org.springframework.security.core.userdetails
                .User.withUsername(user.getUsername()).password(user.getPassword()).authorities(permissions).build();
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode("123456");
        System.out.println(encode);
    }

}

