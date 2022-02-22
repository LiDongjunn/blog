package com.falltwo.service;

import com.falltwo.pojo.About;

/**
 * @author: FallTwo
 * @createTime: 2022/2/22 14:53
 * @description:
 */
public interface AboutService {
    About getAbout(Long id);

    int saveAbout(About about);

    About getAndConvert(Long id);

}
