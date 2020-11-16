package com.ch.blog.service;

import com.ch.blog.pojo.Blog;
import com.ch.blog.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: chenhao
 * @create:2020/10/28 10:17
 **/
public interface BlogService {

    Blog getBlog(Long id);

    Blog getAndConvert(Long id);

    Page<Blog> pageBlog(Pageable pageable, BlogQuery blog);

    Page<Blog> pageBlog(Pageable pageable);

    Page<Blog> pageBlog(Long tagId, Pageable pageable);

    Page<Blog> pageBlog(String query, Pageable pageable);

    /**
     * 推荐的前size条博客
     *
     * @param size
     * @return
     */
    List<Blog> listRecommendBlogTop(Integer size);

    Map<String,List<Blog>> archiveBlog();

    Long countBlog();

    Blog saveBlog(Blog blog);

    Blog updateBlog(Long id, Blog blog);

    void deleteBlog(Long id);
}
