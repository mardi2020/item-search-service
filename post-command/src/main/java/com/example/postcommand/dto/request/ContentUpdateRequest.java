package com.example.postcommand.dto.request;


import com.example.postcommand.entity.Content;
import com.example.postcommand.entity.ImageUrl;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor @Builder
public class ContentUpdateRequest {

    private Long userId;

    private String text;

    private List<ImageUrl> imageUrl;

    private Boolean visibleLikes;

    private Boolean visibleComments;

    private Set<Long> hashtags;

    public Content toEntity() {
        return Content.builder()
                .userId(userId)
                .text(text)
                .imageUrl(imageUrl)
                .visibleLikes(visibleLikes)
                .visibleComments(visibleComments)
                .hashtags(hashtags)
                .build();
    }
}
