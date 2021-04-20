package com.newsapi.SpringWebNewsApi.repository;

import com.newsapi.SpringWebNewsApi.entity.IssuesEntity;
import com.newsapi.SpringWebNewsApi.entity.NewsApiEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsApiRepository extends JpaRepository<NewsApiEntity,Long> {
    List<NewsApiEntity> findByTopics(IssuesEntity topic);
}
