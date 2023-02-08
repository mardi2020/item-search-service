package com.example.postcommand.dto.message;

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
}
