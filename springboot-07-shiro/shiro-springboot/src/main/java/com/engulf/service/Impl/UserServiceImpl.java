package com.engulf.service.Impl;

import com.engulf.mapper.UserMapper;
import com.engulf.pojo.User;
import com.engulf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserByName(String name) {
        return userMapper.getUserByName(name);
    }
}
