package com.ch.blog.web;

import com.ch.blog.pojo.Tag;
import com.ch.blog.service.BlogService;
import com.ch.blog.service.TagService;
import com.ch.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @description:
 * @author: chenhao
 * @create:2020/11/3 15:38
 **/
@Controller
public class TagShowController {

    @Autowired
    private TagService tagservice;

    @Autowired
    private BlogService blogService;

    @GetMapping("/tags/{id}")
    public String tags(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                       @PathVariable Long id, Model model) {

        List<Tag> tags = tagservice.lsitTagTop(10000);
        if (id == -1) {
            id = tags.get(0).getId();
        }
        model.addAttribute("tags", tags);
        model.addAttribute("page", blogService.pageBlog(id, pageable));
        model.addAttribute("activeTagId", id);
        return "tags";
    }
}
