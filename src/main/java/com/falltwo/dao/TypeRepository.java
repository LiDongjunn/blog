package com.falltwo.dao;

import com.falltwo.pojo.Type;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: RunRoad
 * @Date: 2022/2/14 13:14
 */
public interface TypeRepository extends JpaRepository<Type,Long> {
    Type findByName(String name);
}
