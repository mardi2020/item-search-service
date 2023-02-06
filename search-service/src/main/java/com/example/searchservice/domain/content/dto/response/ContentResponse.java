package com.example.searchservice.domain.content.dto.response;

import com.example.searchservice.domain.content.ContentIndex;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentResponse {

    private Long contentId;

    private List<String> hashtags;

    private int count;

    private String createdAt;

    public ContentResponse(Long contentId, List<String> hashtags, int count, Long createdAt) {
        this.contentId = contentId;
        this.hashtags = hashtags;
        this.count = count;
        this.createdAt = toStringDate(createdAt);
    }

    public ContentResponse(ContentIndex contentIndex) {
        this.contentId = contentIndex.getContentId();
        this.hashtags = contentIndex.getHashtags();
        this.count = contentIndex.getCount();
        this.createdAt = toStringDate(contentIndex.getCreatedAt());
    }

    public static String toStringDate(Long createdAt) {
        LocalDate date = LocalDate.ofEpochDay(Duration.ofMillis(createdAt).toDays());
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
