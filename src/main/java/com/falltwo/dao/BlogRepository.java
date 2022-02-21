package com.falltwo.dao;

import com.falltwo.pojo.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: RunRoad
 * @Date: 2022/2/16 14:10
 */
public interface BlogRepository extends JpaRepository<Blog,Long>, JpaSpecificationExecutor<Blog> {

    @Query("select b from Blog b where b.recommend = true")
    List<Blog> findBlogTop(Pageable pageable);

    @Query("select b from Blog b where b.title like ?1 or b.content like ?1 or b.description like  ?1")
    Page<Blog> findByQuery(String query, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update Blog b set b.views = b.views + 1 where b.id = ?1")
    int updateViews(Long id);

    //    @Query("select function('date_format',b.updateTime,'%Y') as year from Blog b group by function('date_format',b.updateTime,'%Y') order by year desc ")
    @Query(value="SELECT DATE_FORMAT(b.create_time,'%Y') AS YEAR FROM t_blog b GROUP BY YEAR ORDER BY YEAR DESC;",nativeQuery=true)
    List<String> findGroupYear();
    @Query(value="SELECT * FROM t_blog b WHERE DATE_FORMAT(b.create_time,'%Y') = ?1 ;",nativeQuery=true)
    List<Blog> findByYear(String year);
}
