package com.example.postquery.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.postquery.entity.ContentLike;
import com.example.postquery.repository.ContentLikeQueryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class ContentLikeQueryServiceTest {

    @Autowired
    private ContentLikeQueryService contentLikeQueryService;

    @Autowired
    private ContentLikeQueryRepository contentLikeQueryRepository;

    @AfterEach
    void tearDown() {
        contentLikeQueryRepository.deleteAll();
    }

    @DisplayName("사용자가 해당 게시글을 좋아요 눌렀는지 조회한다")
    @Test
    void isLikeContent_liked() {
        // given
        final Long userId = 1L;
        final Long contentId = 1L;
        contentLikeQueryRepository.save(new ContentLike(1L, userId, contentId));

        // when
        Boolean likeContent = contentLikeQueryService.isLikeContent(userId, contentId);

        // then
        assertThat(likeContent).isEqualTo(true);
    }

    @DisplayName("사용자가 해당 게시글을 좋아요 눌렀는지 조회한다")
    @Test
    void isLikeContent_not_liked() {
        // given
        final Long userId = 1L;
        final Long contentId = 1L;

        // when
        Boolean likeContent = contentLikeQueryService.isLikeContent(userId, contentId);

        // then
        assertThat(likeContent).isEqualTo(false);
    }
}