package com.example.postcommand.repository;

import com.example.postcommand.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashtagCommandRepository extends JpaRepository<Hashtag, Long> {

    Boolean existsByName(String name);
}
