package com.ch.blog.dao;

import com.ch.blog.pojo.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @description:
 * @author: chenhao
 * @create:2020/10/28 10:23
 **/
public interface BlogRepository extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {

    @Query("select b from t_blog b where b.recommend = true")
    List<Blog> findTop(Pageable pageable);

    /**
     * ?1代表第一个参数，如果需要第二个参数，就是?2
     *
     * @param query
     * @param pageable
     * @return
     */
    @Query("select b from t_blog b where b.title like ?1 or b.content like ?1")
    Page<Blog> findByQuery(String query, Pageable pageable);

    /**
     * 加上modifying才可以修改
     *
     * @param id
     * @return
     */
    @Transactional
    @Modifying
    @Query("update t_blog b set b.views=b.views+1 where b.id=?1")
    int updateViews(Long id);


    //    @Query("select function('date_format',b.updateTime,'%Y') as year from t_blog b group by year order by year desc ")
//    @Query("select function('date_format',b.updateTime,'%Y') as year from t_blog b group by year order by b.updateTime desc ")
    @Query("select function('date_format',b.updateTime,'%Y') as year from t_blog b group by function('date_format',b.updateTime,'%Y') order by b.updateTime desc ")
    List<String> findGroupYear();

    @Query("select b from t_blog b WHERE function('date_format',b.updateTime,'%Y') = ?1")
    List<Blog> findByYear(String year);
}
