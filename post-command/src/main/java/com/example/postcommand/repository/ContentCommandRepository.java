package com.example.postcommand.repository;


import com.example.postcommand.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentCommandRepository extends JpaRepository<Content, Long> {
}
