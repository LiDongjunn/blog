package com.falltwo.dao;

import com.falltwo.pojo.About;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author: FallTwo
 * @createTime: 2022/2/22 14:52
 * @description:
 */
public interface AboutRepository extends JpaRepository<About,Long> {

    @Modifying
    @Query(value="INSERT INTO t_about VALUES(1,?1) ;",nativeQuery=true)
    int insert(String content);
}
