package com.example.postquery.message;

import com.example.postquery.entity.Hashtag;
import lombok.Data;

@Data
public class NewHashtagKafkaMessage {

    private Long hashtagId;

    private String name;

    public Hashtag toDocument() {
        return Hashtag.builder()
                .hashtagId(hashtagId)
                .name(name)
                .build();
    }
}
