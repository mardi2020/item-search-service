package com.example.postcommand.repository;

import com.example.postcommand.entity.ContentLike;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeCommandRepository extends JpaRepository<ContentLike, Long> {

    Optional<ContentLike> findByUserIdAndContentId(Long userId, Long contentId);

    Boolean existsByUserIdAndContentId(Long userId, Long contentId);
}
