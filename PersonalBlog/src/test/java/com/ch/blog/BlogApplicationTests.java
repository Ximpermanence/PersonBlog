package com.ch.blog;

import com.ch.blog.pojo.Blog;
import com.ch.blog.service.BlogService;
import com.ch.blog.service.TypeService;
import com.ch.blog.util.MD5Util;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

@SpringBootTest
@Component
class BlogApplicationTests {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Test
    void contextLoads() {
    }

    @Test
    void TestMD5(){
       String a = MD5Util.code("123456");
        System.out.println(1);
    }

    @Test
    void Test1(){
       Blog blog =  blogService.getBlog(31L);
//        List<Blog> blogs = blogRepository.findAll();
        System.out.println(blog.getTags());
    }
}
