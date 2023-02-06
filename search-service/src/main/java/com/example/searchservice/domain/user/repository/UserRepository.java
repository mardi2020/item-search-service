package com.example.searchservice.domain.user.repository;

import com.example.searchservice.domain.user.UserIndex;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserRepository extends ElasticsearchRepository<UserIndex, Long> {

    @Query(value = "{\"bool\": {\"must_not\": [{\"term\": {\"name\": \"?0\"}}, {\"term\": {\"loginId\": \"?0\"}} ] }}")
    List<UserIndex> findAllByKeywordUsingQuery(String keyword, Pageable pageable);
}
