package com.example.postcommand.dto.request;

import com.example.postcommand.entity.Content;
import com.example.postcommand.entity.ImageUrl;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentCreateRequest {

    @JsonProperty(required = true)
    private Long userId;

    private String text;

    private boolean visibleComments = true;

    private boolean visibleLikes = true;

    @JsonProperty(required = true)
    private List<ImageUrl> imageUrl;

    private Set<Long> hashtags;

    @Builder
    public ContentCreateRequest(Long userId, String text, boolean visibleComments,
                                boolean visibleLikes, List<ImageUrl> imageUrl,
                                Set<Long> hashtags) {
        this.userId = userId;
        this.text = text;
        this.visibleComments = visibleComments;
        this.visibleLikes = visibleLikes;
        this.imageUrl = imageUrl;
        this.hashtags = hashtags;
    }

    public Content toEntity() {
        return Content.builder()
                .text(text)
                .imageUrl(imageUrl)
                .userId(userId)
                .visibleComments(visibleComments)
                .visibleLikes(visibleLikes)
                .hashtags(hashtags)
                .build();
    }
}
