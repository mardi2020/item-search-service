package com.example.postcommand.service;

import com.example.postcommand.dto.message.NewHashtagKafkaMessage;
import com.example.postcommand.entity.Hashtag;
import com.example.postcommand.repository.HashtagCommandRepository;
import com.example.postcommand.util.KafkaTopic;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class HashtagCommandService {

    private final HashtagCommandRepository hashtagRepository;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public Long save(String name) {
        if (hashtagRepository.existsByName(name)) {
            return -1L;
        }

        Hashtag savedHashtag = hashtagRepository.save(new Hashtag(name));
        sendMessage(savedHashtag);
        return savedHashtag.getId();
    }

    private void sendMessage(Hashtag hashtag) {
        ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String data = null;
        try {
            data = writer.writeValueAsString(NewHashtagKafkaMessage.builder()
                    .hashtagId(hashtag.getId())
                    .name(hashtag.getName()));

            kafkaTemplate.send(KafkaTopic.NEW_HASHTAG, data);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        log.info("content kafka send - " + data);
    }
}
