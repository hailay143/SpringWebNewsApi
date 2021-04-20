package com.newsapi.SpringWebNewsApi.conroller;

import com.newsapi.SpringWebNewsApi.entity.IssuesEntity;
import com.newsapi.SpringWebNewsApi.entity.NewsApiEntity;
import com.newsapi.SpringWebNewsApi.repository.IssuesRepository;
import com.newsapi.SpringWebNewsApi.repository.NewsApiRepository;
import com.newsapi.SpringWebNewsApi.ResourceExceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
public class NewsApiController{

    NewsApiRepository newsApiRepository;
    IssuesRepository issuesRepository;

    @Autowired
    public NewsApiController(NewsApiRepository newsApiRepository, IssuesRepository issuesRepository) {
        this.newsApiRepository = newsApiRepository;
        this.issuesRepository= issuesRepository;
    }

    @GetMapping("/articles")
    public ResponseEntity<List<NewsApiEntity>> listAllArticles(
            @RequestParam(
                    required = false,
                    value="someAttr") Long topicId) {
        return ResponseEntity.ok(newsApiRepository.findAll());
    }

    @GetMapping("/topics/{topicId}/articles")
    public ResponseEntity<Set<NewsApiEntity>> listArticlesByTopic(@PathVariable Long topicId){
        IssuesEntity issuesEntity = issuesRepository.findById(topicId).orElseThrow(ResourceNotFoundException::new);
        return ResponseEntity.ok(issuesEntity.getArticles());
    }

    @GetMapping("/articles/{id}")
    public ResponseEntity<NewsApiEntity> getArticleById(@PathVariable Long id){
        NewsApiEntity articleId = (NewsApiEntity) newsApiRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        return ResponseEntity.ok(articleId);
    }

    @PostMapping("/articles")
    public ResponseEntity<NewsApiEntity> createArticle(@RequestBody NewsApiEntity article){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(newsApiRepository.save(article));
    }


    @PutMapping("articles/{id}")
    public ResponseEntity<NewsApiEntity> updateArticle(@PathVariable Long id, @Valid @RequestBody NewsApiEntity updatedArticle) {
        newsApiRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        updatedArticle.setId(id);
        return ResponseEntity.ok(newsApiRepository.save(updatedArticle));
    }

    @DeleteMapping("articles/{id}")
    public ResponseEntity<NewsApiEntity> deleteArticle(@PathVariable Long id) {
        NewsApiEntity article = newsApiRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        for (IssuesEntity topic: article.getTopics()) {
            topic.getArticles().remove(article);
        }
        newsApiRepository.delete(article);
        return ResponseEntity.ok(article);
    }


}
