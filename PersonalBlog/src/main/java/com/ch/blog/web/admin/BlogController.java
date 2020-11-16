package com.ch.blog.web.admin;

import com.ch.blog.pojo.Blog;
import com.ch.blog.pojo.User;
import com.ch.blog.service.BlogService;
import com.ch.blog.service.TagService;
import com.ch.blog.service.TypeService;
import com.ch.blog.vo.BlogQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.expression.Ids;

import javax.persistence.Id;
import javax.servlet.http.HttpSession;

/**
 * @description:
 * @author: chenhao
 * @create:2020/10/27 13:45
 **/
@Controller
@RequestMapping("/admin")
public class BlogController {

    private static final String INPUT = "admin/blogs-input";
    private static final String LIST = "admin/blogs";
    private static final String REDIRECT_LIST = "redirect:/admin/blogs";


    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @GetMapping("/blogs")
    public String blogs(@PageableDefault(size = 3, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        BlogQuery blog, Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("page", blogService.pageBlog(pageable, blog));
        return LIST;
    }

    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 3, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         BlogQuery blog, Model model) {
        model.addAttribute("page", blogService.pageBlog(pageable, blog));
//        返回该页面下的bloglist片段（就像导航栏一样的东西就是片段）,局部刷新
        return "/admin/blogs :: blogList";
    }


    @GetMapping("blogs/input")
    public String input(Model model) {
        setTypeAndTag(model);
        model.addAttribute("blog", new Blog());
        return INPUT;
    }

    private void setTypeAndTag(Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
    }

    @GetMapping("blogs/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        setTypeAndTag(model);
        Blog blog = blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog", blog);
        System.out.println("_____controller获取到的blog里的title和tag_______" + blog.getTitle() + blog.getTags() + blog.getTagIds());
        return INPUT;
    }

    /**
     * 添加和修改,后端也可以和前面写type一样加valid验证，先不加
     *
     * @param blog
     * @param attributes 用于在redirect时添加message
     * @param session    session对象里有user blog对象里需要用到
     * @return
     */
    @PostMapping("blogs")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session) {

        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));
        Blog b;

        if (blog.getId()==null) {
            b=blogService.saveBlog(blog);
        } else {
            b=blogService.updateBlog(blog.getId(),blog);
        }

        if (b == null) {
            attributes.addAttribute("message", "操作失败");
        } else {
            attributes.addAttribute("message", "操作成功");
        }

        return REDIRECT_LIST;
    }


    @GetMapping("blogs/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/blogs";
    }
}
