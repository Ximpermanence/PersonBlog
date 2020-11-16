package com.ch.blog.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: chenhao
 * @create:2020/10/26 16:39
 **/
@Entity(name = "t_comment")
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue
    private Long id;
    private String nickname;
    private String email;
    private String content;
    private String avatar;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    @ManyToOne
    private Blog blog;

    /**
     * 下一级的子节点
     */
    @OneToMany(mappedBy = "parentComment")
    private List<Comment> replycomments = new ArrayList<>();

    @ManyToOne
    private Comment parentComment;

    private boolean adminComment;
}
