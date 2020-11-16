package com.ch.blog.web.admin;

import com.ch.blog.pojo.Blog;
import com.ch.blog.pojo.Tag;
import com.ch.blog.service.CommentService;
import com.ch.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * @description:
 * @author: chenhao
 * @create:2020/10/27 14:31
 **/
@Controller
@RequestMapping("/admin")
public class CommentController {


    @Autowired
    private CommentService commentService;

    /**
     * 默认分页时查10条，按照id来倒叙
     *
     * @param pageable
     * @return
     */
    @GetMapping("/comments")
    public String list(@PageableDefault(size = 3, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        //前端页面可以获取到分页对象
        model.addAttribute("page", commentService.pageComment(pageable));
        return "admin/comments";
    }

}
