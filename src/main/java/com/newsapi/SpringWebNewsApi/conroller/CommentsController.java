package com.newsapi.SpringWebNewsApi.conroller;

import com.newsapi.SpringWebNewsApi.ResourceExceptions.ResourceNotFoundException;
import com.newsapi.SpringWebNewsApi.entity.CommentsEntity;
import com.newsapi.SpringWebNewsApi.entity.NewsApiEntity;
import com.newsapi.SpringWebNewsApi.repository.CommentsRepository;
import com.newsapi.SpringWebNewsApi.repository.NewsApiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentsController {
    CommentsRepository commentsRepository;
    NewsApiRepository newsApiRepository;

    @Autowired
    public CommentsController(CommentsRepository commentsRepository, NewsApiRepository newsApiRepository) {
        this.commentsRepository = commentsRepository;
        this.newsApiRepository= newsApiRepository;
    }

    @GetMapping("/articles/{articleId}/comments")
    public ResponseEntity<List<CommentsEntity>> listAllCommentsById(@PathVariable Long articleId){
        return ResponseEntity.ok(commentsRepository.findAll());
    }

    @GetMapping(value = "/comments", params = {"authorName"})
    public ResponseEntity<List<CommentsEntity>> listAllCommentsByAuthorName(@RequestParam String authorName){
        return ResponseEntity.ok(commentsRepository.findByAuthorName(authorName));
    }

    @PostMapping("/articles/{articleId}/comments")
    public ResponseEntity<CommentsEntity> createComment(@PathVariable Long articleId, @RequestBody CommentsEntity comments){
        NewsApiEntity article = (NewsApiEntity) newsApiRepository.findById(articleId).orElseThrow(ResourceNotFoundException::new);
        comments.setArticle(article);
        commentsRepository.save(comments);
        return ResponseEntity.status(HttpStatus.CREATED).body(comments);
    }


    @PutMapping("/comments/{id}")
    public ResponseEntity<CommentsEntity> updateComment(@PathVariable Long id, @RequestBody CommentsEntity updatedComment){
        commentsRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        updatedComment.setId(id);
        commentsRepository.save(updatedComment);
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("comments/{id}")
    public ResponseEntity<CommentsEntity> deleteComment(@PathVariable Long id){
        CommentsEntity comment = commentsRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        commentsRepository.delete(comment);
        return ResponseEntity.ok(comment);
    }
}