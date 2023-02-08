package com.example.postquery.service;


import com.example.postquery.entity.Content;
import com.example.postquery.exception.ContentNotFoundException;
import com.example.postquery.repository.ContentQueryRepository;
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
public class KafkaContentConsumer {

    private final ObjectMapper mapper;

    private final ContentQueryRepository contentQueryRepository;

    @KafkaListener(topics = KafkaTopic.ADD_CONTENT)
    public void addContent(String message) throws JsonProcessingException {
        Content content = readContentByJson(message);
        contentQueryRepository.save(content);
    }

    @KafkaListener(topics = KafkaTopic.UPDATE_CONTENT)
    public void updateContent(String message) throws JsonProcessingException {
        Content content = readContentByJson(message);
        Content storedContent = getContentById(content.getContentId());
        storedContent.updateContent(content);
        contentQueryRepository.save(storedContent);
    }

    @KafkaListener(topics = KafkaTopic.DELETE_CONTENT)
    public void deleteContent(String message) throws JsonProcessingException {
        log.info("[Kafka message]: " + message);
        Long contentId = mapper.readValue(message, Long.class);
        Content storedContent = getContentById(contentId);
        contentQueryRepository.delete(storedContent);
    }


    @KafkaListener(topics = KafkaTopic.VISIBLE_LIKES)
    public void visibilityLikes(String message) throws JsonProcessingException {
        Long contentId = mapper.readValue(message, Long.class);
        Content content = getContentById(contentId);
        content.updateVisibilityLikes();
        contentQueryRepository.save(content);
    }

    @KafkaListener(topics = KafkaTopic.VISIBLE_COMMENT)
    public void visibilityComments(String message) throws JsonProcessingException {
        Long contentId = mapper.readValue(message, Long.class);
        Content content = getContentById(contentId);
        content.updateVisibilityComments();
        contentQueryRepository.save(content);
    }

    private Content getContentById(Long id) {
        return contentQueryRepository.findByContentId(id)
                .orElseThrow(ContentNotFoundException::new);
    }

    private Content readContentByJson(String json) throws JsonProcessingException {
        log.info("[Kafka message]: " + json);
        return mapper.readValue(json, Content.class);
    }
}
