package com.falltwo.pojo;

import org.hibernate.annotations.Proxy;

import javax.persistence.*;

/**
 * @author: FallTwo
 * @createTime: 2022/2/22 14:49
 * @description: About类
 */
@Proxy(lazy = false)
@Entity
@Table(name = "t_about")
public class About {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Lob
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public String toString() {
        return "About{" +
                "id=" + id +
                ", content='" + content + '\'' +
                '}';
    }
}
