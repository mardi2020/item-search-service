package com.example.postcommand.repository;


import com.example.postcommand.entity.Bookmark;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkCommandRepository extends JpaRepository<Bookmark, Long> {

    Optional<Bookmark> findByUserIdAndContentId(Long userId, Long contentId);

    Boolean existsByUserIdAndContentId(Long userId, Long contentId);
}
