package com.ch.blog.service;

import com.ch.blog.pojo.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @description:
 * @author: chenhao
 * @create:2020/11/3 9:14
 **/
public interface CommentService {

    List<Comment> listCommentByBlogId(Long blogId);

    Comment saveComment(Comment comment);

    Page<Comment> pageComment(Pageable pageable);
}
