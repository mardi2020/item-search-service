package com.example.postquery.repository;

import com.example.postquery.entity.Hashtag;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HashtagQueryRepository extends MongoRepository<Hashtag, String> {
}
