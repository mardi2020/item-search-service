package com.example.postcommand.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("test")
class HashtagCommandServiceTest {

    @Autowired
    private HashtagCommandService hashtagCommandService;


    @DisplayName("새로운 해시태그를 저장한다")
    @Test
    void save() {
        // given
        final String name = "아이유";

        // when
        Long actual = hashtagCommandService.save(name);

        // then
        assertThat(actual).isEqualTo(1L);
    }

    @DisplayName("해시태그는 중복 저장되지 않는다")
    @Test
    void save_duplicated() {
        // given
        final String name = "아이유";
        hashtagCommandService.save(name);

        // when
        Long result = hashtagCommandService.save(name);

        // then
        assertThat(result).isEqualTo(-1L);
    }
}