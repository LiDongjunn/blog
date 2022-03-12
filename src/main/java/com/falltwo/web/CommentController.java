package com.falltwo.web;

import com.falltwo.pojo.Comment;
import com.falltwo.pojo.User;
import com.falltwo.service.BlogService;
import com.falltwo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author: FallTwo
 * @createTime: 2022/2/20 17:02
 * @description:
 */
@Controller
public class CommentController {
    @Autowired
    CommentService commentService;
    @Autowired
    BlogService blogService;

    @Value("${comment.avatar}")
    private String avatar;

    @Value("${comment.fans}")
    private String fans;
    /**
     * @author: FallTwo
     * @description: 展示评论
     * @createTime:  2022/2/20 21:22
     * @param: [blogId, model]
     * @return: java.lang.String
     */
    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model){
        List<Comment> comments = commentService.listCommentByBlogId(blogId);
        model.addAttribute("comments",comments);
        return "blogs :: commentList";
    }
    /**
     * @author: FallTwo
     * @description: 发布评论
     * @createTime:  2022/2/20 21:23
     * @param: [comment]
     * @return: java.lang.String
     */
    @PostMapping("/comments")
    public String post(Comment comment, HttpSession session){
        Long blogId = comment.getBlog().getId();
        comment.setBlog(blogService.getBlog(blogId));

        User user = (User)session.getAttribute("user");
        if (user != null){
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
        }else {
            comment.setAvatar(fans);
        }
        commentService.saveComment(comment);
        return "redirect:/comments/"+ blogId;
    }
}
