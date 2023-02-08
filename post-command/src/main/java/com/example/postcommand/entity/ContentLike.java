package com.example.postcommand.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "content_likes", indexes = {
        @Index(name = "user_content_ui", columnList = "user_id, content_id")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "content_id", nullable = false)
    private Long contentId;

    @Builder
    public ContentLike(Long userId, Long contentId) {
        this.userId = userId;
        this.contentId = contentId;
    }
}