package com.ch.blog.service;

import com.ch.blog.pojo.User;

/**
 * @description:
 * @author: chenhao
 * @create:2020/10/27 9:49
 **/
public interface UserService {

    /**
     * 检查登录
     * @param username
     * @param password
     * @return
     */
    User checkUser(String username,String password);

}
