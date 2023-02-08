package com.example.postquery.repository;

import com.example.postquery.entity.ContentLike;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContentLikeQueryRepository extends MongoRepository<ContentLike, String> {
}
