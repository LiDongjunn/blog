package com.falltwo.dao;

import com.falltwo.pojo.Tag;
import com.falltwo.pojo.Type;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: RunRoad
 * @Date: 2022/2/15 15:51
 */
public interface TagRepository extends JpaRepository<Tag,Long> {
    Tag findByName(String name);
}
