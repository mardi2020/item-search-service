package com.example.postquery.repository;


import com.example.postquery.entity.Content;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentQueryRepository extends MongoRepository<Content, String> {

    Optional<Content> findByContentId(Long postId);

    List<Content> findByUserId(Long userId);
}
