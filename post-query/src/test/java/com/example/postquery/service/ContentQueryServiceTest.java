package com.example.postquery.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.example.postquery.entity.Content;
import com.example.postquery.entity.ImageUrl;
import com.example.postquery.repository.ContentQueryRepository;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@AutoConfigureDataMongo
@ActiveProfiles("test")
class ContentQueryServiceTest {

    @Autowired
    private ContentQueryService contentService;

    @Autowired
    private ContentQueryRepository contentRepository;

    @AfterEach
    void after() {
        contentRepository.deleteAll();
    }

    @BeforeEach
    void setUp() {
        Content content = Content.builder()
                .likes(1234)
                .contentId(1L)
                .text("텍스트 본문 내용 #aaa #aab")
                .imageUrl(List.of(new ImageUrl("http://test.jikji/before-image", 1, 1L)))
                .userId(1L)
                .hashtags(Set.of(1L, 2L))
                .build();

        contentRepository.save(content);
    }

    @DisplayName("게시글 id로 세부적인 조회를 할 수 있다")
    @Test
    void findByPostId() {
        // given
        final Long contentId = 1L;

        // when, then
        assertDoesNotThrow(() -> contentService.findByContentId(contentId));
    }

    @DisplayName("사용자의 id로 작성한 게시글을 모두 볼 수 있다")
    @Test
    void findByUserId() {
        // given
        final Long userId = 1L;

        // when
        List<Content> contents = contentService.findByUserId(userId);

        // then
        assertThat(contents.size()).isEqualTo(1);
    }

}