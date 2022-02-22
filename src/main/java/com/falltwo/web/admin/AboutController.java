package com.falltwo.web.admin;

import com.falltwo.pojo.About;
import com.falltwo.service.AboutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author: FallTwo
 * @createTime: 2022/2/22 14:42
 * @description:
 */
@Controller
@RequestMapping("/admin")
public class AboutController {

    private Long ABOUT_ID = new Long(1);

    @Autowired
    AboutService aboutService;

    @GetMapping("/about")
    public String about(Model model){
        About about ;

        about = aboutService.getAbout(ABOUT_ID);

        model.addAttribute("about",about);
        return "admin/about-input";
    }

    @PostMapping("/about")
    public String about(About about ,Model model){
        aboutService.saveAbout(about);

        return "redirect:/admin/about";
    }
}
