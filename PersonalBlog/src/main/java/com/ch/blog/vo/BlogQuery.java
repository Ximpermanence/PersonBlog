package com.ch.blog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: chenhao
 * @create:2020/10/28 16:09
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogQuery {

    private String title;

    private Long typeId;

    private boolean recommend;


}
