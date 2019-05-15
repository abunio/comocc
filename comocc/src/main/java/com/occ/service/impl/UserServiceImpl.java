package com.occ.service.impl;

import com.occ.dao.mapper.UserMapper;
import com.occ.entity.User;
import com.occ.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;

    public List<User> getUsers(User user) {

        return userMapper.selectByUser(user);

    }
}
