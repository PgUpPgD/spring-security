package com.zh.springsecurity.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zh.springsecurity.entity.User;
import org.springframework.stereotype.Component;

@Component
public interface LoginService extends BaseMapper<User> {
}
