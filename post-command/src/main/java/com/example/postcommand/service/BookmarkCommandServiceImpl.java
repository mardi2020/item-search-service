package com.example.postcommand.service;

import com.example.postcommand.dto.message.BookmarkKafkaMessage;
import com.example.postcommand.entity.Bookmark;
import com.example.postcommand.entity.Content;
import com.example.postcommand.exception.BookmarkNotFoundException;
import com.example.postcommand.exception.ContentNotFoundException;
import com.example.postcommand.repository.BookmarkCommandRepository;
import com.example.postcommand.repository.ContentCommandRepository;
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
@Slf4j
@Transactional
public class BookmarkCommandServiceImpl implements BookmarkCommandService {

    private final BookmarkCommandRepository bookmarkCommandRepository;

    private final ContentCommandRepository contentCommandRepository;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public Long saveBookmark(Long userId, Long contentId) {
        ValidCheckUtil.checkDuplicatedBookmark(userId, contentId, bookmarkCommandRepository);

        Content content = contentCommandRepository.findById(contentId)
                .orElseThrow(ContentNotFoundException::new);

        Bookmark savedBookmark = bookmarkCommandRepository
                .save(Bookmark.builder()
                        .contentId(contentId)
                        .userId(userId)
                        .build());

        sendMessage(savedBookmark, 0, content.getLikes());
        return savedBookmark.getId();
    }

    @Override
    public void unsaveBookmark(Long userId, Long contentId) {
        Bookmark bookmark = bookmarkCommandRepository.findByUserIdAndContentId(userId, contentId)
                .orElseThrow(BookmarkNotFoundException::new);

        bookmarkCommandRepository.delete(bookmark);
        kafkaTemplate.send(KafkaTopic.DELETE_BOOKMARK, String.valueOf(bookmark.getId()));
    }

    private void sendMessage(Bookmark bookmark, int commentCount, int likesCount) {
        ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String data = null;
        try {
            data = writer.writeValueAsString(BookmarkKafkaMessage.builder()
                    .bookmarkId(bookmark.getId())
                    .userId(bookmark.getUserId())
                    .contentId(bookmark.getContentId())
                    .commentCount(commentCount)
                    .likesCount(likesCount));

            kafkaTemplate.send(KafkaTopic.ADD_BOOKMARK, data);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        log.info("content kafka send - " + data);
    }
}
