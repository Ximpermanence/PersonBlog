package com.ch.blog.web.admin;

import com.ch.blog.pojo.Blog;
import com.ch.blog.pojo.Tag;
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
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * 默认分页时查10条，按照id来倒叙
     *
     * @param pageable
     * @return
     */
    @GetMapping("/tags")
    public String list(@PageableDefault(size = 3, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        //前端页面可以获取到分页对象
        model.addAttribute("page", tagService.pageTag(pageable));
        return "admin/tags";
    }


    @GetMapping("/tags/input")
    public String input(Model model) {
        model.addAttribute("tag", new Tag());
        return "admin/tags-input";
    }

    /**
     * @param id
     * @param model
     * @return
     * @PathVariable使得id一致
     */
    @GetMapping("/tags/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        model.addAttribute("tag", tagService.getTag(id));
        return "admin/tags-input";
    }

    /**
     * 新增
     *
     * @param tags
     * @param result
     * @param attributes
     * @return
     */
    @PostMapping("/tags")
    public String post(@Valid Tag tags, BindingResult result, RedirectAttributes attributes) {
        Tag Tag1 = tagService.getTagByName(tags.getName());

        if (!Objects.isNull(Tag1)) {
            result.rejectValue("name", "nameError", "不能重复添加分类");
        }

        if (result.hasErrors()) {
            return "admin/tags-input";
        }
        Tag t = tagService.saveTag(tags);
        if (t == null) {
            attributes.addFlashAttribute("message", "新增失败");
            return "";
        } else {
            attributes.addFlashAttribute("message", "新增成功");
            return "redirect:/admin/tags";
        }
    }

    /**
     * 更新
     * @param tag
     * @param result
     * @param id
     * @param attributes
     * @return
     */
    @PostMapping("/tags/{id}")
    public String editPost(@Valid Tag tag, BindingResult result, @PathVariable Long id, RedirectAttributes attributes) {

        Tag Tag1 = tagService.getTagByName(tag.getName());
        if (!Objects.isNull(Tag1)) {
            result.rejectValue("name", "nameError", "不能重复添加分类");
        }
        if (result.hasErrors()) {
            return "admin/tags-input";
        }
        Tag t = tagService.updateTag(id, tag);
        if (t == null) {
            attributes.addFlashAttribute("message", "修改失败");
            return "";
        } else {
            attributes.addFlashAttribute("message", "修改成功");
            return "redirect:/admin/tags";
        }
    }

    @GetMapping("/tags/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes){
        Tag tag = tagService.getTag(id);
        if(CollectionUtils.isEmpty(tag.getBlogs())){
            tagService.deleteTag(id);
            attributes.addFlashAttribute("message", "删除成功");
            return "redirect:/admin/tags";
        }
        List<Blog> blogList = tag.getBlogs();
        StringBuilder blognames = new StringBuilder();
        blogList.forEach(t-> blognames.append(t.getTitle()));
        attributes.addFlashAttribute("error", "删除失败:如下博客涉及到此标签"+"\r"+new String(blognames));
        return "redirect:/admin/tags";
    }

}
