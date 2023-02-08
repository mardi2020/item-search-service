package com.example.postquery.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.postquery.entity.Bookmark;
import com.example.postquery.repository.BookmarkQueryRepository;
import java.util.List;
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
class BookmarkQueryServiceTest {

    @Autowired
    private BookmarkQueryService bookmarkQueryService;

    @Autowired
    private BookmarkQueryRepository bookmarkQueryRepository;

    @AfterEach
    void tearDown() {
        bookmarkQueryRepository.deleteAll();
    }

    @DisplayName("사용자 id로 북마크 리스트를 조회한다")
    @Test
    void findBookmarkByUserId() {
        // given
        final Long userId = 1L;
        Bookmark bookmark1 = Bookmark.builder()
                .bookmarkId(1L)
                .userId(userId)
                .contentId(1L)
                .commentCount(5)
                .likesCount(222)
                .build();
        Bookmark bookmark2 = Bookmark.builder()
                .bookmarkId(2L)
                .userId(userId)
                .contentId(2L)
                .commentCount(9)
                .likesCount(44)
                .build();

        Bookmark bookmark3 = Bookmark.builder()
                .bookmarkId(3L)
                .userId(userId)
                .contentId(3L)
                .commentCount(11111)
                .likesCount(1613)
                .build();
        bookmarkQueryRepository.saveAll(List.of(bookmark1, bookmark2, bookmark3));

        // when
        List<Bookmark> bookmark = bookmarkQueryService.findBookmarkByUserId(userId);

        // then
        assertThat(bookmark.size()).isEqualTo(3);
    }
}