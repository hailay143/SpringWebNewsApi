package com.newsapi.SpringWebNewsApi.repository;

import com.newsapi.SpringWebNewsApi.entity.IssuesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssuesRepository extends JpaRepository<IssuesEntity, Long> {
    IssuesEntity findByName(String name);
}
