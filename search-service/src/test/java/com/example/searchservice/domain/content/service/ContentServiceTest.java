package com.example.searchservice.domain.content.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.searchservice.domain.content.ContentIndex;
import com.example.searchservice.domain.content.dto.response.ContentResponse;
import com.example.searchservice.domain.content.dto.response.HashtagResponse;
import com.example.searchservice.domain.content.repository.ContentRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


@SpringBootTest
//@ActiveProfiles("test")
class ContentServiceTest {

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private ContentService contentService;

    private ContentIndex contentIndex1;
    private ContentIndex contentIndex2;
    private ContentIndex contentIndex3;

    @BeforeEach
    void setUp() {
        contentIndex1 = ContentIndex.builder()
                .contentId(1L)
                .hashtags(List.of("aaa", "bbb", "ccc", "ddd", "ee",  "아이유", "라일락"))
                .count(50000)
                .createdAt(1605571906149L)
                .build();

        contentIndex2 = ContentIndex.builder()
                .contentId(2L)
                .hashtags(List.of("아이유라일락"))
                .count(10)
                .createdAt(1675652708358L)
                .build();

        contentIndex3 = ContentIndex.builder()
                .contentId(3L)
                .hashtags(List.of("아이유"))
                .count(10000)
                .createdAt(1675652691641L)
                .build();

        List<ContentIndex> data = List.of(contentIndex1, contentIndex3, contentIndex2);
        contentRepository.saveAll(data);
    }

    @DisplayName("해시태그가 포함된 게시글을 조회한다")
    @Test
    void findByHashtag() {
        // given
        final String hashtag = "아이유";
        Sort sort = Sort.by("createdAt").descending()
                .and(Sort.by("count").descending());
        Pageable pageable = PageRequest.of(0, 10, sort);

        // when
        List<ContentResponse> result = contentService.findByHashtag(hashtag, pageable);

        // then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0)).usingRecursiveComparison()
                .ignoringActualNullFields()
                .ignoringExpectedNullFields()
                .isEqualTo(new ContentResponse(contentIndex3));
        assertThat(result.get(1)).usingRecursiveComparison()
                .ignoringActualNullFields()
                .ignoringExpectedNullFields()
                .isEqualTo(new ContentResponse(contentIndex1));
    }

    @DisplayName("해시태그 개수를 조회한다")
    @Test
    void countByHashtag() {
        // given
        final String keyword = "아이유";

        // when
        HashtagResponse actual = contentService.findHashtagInfo(keyword);

        // then
        assertThat(actual.getName()).isEqualTo(keyword);
        assertThat(actual.getCount()).isEqualTo(2);
    }
}