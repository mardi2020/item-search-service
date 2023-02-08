package com.example.postcommand.dto.message;

import java.util.Set;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HashtagKafkaMessage {

    private Set<Long> hashtags;

    private Long contentId;

    @Builder
    public HashtagKafkaMessage(Set<Long> hashtags, Long contentId) {
        this.hashtags = hashtags;
        this.contentId = contentId;
    }
}
