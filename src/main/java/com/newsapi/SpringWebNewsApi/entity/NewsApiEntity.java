package com.newsapi.SpringWebNewsApi.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
public class NewsApiEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String header;

    private String content;

    @NotBlank
    @Column(nullable = false)
    private String author;


    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<CommentsEntity> comments;

    @ManyToMany(mappedBy = "articles")
    Set<IssuesEntity> topics = new HashSet<>();

    public NewsApiEntity(String header, String content, String author){

        this.header=header;
        this.content=content;
        this.author=author;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<CommentsEntity> getComment(){
        return comments;
    }

    public void setComments(List<CommentsEntity> comments){
        this.comments= comments;
    }

    public void setTopics(Set<IssuesEntity> topics) {
        this.topics = topics;
    }

    public Set<IssuesEntity> getTopics() {
        return topics;
    }
}
