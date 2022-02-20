package com.falltwo.dao;

import com.falltwo.pojo.Tag;
import com.falltwo.pojo.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author: RunRoad
 * @Date: 2022/2/15 15:51
 */
public interface TagRepository extends JpaRepository<Tag,Long> {
    Tag findByName(String name);

    @Query("select t from Tag t ")
    List<Tag> findTop(Pageable pageable);
}
