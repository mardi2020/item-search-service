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
@Table(name = "bookmarks",
        indexes = {
                @Index(name = "user_content_ui", columnList = "user_id, content_id")
        })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "user_id")
    private Long userId;

    @Column(nullable = false, name = "content_id")
    private Long contentId;

    @Builder
    public Bookmark(Long id, Long userId, Long contentId) {
        this.id = id;
        this.userId = userId;
        this.contentId = contentId;
    }
}