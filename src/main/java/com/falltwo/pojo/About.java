package com.falltwo.pojo;

import lombok.Data;
import lombok.ToString;
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
@Data
@ToString
public class About {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Lob
    private String content;

    private String introduce;

}
