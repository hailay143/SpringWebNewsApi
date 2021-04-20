package com.newsapi.SpringWebNewsApi.repository;

import com.newsapi.SpringWebNewsApi.entity.CommentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CommentsRepository extends JpaRepository<CommentsEntity,Long> {
    List<CommentsEntity> findByAuthorName(String authorName);
}
