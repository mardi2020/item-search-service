package com.example.postquery.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.postquery.entity.Hashtag;
import com.example.postquery.repository.HashtagQueryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class HashtagQueryServiceTest {

    @Autowired
    private HashtagQueryService hashtagQueryService;

    @Autowired
    private HashtagQueryRepository hashtagQueryRepository;

    @AfterEach
    void tearDown() {
        hashtagQueryRepository.deleteAll();
    }

    @DisplayName("해시태그 이름으로 id 값을 조회한다")
    @Test
    void getHashtagIdByName() {
        // given
        final String name = "아이유";
        hashtagQueryRepository.save(new Hashtag(99L, name));

        // when
        Long hashtagId = hashtagQueryService.getHashtagIdByName(name);

        // then
        assertThat(hashtagId).isNotNull();
    }
}