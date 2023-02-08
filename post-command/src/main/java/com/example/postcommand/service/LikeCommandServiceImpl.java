package com.example.postcommand.service;

import com.example.postcommand.dto.message.LikeKafkaMessage;
import com.example.postcommand.entity.Content;
import com.example.postcommand.entity.ContentLike;
import com.example.postcommand.exception.ContentNotFoundException;
import com.example.postcommand.exception.LikeNotFoundException;
import com.example.postcommand.repository.ContentCommandRepository;
import com.example.postcommand.repository.LikeCommandRepository;
import com.example.postcommand.util.KafkaTopic;
import com.example.postcommand.util.ValidCheckUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LikeCommandServiceImpl implements LikeCommandService {

    private final LikeCommandRepository likeCommandRepository;

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ContentCommandRepository contentCommandRepository;

    @Override
    public int likeContent(Long userId, Long contentId) {
        ValidCheckUtil.checkDuplicatedLike(userId, contentId, likeCommandRepository);

        Content content = contentCommandRepository.findById(contentId)
                .orElseThrow(ContentNotFoundException::new);
        content.increaseLikes();
        contentCommandRepository.save(content);

        ContentLike contentLike = likeCommandRepository
                .save(new ContentLike(userId, contentId));

        likeCommandRepository.save(contentLike);
        sendMessage(contentLike, KafkaTopic.ADD_CONTENT_LIKE);
        return content.getLikes();
    }

    @Override
    public int unlikeContent(Long userId, Long contentId) {
        Content content = contentCommandRepository.findById(contentId)
                .orElseThrow(ContentNotFoundException::new);
        content.decreaseLikes();
        contentCommandRepository.save(content);

        ContentLike contentLike = likeCommandRepository.findByUserIdAndContentId(userId, contentId)
                .orElseThrow(LikeNotFoundException::new);
        likeCommandRepository.delete(contentLike);
        sendMessage(contentLike, KafkaTopic.DELETE_CONTENT_LIKE);
        return content.getLikes();
    }

    private void sendMessage(ContentLike contentLike, String topic) {
        ObjectWriter writer = new ObjectMapper().writer();
        String data = null;
        try {
            LikeKafkaMessage message = LikeKafkaMessage.builder()
                    .contentId(contentLike.getContentId())
                    .userId(contentLike.getUserId())
                    .contentLikeId(contentLike.getContentId())
                    .build();

            data = writer.writeValueAsString(message);
            kafkaTemplate.send(topic, data);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        log.info("content kafka send - " + data);
    }
}
