package com.example.searchservice.domain.user.repository;

import com.example.searchservice.domain.user.UserIndex;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserRepository extends ElasticsearchRepository<UserIndex, Long> {

    @Query(value = "{\"bool\": {\"should\": [{\"match\": {\"name\": \"?0\" }},{\"match\": {\"loginId\": \"?0\"}}]}}")
    List<UserIndex> findAllByNameOrLoginIdUsingQuery(String keyword, Pageable pageable);
}
