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
public class IssuesController {

    IssuesRepository issuesRepository;
    NewsApiRepository newsApiRepository;

    @Autowired
    public IssuesController(IssuesRepository issuesRepository, NewsApiRepository newsApiRepository) {
        this.issuesRepository = issuesRepository;
        this.newsApiRepository = newsApiRepository;
    }

    @GetMapping("/topics")
    public ResponseEntity<List<IssuesEntity>> listTopics() {
        return ResponseEntity.ok(issuesRepository.findAll());
    }

    @GetMapping("/articles/{articleId}/topics")
    public ResponseEntity<Set<IssuesEntity>> listTopicsOnArticle(@PathVariable Long articleId) {
        NewsApiEntity article = (NewsApiEntity) newsApiRepository.findById(articleId)
                .orElseThrow(ResourceNotFoundException::new);
        return ResponseEntity.ok(article.getTopics());
    }


    @PostMapping("/topics")
    public ResponseEntity<IssuesEntity> createTopic(@Valid @RequestBody IssuesEntity topic){
        IssuesEntity topicSearch = issuesRepository.findByName(topic.getName());
        if(topicSearch == null){
            issuesRepository.save(topic);
            return ResponseEntity.status(HttpStatus.CREATED).body(topic);
        }
        issuesRepository.save(topicSearch);
        return ResponseEntity.status(HttpStatus.CREATED).body(topicSearch);
    }

    @PostMapping("/articles/{articleId}/topics")
    public ResponseEntity<NewsApiEntity> createAssociation(@RequestBody IssuesEntity topicToAdd, @PathVariable Long articleId){
        NewsApiEntity article = newsApiRepository.findById(articleId).orElseThrow(ResourceNotFoundException::new);

        //Checks if Topic already exists on the Article so double topic is not added.
        List<IssuesEntity> topics = (List<IssuesEntity>) article.getTopics();
        for(IssuesEntity topic2 : topics){
            if(topic2.getName().equals(topicToAdd.getName())){
                return  ResponseEntity.status(HttpStatus.CONFLICT).body(article);
            }
        }

        //Checks if the article Topic already exists in the articleTopic Repositoty, if it doesnt exist adds it to the Repository.
        IssuesEntity articleTopicSearch = issuesRepository.findByName(topicToAdd.getName());
        if (articleTopicSearch == null) {
            issuesRepository.save(topicToAdd);
            article.getTopics().add(topicToAdd);
        } else {
            article.getTopics().add(articleTopicSearch);
        }
        newsApiRepository.save(article);
        return ResponseEntity.status(HttpStatus.CREATED).body(article);
    }


    @PutMapping("/topics/{id}")
    public ResponseEntity<IssuesEntity> updateTopic(@PathVariable Long id, @Valid @RequestBody IssuesEntity updatedTopic){
        issuesRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        updatedTopic.setId(id);
        issuesRepository.save(updatedTopic);
        return ResponseEntity.ok(updatedTopic);
    }


    @DeleteMapping("/topics/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTopic(@PathVariable Long id){
        IssuesEntity toBeDeleted = issuesRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        issuesRepository.delete(toBeDeleted);
    }

    @DeleteMapping("/articles/{articleId}/topics/{topicId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTopicOnArticle(@PathVariable Long articleId, @PathVariable Long topicId){
        NewsApiEntity article = (NewsApiEntity) newsApiRepository.findById(articleId)
                .orElseThrow(ResourceNotFoundException::new);
        IssuesEntity topic = issuesRepository.findById(topicId)
                .orElseThrow(ResourceNotFoundException::new);

        if(topic.getArticles().contains(article)){
            topic.getArticles().remove(article);
            issuesRepository.save(topic);
        }
        else{
            throw new ResourceNotFoundException();
        }

    }


}
