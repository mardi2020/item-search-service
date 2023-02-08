package com.example.postquery.repository;

import com.example.postquery.entity.Bookmark;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookmarkQueryRepository extends MongoRepository<Bookmark, String> {

    Optional<Bookmark> findByBookmarkId(Long BookmarkId);
}
