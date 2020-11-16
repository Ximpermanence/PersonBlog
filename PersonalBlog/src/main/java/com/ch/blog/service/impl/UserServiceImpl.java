package com.ch.blog.service.impl;

import com.ch.blog.dao.UserRepository;
import com.ch.blog.pojo.User;
import com.ch.blog.service.UserService;
import com.ch.blog.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: chenhao
 * @create:2020/10/27 9:51
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User checkUser(String username, String password) {

        User user = userRepository.findByUsernameAndPassword(username, MD5Util.code(password));
        return user;
    }
}
