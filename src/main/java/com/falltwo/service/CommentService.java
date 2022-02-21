package com.falltwo.service;

import com.falltwo.pojo.Comment;

import java.util.List;

/**
 * @author: FallTwo
 * @createTime: 2022/2/20 19:55
 * @description:
 */
public interface CommentService {
    List<Comment> listCommentByBlogId(Long blogId);

    Comment saveComment(Comment comment);

}
