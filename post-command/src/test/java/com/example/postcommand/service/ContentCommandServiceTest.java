package com.example.postcommand.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


import com.example.postcommand.dto.request.ContentCreateRequest;
import com.example.postcommand.dto.request.ContentUpdateRequest;
import com.example.postcommand.entity.Content;
import com.example.postcommand.entity.ImageUrl;
import com.example.postcommand.repository.ContentCommandRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ContentCommandServiceTest {

    @Autowired
    private ContentCommandService contentCommandService;
    @Autowired
    private ContentCommandRepository contentCommandRepository;


    @DisplayName("새로운 게시글을 생성한다")
    @Test
    void 새로운_게시글을_생성한다() {
        // given
        Long userId = 1L;
        String text = "본문 #해시태그 @test1234";
        boolean visibleComments = false;
        boolean visibleLikes = false;
        String url = "http://test.jikji/asd12sdf452";
        int order = 1;
        List<ImageUrl> imageUrl = List.of(new ImageUrl(url, order, userId));
        Set<Long> hashtags = Set.of(1L, 2L, 3L, 4L);

        ContentCreateRequest contentCreateRequest = new ContentCreateRequest(
                userId, text,
                visibleComments,
                visibleLikes,
                imageUrl,
                hashtags);

        // when
        Long savedId = contentCommandService.save(contentCreateRequest);

        // then
        assertThat(savedId).isNotNull();
    }

    @DisplayName("게시글을 수정한다")
    @Test
    void 게시글을_수정한다() {
        // given
        final Long contentId = 1L;
        Content content = Content.builder()
                .id(contentId)
                .likes(0)
                .text("변경전 텍스트 #aaa #bb #ccc #dde")
                .imageUrl(List.of(new ImageUrl("http://test.jikji/before-image", 1, 1L)))
                .userId(1L)
                .visibleComments(false)
                .visibleLikes(false)
                .hashtags(Set.of(1L, 2L, 3L, 4L))
                .build();
        contentCommandRepository.save(content);

        ContentUpdateRequest request = ContentUpdateRequest.builder()
                .text("변경된 텍스트 #aaa")
                .userId(1L)
                .imageUrl(List.of(new ImageUrl("http://test.jikji/changed-image-url", 1, 1L)))
                .visibleComments(true)
                .visibleLikes(true)
                .hashtags(Set.of(1L))
                .build();
        // when
        contentCommandService.update(contentId, request);
        content = contentCommandRepository.findById(contentId).orElseThrow();

        // then
        assertThat(content.getText()).isEqualTo(request.getText());
        assertThat(content.getImageUrl()).isEqualTo(request.getImageUrl());
        assertThat(content.getVisibleComments()).isEqualTo(request.getVisibleComments());
        assertThat(content.getVisibleLikes()).isEqualTo(request.getVisibleLikes());
        assertThat(content.getHashtags()).isEqualTo(request.getHashtags());
    }

    @DisplayName("게시글을 삭제한다")
    @Test
    void 게시글을_삭제한다() {
        // given
        Long contentId = 1L;
        Content content = Content.builder()
                .id(contentId)
                .likes(0)
                .text("게시글 본문")
                .imageUrl(List.of(new ImageUrl("http://test.jikji/image", 1, 1L)))
                .userId(1L)
                .visibleComments(false)
                .visibleLikes(false)
                .hashtags(new HashSet<>())
                .build();
        contentCommandRepository.save(content);

        // when, then
        assertDoesNotThrow(() -> contentCommandService.delete(contentId));
    }

    @DisplayName("댓글 감추기 요청시 댓글을 비활성화 한다")
    @Test
    void 댓글을_비활성화한다() {
        // given
        Long contentId = 1L;
        boolean visibleComments = true;
        Content content = Content.builder()
                .id(contentId)
                .likes(0)
                .text("게시글 본문")
                .imageUrl(List.of(new ImageUrl("http://test.jikji/image", 1, 1L)))
                .userId(1L)
                .visibleComments(visibleComments)
                .hashtags(new HashSet<>())
                .build();
        contentCommandRepository.save(content);

        // when
        boolean result = contentCommandService.visibilityComments(contentId);

        // then
        assertThat(result).isEqualTo(!visibleComments);
    }

    @DisplayName("좋아요 숫자 감추기 요청시 좋아요 수를 가린다")
    @Test
    void 좋아요수를_가린다() {
        // given
        Long contentId = 1L;
        boolean visibleLikes = true;
        Content content = Content.builder()
                .id(contentId)
                .likes(0)
                .text("게시글 본문")
                .imageUrl(List.of(new ImageUrl("http://test.jikji/image", 1, 1L)))
                .userId(1L)
                .visibleLikes(visibleLikes)
                .hashtags(new HashSet<>())
                .build();
        contentCommandRepository.save(content);

        // when
        boolean result = contentCommandService.visibilityLikes(contentId);

        // then
        assertThat(result).isEqualTo(!visibleLikes);
    }
}