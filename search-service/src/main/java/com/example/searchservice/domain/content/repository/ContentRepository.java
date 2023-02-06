package com.example.searchservice.domain.content.repository;

import com.example.searchservice.domain.content.ContentIndex;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ContentRepository extends ElasticsearchRepository<ContentIndex, Long> {

    @Query(value = "{\"bool\": {\"must\": [{\"match\": {\"hashtags\": \"?0\"}}]}}")
    List<ContentIndex> findAllByHashtagUsingQuery(String hashtag, Pageable pageable);
}
