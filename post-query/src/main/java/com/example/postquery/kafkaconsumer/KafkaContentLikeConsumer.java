package com.example.postquery.kafkaconsumer;

import com.example.postquery.entity.ContentLike;
import com.example.postquery.repository.ContentLikeQueryRepository;
import com.example.postquery.util.KafkaTopic;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaContentLikeConsumer {

    private final ObjectMapper mapper;

    private final ContentLikeQueryRepository contentLikeQueryRepository;

    @KafkaListener(topics = KafkaTopic.ADD_CONTENT_LIKE)
    public void likeContent(String message) throws JsonProcessingException {
        ContentLike contentLike = mapper.readValue(message, ContentLike.class);
        contentLikeQueryRepository.save(contentLike);
    }

    @KafkaListener(topics = KafkaTopic.DELETE_CONTENT_LIKE)
    public void unlikeContent(String message) throws JsonProcessingException {
        ContentLike contentLike = mapper.readValue(message, ContentLike.class);
        contentLikeQueryRepository.delete(contentLike);
    }
}
