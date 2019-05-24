package com.occ.service.impl;

import com.alibaba.fastjson.JSON;
import com.occ.dao.mapper.UserMapper;
import com.occ.entity.User;
import com.occ.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    public List<User> getUsers(User user) {
        //redisTemplate.delete("users");
        Object users = redisTemplate.opsForValue().get("users");
        List<User> list = new ArrayList<User>();
        if(users==null){
            //查数据库
            list = userMapper.selectByUser(user);

            redisTemplate.opsForValue().set("users", JSON.toJSON(list),20, TimeUnit.SECONDS);

        }else{
            //启用缓存
            list = JSON.parseArray(JSON.toJSONString(users),User.class);
        }

        return list;
    }
}
