package com.ch.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.ch.blog.NotFoundException;
import com.ch.blog.dao.BlogRepository;
import com.ch.blog.pojo.Blog;
import com.ch.blog.pojo.Type;
import com.ch.blog.service.BlogService;
import com.ch.blog.util.MarkdownUtil;
import com.ch.blog.vo.BlogQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.*;

/**
 * @description:
 * @author: chenhao
 * @create:2020/10/28 10:17
 **/
@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;


    @Override
    public Blog getBlog(Long id) {
        Blog blog = blogRepository.getOne(id);
        System.out.println("____________________________________" + blog.getTitle());
        return blog;
    }

    @Transactional
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = blogRepository.getOne(id);
        if (blog == null) {
            throw new NotFoundException("资源不存在");
        }

        //content也会变得和返回值一样,新new对象是防止可能会直接操作数据库
        Blog b = new Blog();
        BeanUtils.copyProperties(blog, b);
        String content = MarkdownUtil.markdownToHtmlExtensions(b.getContent());
        b.setContent(content);
        blogRepository.updateViews(id);
        return b;
    }

    /**
     * 分页动态查值
     *
     * @param pageable
     * @param blog
     * @return
     */
    @Override
    public Page<Blog> pageBlog(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

                //将查询条件放入predicates
                List<Predicate> predicates = new ArrayList<>();
                if (StringUtils.isNotEmpty(blog.getTitle())) {
                    //like方法，不过条件还是要拼接下
                    predicates.add(cb.like(root.<String>get("title"), "%" + blog.getTitle() + "%"));
                }
                if (blog.getTypeId() != null) {
                    predicates.add(cb.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
                }
                if (blog.isRecommend()) {
                    predicates.add(cb.equal(root.<Boolean>get("recommend"), blog.isRecommend()));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    @Override
    public Page<Blog> pageBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public Page<Blog> pageBlog(Long tagId, Pageable pageable) {

        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                //关联对象
                Join join = root.join("tags");
                return cb.equal(join.get("id"), tagId);
            }
        }, pageable);
    }

    @Override
    public Page<Blog> pageBlog(String query, Pageable pageable) {

        return blogRepository.findByQuery(query, pageable);
    }

    /**
     * 推荐的前size条博客
     *
     * @param size
     * @return
     */
    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        Pageable pageable = PageRequest.of(0, size, sort);

        return blogRepository.findTop(pageable);
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {

        List<String> years = blogRepository.findGroupYear();
        Map<String, List<Blog>> map = new HashMap<>();
        for (String year : years) {
            map.put(year, blogRepository.findByYear(year));
        }
        return map;
    }

    @Override
    public Long countBlog() {

        return blogRepository.count();
    }

    /**
     * 新增时初始化blog
     *
     * @param blog
     * @return
     */
    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if (blog.getId() == null) {
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        } else {
            blog.setUpdateTime(new Date());
        }

        return blogRepository.save(blog);
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = blogRepository.getOne(id);
        if (b == null) {
            throw new NotFoundException("该博客不存在");
        }

        //过滤，将前端传过来的blog中为空（即之前没有传给前端的属性在copy时不覆盖）
        BeanUtil.copyProperties(blog, b, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        b.setUpdateTime(new Date());
        return blogRepository.save(b);
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }
}
