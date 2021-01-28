package com.zh.springsecurity.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zh.springsecurity.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends BaseMapper<User> {

}

