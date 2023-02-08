package com.example.postcommand.service;

import com.example.postcommand.dto.message.ContentKafkaMessage;
import com.example.postcommand.dto.message.HashtagKafkaMessage;
import com.example.postcommand.dto.request.ContentCreateRequest;
import com.example.postcommand.dto.request.ContentUpdateRequest;
import com.example.postcommand.entity.Content;
import com.example.postcommand.exception.ContentNotFoundException;
import com.example.postcommand.repository.ContentCommandRepository;
import com.example.postcommand.util.KafkaTopic;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ContentCommandServiceImpl implements ContentCommandService {

    private final ContentCommandRepository contentCommandRepository;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public Long save(final ContentCreateRequest request) {
        Content savedContent = contentCommandRepository.save(request.toEntity());
        sendMessage(savedContent, KafkaTopic.ADD_CONTENT);
        return savedContent.getId();
    }

    @Override
    public void update(final Long contentId, final ContentUpdateRequest request) {
        final Content content = contentCommandRepository.findById(contentId)
                .orElseThrow(ContentNotFoundException::new);
        content.update(request);
        contentCommandRepository.save(content);
        sendMessage(content, KafkaTopic.UPDATE_CONTENT);
        sendMessage(content.getHashtags(), contentId);
    }

    @Override
    public void delete(Long contentId) {
        contentCommandRepository.deleteById(contentId);
        kafkaTemplate.send(KafkaTopic.DELETE_CONTENT, String.valueOf(contentId));
    }

    public boolean visibilityLikes(Long contentId) {
        Content content = getContent(contentId);
        content.changeVisibleLikes();
        Content savedContent = contentCommandRepository.save(content);
        kafkaTemplate.send(KafkaTopic.VISIBLE_LIKES, String.valueOf(contentId));
        return savedContent.getVisibleLikes();
    }

    public boolean visibilityComments(Long contentId) {
        Content content = getContent(contentId);
        content.changeVisibleComments();
        Content savedContent = contentCommandRepository.save(content);
        kafkaTemplate.send(KafkaTopic.VISIBLE_COMMENT, String.valueOf(contentId));
        return savedContent.getVisibleComments();
    }

    private Content getContent(Long contentId) {
        return contentCommandRepository.findById(contentId)
                .orElseThrow(ContentNotFoundException::new);
    }

    private void sendMessage(Content content, String topic) {
        ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String data = null;
        try {
            ContentKafkaMessage message = new ContentKafkaMessage(content);
            data = writer.writeValueAsString(message);
            kafkaTemplate.send(topic, data);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        log.info("content kafka send - " + data);
    }

    private void sendMessage(Set<Long> hashtags, Long contentId) {
        ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String data = null;
        try {
            HashtagKafkaMessage message = new HashtagKafkaMessage(hashtags, contentId);
            data = writer.writeValueAsString(message);
            kafkaTemplate.send(KafkaTopic.UPDATE_HASHTAG, data);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        log.info("content kafka send - " + data);
    }
}