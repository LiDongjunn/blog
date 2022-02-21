package com.falltwo.web;

import com.falltwo.pojo.Blog;
import com.falltwo.pojo.Tag;
import com.falltwo.service.BlogService;
import com.falltwo.service.TagService;
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
 * @createTime: 2022/2/21 16:00
 * @description:前台标签控制层
 */
@Controller
@RequestMapping("/tag")
public class TagShowController {
    @Autowired
    TagService tagService;
    @Autowired
    BlogService blogService;

    @GetMapping("/{id}")
    public String tag(@PageableDefault(size = 2,sort = {"updateTime"},direction = Sort.Direction.DESC)
                               Pageable pageable, @PathVariable Long id, Model model){
        List<Tag> tags = tagService.listTagTop(1000);
        Tag tag;
        if (id == -1){
            tag = tags.get(0);
        }else {
            tag = tagService.getTag(id);
        }

        model.addAttribute("tags",tags);
        model.addAttribute("page",blogService.listBlog(tag.getId(),pageable));
        model.addAttribute("activeTagId",tag.getId());
        model.addAttribute("activeTagName",tag.getName());
        return "tags";
    }

}
