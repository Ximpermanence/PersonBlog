package com.ch.blog.pojo;

import lombok.*;
import org.hibernate.annotations.ManyToAny;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: chenhao
 * @create:2020/10/26 16:26
 **/
@Entity(name = "t_blog")
@Getter
@Setter
@Table
@AllArgsConstructor
@NoArgsConstructor
public class Blog {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    /**
     * 这样在初始化时会生成longtext类型的字段
     */
    @Basic(fetch = FetchType.LAZY)
    @Lob
    private String content;
    private String firstPicture;
    private String flag;
    private Integer views;
    private boolean appreciation;
    private boolean shareStatement;
    private boolean commentabled;
    private boolean published;
    private boolean recommend;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
    @ManyToOne
    private Type type;
    //@Transient使得不会进入导数据库，正常属性值，不和数据库进行映射
    @Transient
    private String tagIds;
    /**
     * 级联新增，新增加一个tag,会将其保存到数据库里面
     */
    @ManyToMany(cascade = {CascadeType.PERSIST})
    private List<Tag> tags = new ArrayList<>();
    @ManyToOne
    private User user;
    //一个blog包含多个comment，一个comment只能对应一个博客，blog是关系被维护方，所以加mappedBy
    @OneToMany(mappedBy = "blog")
    private List<Comment> comments = new ArrayList<>();
    /**
     * 这样在初始化时会生成longtext类型的字段
     */
    @Basic(fetch = FetchType.LAZY)
    @Lob
    private String description;


    public void init() {
        this.tagIds=tagsToTagIds(this.getTags());
    }

    private String tagsToTagIds(List<Tag> tags) {
        StringBuffer ids = new StringBuffer();
        tags.forEach(t -> {
            ids.append(t.getId());
            ids.append(",");
        });
        if(tags.size()>=1){
            ids.deleteCharAt(ids.length()-1);
        }
        return ids.toString();
    }


}
