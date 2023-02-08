package com.example.postcommand.dto.message;


import com.example.postcommand.entity.Hashtag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewHashtagKafkaMessage {

    private Long hashtagId;

    private String name;

    public Hashtag toEntity() {
        return new Hashtag(name);
    }
}
