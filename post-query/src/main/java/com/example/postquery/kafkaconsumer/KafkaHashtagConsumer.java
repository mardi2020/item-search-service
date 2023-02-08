package com.example.postquery.kafkaconsumer;

import com.example.postquery.entity.Content;
import com.example.postquery.exception.ContentNotFoundException;
import com.example.postquery.message.HashtagKafkaMessage;
import com.example.postquery.message.NewHashtagKafkaMessage;
import com.example.postquery.repository.ContentQueryRepository;
import com.example.postquery.repository.HashtagQueryRepository;
import com.example.postquery.util.KafkaTopic;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaHashtagConsumer {

    private final HashtagQueryRepository hashtagQueryRepository;

    private final ContentQueryRepository contentQueryRepository;

    private final ObjectMapper mapper;

    @KafkaListener(topics = KafkaTopic.UPDATE_HASHTAG)
    public void updateHashtag(String message) throws JsonProcessingException {
        HashtagKafkaMessage hashtags = mapper.readValue(message, HashtagKafkaMessage.class);
        Long contentId = hashtags.getContentId();

        Content content = getContentById(contentId);
        content.updateHashtags(hashtags.getHashtags());

        contentQueryRepository.save(content);
    }

    @KafkaListener(topics = KafkaTopic.NEW_HASHTAG)
    public void addHashtag(String message) throws JsonProcessingException {
        NewHashtagKafkaMessage data = mapper.readValue(message, NewHashtagKafkaMessage.class);
        hashtagQueryRepository.save(data.toDocument());
    }

    private Content getContentById(Long id) {
        return contentQueryRepository.findByContentId(id)
                .orElseThrow(ContentNotFoundException::new);
    }
}
