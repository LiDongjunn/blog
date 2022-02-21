package com.falltwo.web;

import com.falltwo.pojo.Blog;
import com.falltwo.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * @author: FallTwo
 * @createTime: 2022/2/21 19:32
 * @description: 归档页控制层
 */
@Controller
@RequestMapping("/archive")
public class ArchiveShowController {

    @Autowired
    BlogService blogService;

    @GetMapping
    public String archive(Model model){
        Map<String, List<Blog>> archiveMap = blogService.archiveBlog();
        Long blogCount = blogService.countBlog();
        model.addAttribute("archiveMap",archiveMap);
        model.addAttribute("blogCount",blogCount);
        return "archive";
    }

}
