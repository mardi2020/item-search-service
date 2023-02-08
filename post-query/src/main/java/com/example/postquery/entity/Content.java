package com.example.postquery.entity;

import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Document(collection = "contents")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor @Builder
public class Content {

    @Id
    @Field(value = "_id", targetType = FieldType.OBJECT_ID)
    private String id;

    @Indexed(unique = true)
    private Long contentId;

    private Long userId;

    private String text;

    private int likes;

    private List<ImageUrl> imageUrl;

    private Set<Long> hashtags;

    private Boolean visibleLikes;

    private Boolean visibleComments;

    private String createdAt;

    private String modifiedAt;

    public void updateContent(Content contentDto) {
        if (contentDto.getText() != null) {
            this.text = contentDto.getText();
        }
        if (contentDto.getImageUrl() != null) {
            this.imageUrl = contentDto.getImageUrl();
        }
    }

    public void updateHashtags(Set<Long> hashtags) {
        this.hashtags = hashtags;
    }

    public void updateVisibilityLikes() {
        this.visibleLikes = !this.visibleLikes;
    }

    public void updateVisibilityComments() {
        this.visibleComments = !this.visibleComments;
    }
}
