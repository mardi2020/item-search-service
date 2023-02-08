package com.example.postquery.repository;

import com.example.postquery.entity.Hashtag;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HashtagQueryRepository extends MongoRepository<Hashtag, String> {

    Optional<Hashtag> findByName(String name);
}
