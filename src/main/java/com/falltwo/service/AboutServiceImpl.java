package com.falltwo.service;

import com.falltwo.NotFoundException;
import com.falltwo.dao.AboutRepository;
import com.falltwo.pojo.About;
import com.falltwo.util.MarkdownUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: FallTwo
 * @createTime: 2022/2/22 14:54
 * @description:
 */
@Service
public class AboutServiceImpl implements AboutService{
    @Autowired
    AboutRepository aboutRepository;

    @Override
    public About getAbout(Long id) {
        About about = new About();

        if (aboutRepository.count() == 0){
            about.setId(new Long(1));
            about.setContent("About内容为空，请输入...");
        }else {
            about = aboutRepository.getOne(new Long(1));
        }
        return about;
    }

    @Override
    public int saveAbout(About about) {
        if (aboutRepository.count() == 0){
            aboutRepository.insert("%"+about.getContent()+"%");
        }else {
            about.setId(new Long(1));
            aboutRepository.save(about);
        }
        return 1;
    }

    @Override
    public About getAndConvert(Long id) {
        About about = aboutRepository.getOne(id);
        if (about == null){
            throw new NotFoundException("未找到关于内容");
        }

        About a =  new About();
        BeanUtils.copyProperties(about,a);
        a.setContent(MarkdownUtils.markdownToHtmlExtensions(a.getContent()));
        return a;
    }
}
