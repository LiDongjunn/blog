package com.falltwo.web;

import com.falltwo.pojo.Blog;
import com.falltwo.pojo.Type;
import com.falltwo.service.BlogService;
import com.falltwo.service.TypeService;
import com.falltwo.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author: FallTwo
 * @createTime: 2022/2/21 14:20
 * @description:分类控制层
 */
@Controller
@RequestMapping("/type")
public class TypeShowController {
    @Autowired
    TypeService typeService;
    @Autowired
    BlogService blogService;

    @GetMapping("/{id}")
    public String type(@PageableDefault(size = 4,sort = {"updateTime"},direction = Sort.Direction.DESC)
                                   Pageable pageable, @PathVariable Long id, Model model){
        List<Type> types = typeService.listTypeTop(1000);

        if (id == -1){
            id = types.get(0).getId();
        }
        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setTypeId(id);
        Page<Blog> blogs = blogService.listBlog(pageable, blogQuery);

        model.addAttribute("types",types);
        model.addAttribute("page",blogs);
        model.addAttribute("activeTypeId",id);
        return "types";
    }
}
