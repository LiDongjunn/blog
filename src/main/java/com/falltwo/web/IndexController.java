package com.falltwo.web;

import com.falltwo.pojo.About;
import com.falltwo.pojo.Tag;
import com.falltwo.pojo.Type;
import com.falltwo.service.AboutService;
import com.falltwo.service.BlogService;
import com.falltwo.service.TagService;
import com.falltwo.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author: FallTwo
 * @createTime: 2022/2/12 17:18
 * @description: 首页控制层
 */
@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;

    /**
     * @author: FallTwo
     * @description: 首页显示
     * @createTime:  2022/2/20 12:32
     * @param: [pageable, model]
     * @return: java.lang.String
     */
    @GetMapping("/")
    public String index(@PageableDefault(size = 8,sort = {"createTime"},direction = Sort.Direction.DESC)
                                    Pageable pageable, Model model){
        model.addAttribute("page",blogService.listBlog(pageable));
        List<Type> types = typeService.listTypeTop(6);
        model.addAttribute("types",types);
        List<Tag> tags = tagService.listTagTop(10);
        model.addAttribute("tags",tags);
        model.addAttribute("recommendBlogs",blogService.listRecommendBlogTop(5));
        return "index";
    }
    /**
     * @author: FallTwo
     * @description: 首页搜索博客
     * @createTime:  2022/2/20 14:20
     * @param: [pageable, query, model]
     * @return: java.lang.String
     */
    @GetMapping("/search")
    public String query(@PageableDefault(size = 5,sort = {"createTime"},direction = Sort.Direction.DESC)
                                Pageable pageable, @RequestParam String query, Model model){
        model.addAttribute("page",blogService.listBlog('%'+query+'%',pageable));
        model.addAttribute("query",query);

        return "search";
    }

    /**
     * @author: FallTwo
     * @description: 作用：博客详情展示功能 原理：根据blogId，取得MD格式博客内容，并转化成html格式展示
     * @createTime:  2022/2/22 14:42
     * @param: [id, model]
     * @return: java.lang.String
     */
    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id,Model model){
        model.addAttribute("blog",blogService.getAndConvert(id));
        return "blogs";
    }

    private Long ABOUT_ID = new Long(1);

    @Autowired
    AboutService aboutService;

    @GetMapping("/about")
    public String about(Model model){
        About about = aboutService.getAndConvert(ABOUT_ID);
        model.addAttribute("about",about);
        return "about";
    }

    @GetMapping("/footer/newblogs")
    public String newBlogs(Model model){
        model.addAttribute("newblogs",blogService.listRecommendBlogTop(3));
        return "_fragments :: newblogList";
    }
}
