package com.falltwo.web.admin;

import com.falltwo.pojo.Blog;
import com.falltwo.pojo.Tag;
import com.falltwo.pojo.Type;
import com.falltwo.pojo.User;
import com.falltwo.service.BlogService;
import com.falltwo.service.TagService;
import com.falltwo.service.TypeService;
import com.falltwo.vo.BlogQuery;
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

import javax.servlet.http.HttpSession;

/**
 * @author FallTwo
 * @description //TODO
 * @createTime  2022/2/18 13:50
 **/
@Controller
@RequestMapping("/admin")
public class BlogController {

    private static final String INPUT = "admin/blogs-input";
    private static final String LIST = "admin/blogs";
    private static final String REDIRECT_LIST = "redirect:/admin/blogs";

    @Autowired
    BlogService blogService;
    @Autowired
    TypeService typeService;
    @Autowired
    TagService tagService;
    /**
     * @author FallTwo
     * @description //TODO 博客管理列表
     * @createTime  2022/2/18 13:56
     * @param
     * @return
     **/
    @GetMapping("/blogs")
    public String blogs(@PageableDefault(size = 2,sort = {"updateTime"},direction = Sort.Direction.DESC)
                                    Pageable pageable, BlogQuery blog, Model model) {
        model.addAttribute("types",typeService.listType());
        model.addAttribute("page",blogService.listBlog(pageable,blog));
        return LIST;
    }
    /**
     * @author FallTwo
     * @description //TODO 按条件搜索博客列表
     * @createTime  2022/2/18 13:56
     * @param
     * @return
     **/
    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 2,sort = {"updateTime"},direction = Sort.Direction.DESC)
                                Pageable pageable, BlogQuery blog, Model model) {
        model.addAttribute("page",blogService.listBlog(pageable,blog));
        return "admin/blogs :: blogList";
    }
    /**
     * @author FallTwo
     * @description //TODO 新增博客页面
     * @createTime  2022/2/18 13:55
     * @param
     * @return
     **/
    @GetMapping("/blogs/input")
    public String input( Model model) {
        setTypeAndTag(model);
        model.addAttribute("blog",new Blog());
        return INPUT;
    }
    /**
     * @author FallTwo
     * @description //TODO 渲染所有type，tag
     * @createTime  2022/2/18 13:54
     * @param
     * @return
     **/
    private void setTypeAndTag(Model model){
        model.addAttribute("tags",tagService.listTag());
        model.addAttribute("types",typeService.listType());
    }
    /**
     * @author FallTwo
     * @description //新增博客页面
     * @createTime  2022/2/18 13:53
     * @param
     * @return
     **/
    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        setTypeAndTag(model);
        Blog b = blogService.getBlog(id);
        b.init();
        model.addAttribute("blog",b);
        return INPUT;
    }

    /**
     * @author FallTwo
     * @description //保存或新增博客内容
     * @createTime  2022/2/18 13:41
     * @param
     * @return
     ***/
    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session) {
        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));
        Blog b ;

        if (blog.getId() != null){
            b = blogService.updateBlog(blog.getId(),blog);
        }else {
            b = blogService.saveBlog(blog);
        }

        if (b == null){
            attributes.addFlashAttribute("message","操作失败");
        }else {
            attributes.addFlashAttribute("message","操作成功");
        }



        return REDIRECT_LIST;
    }
    /**
     * @author FallTwo
     * @description //TODO 删除博客
     * @createTime  2022/2/18 14:46
     * @param
     * @return
     **/
    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes){
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message","删除成功");
        return REDIRECT_LIST;
    }


}
