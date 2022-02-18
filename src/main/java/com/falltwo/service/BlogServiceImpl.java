package com.falltwo.service;

import com.falltwo.NotFoundException;
import com.falltwo.dao.BlogRepository;
import com.falltwo.pojo.Blog;
import com.falltwo.pojo.Type;
import com.falltwo.util.MyBeanUtils;
import com.falltwo.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: RunRoad
 * @Date: 2022/2/16 14:08
 */

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;
    @Transactional
    @Override
    public Blog getBlog(Long id) {
        return blogRepository.findOne(id);
    }
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
    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.delete(id);
    }
}
