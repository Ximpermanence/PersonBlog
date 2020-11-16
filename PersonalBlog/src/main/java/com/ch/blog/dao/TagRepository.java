package com.ch.blog.dao;

import com.ch.blog.pojo.Tag;
import com.ch.blog.pojo.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @description:
 * @author: chenhao
 * @create:2020/10/27 14:24
 **/
public interface TagRepository extends JpaRepository<Tag,Long> {

    Tag findByName(String name);
    @Query("select t from t_tag t")
    List<Tag> findTop(Pageable pageable);
}
