package com.newsapi.SpringWebNewsApi.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class CommentsEntity {

    public interface PutValidationGroup{

    };

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank
    private String content;

    @Column(nullable = false)
    @NotBlank
    private String author;

    @ManyToOne
    @JsonIdentityReference(alwaysAsId=true)
    @JoinColumn(nullable = false)
    @NotNull(groups = { PutValidationGroup.class })
    private NewsApiEntity article;

    public Long getId(){
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public NewsApiEntity getArticle() {
        return article;
    }

    public void setArticle(NewsApiEntity article) {
        this.article = article;
    }
}
