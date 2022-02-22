package com.falltwo.service;

import com.falltwo.NotFoundException;
import com.falltwo.dao.BlogRepository;
import com.falltwo.pojo.Blog;
import com.falltwo.pojo.Type;
import com.falltwo.util.MarkdownUtils;
import com.falltwo.util.MyBeanUtils;
import com.falltwo.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

/**
 * @Author: FallTwo
 * @Date: 2022/2/16 14:08
 */

@Service
public class BlogServiceImpl implements BlogService {
    
    @Autowired
    private BlogRepository blogRepository;
    /**
     * @author: FallTwo
     * @description: 作用：单个评论设置连接博客，后台编辑文章获取博客 原理：根据BlogId查询博客
     * @createTime:  2022/2/21 14:53
     * @param: [id]
     * @return: com.falltwo.pojo.Blog
     */
    @Transactional
    @Override
    public Blog getBlog(Long id) {
        Blog blog = blogRepository.findOne(id);
        if (blog == null){
            throw new NotFoundException("该博客不存在");
        }
        return blog;

    }
    /**
     * @author: FallTwo
     * @description: 作用：前台博客详情文章显示 原理：根据BlogId查询博客并将MD格式转换成HTML格式
     * @createTime:  2022/2/21 14:54
     * @param: [id]
     * @return: com.falltwo.pojo.Blog
     */
    @Transactional
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = blogRepository.findOne(id);
        if (blog == null){
            throw new NotFoundException("该博客不存在");
        }
        Blog b = new Blog();
        BeanUtils.copyProperties(blog,b);
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(b.getContent()));

        blogRepository.updateViews(id);
        return b;
    }

    /**
     * @author: FallTwo
     * @description: 作用：后台管理员查询博客功能 原理：根据博客查询格式（title，type，recommend查询博客）
     * @createTime:  2022/2/21 14:55
     * @param: [pageable, blog]
     * @return: org.springframework.data.domain.Page<com.falltwo.pojo.Blog>
     */
    @Transactional
    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {

        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (blog!=null){
                    if (!"".equals(blog.getTitle()) && blog.getTitle() != null) {
                        predicates.add(cb.like(root.<String>get("title"), "%"+blog.getTitle()+"%"));
                    }
                    if (blog.getTypeId() != null) {
                        predicates.add(cb.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
                    }
                    if (blog.isRecommend()) {
                        predicates.add(cb.equal(root.<Boolean>get("recommend"), blog.isRecommend()));
                    }
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }
    /**
     * @author: FallTwo
     * @description: 查询所有博客
     * @createTime:  2022/2/21 14:55
     * @param: [pageable]
     * @return: org.springframework.data.domain.Page<com.falltwo.pojo.Blog>
     */
    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    /**
     * @author: FallTwo
     * @description: 作用：前台查询博客功能 原理：根据字符串查询相关博客（title，description，content）
     * @createTime:  2022/2/21 15:01
     * @param: [query, pageable]
     * @return: org.springframework.data.domain.Page<com.falltwo.pojo.Blog>
     */
    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return blogRepository.findByQuery(query,pageable);
    }
    /**
     * @author: FallTwo
     * @description: 作用：前台标签页查询博客功能 原理：根据tagId查询相关博客
     * @createTime:  2022/2/21 16:23
     * @param: [id, pageable]
     * @return: org.springframework.data.domain.Page<com.falltwo.pojo.Blog>
     */
    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {

        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                Join join = root.join("tags");
                return cb.equal(join.get("id"),tagId);
            }
        },pageable);
    }

    /**
     * @author: FallTwo
     * @description: 推荐博客查询功能，
     * @createTime:  2022/2/21 15:03
     * @param: [size]
     * @return: java.util.List<com.falltwo.pojo.Blog>
     */
    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {

        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");

        Pageable pageable = new PageRequest(0,size,sort);

        return blogRepository.findBlogTop(pageable);
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogRepository.findGroupYear();
        Map<String, List<Blog>> map = new HashMap<>();

        for (String year : years) {
            List<Blog> blogs = blogRepository.findByYear(year);
            map.put(year,blogs);
        }

        return map;
    }

    /**
     * @author: FallTwo
     * @description: 作用：后台新增博客后，提交保存至数据库 原理：获取实体博客，并使用jpa保存
     * @createTime:  2022/2/21 15:06
     * @param: [blog]
     * @return: com.falltwo.pojo.Blog
     */
    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if (blog.getId() != null){
            blog.setUpdateTime(new Date());
        }else {
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        }
        return blogRepository.save(blog);
    }
    /**
     * @author: FallTwo
     * @description: 作用：后台更新博客 原理：获取博客id，实体博客类，JPA保存博客
     * @createTime:  2022/2/21 15:08
     * @param: [id, blog]
     * @return: com.falltwo.pojo.Blog
     */
    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog blog1 = blogRepository.findOne(id);
        if (blog1 == null){
            throw new NotFoundException("该博客不存在");
        }
        BeanUtils.copyProperties(blog,blog1, MyBeanUtils.getNullPropertyNames(blog));
        blog1.setUpdateTime(new Date());
        return blogRepository.save(blog1);
    }
    /**
     * @author: FallTwo
     * @description: 作用：后台删除博客 原理：根据blogId删除
     * @createTime:  2022/2/21 15:10
     * @param: [id]
     * @return: void
     */
    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.delete(id);
    }

    @Override
    public Long countBlog() {
        return blogRepository.count();
    }
}
