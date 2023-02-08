package com.example.postquery.message;

import com.example.postquery.entity.Bookmark;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BookmarkKafkaMessage {

    private Long bookmarkId;

    private Long userId;

    private Long contentId;

    private int commentCount;

    private int likesCount;

    public Bookmark toEntity() {
        return Bookmark.builder()
                .bookmarkId(bookmarkId)
                .userId(userId)
                .commentCount(commentCount)
                .contentId(contentId)
                .likesCount(likesCount)
                .build();
    }
}
