package com.ch.blog.dao;

import com.ch.blog.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @description:
 * @author: chenhao
 * @create:2020/10/27 9:52
 **/
//操作对象和主键类型
public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsernameAndPassword(String username,String password);
}
