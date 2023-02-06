package com.example.searchservice.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.searchservice.domain.content.ContentIndex;
import com.example.searchservice.domain.content.repository.ContentRepository;
import com.example.searchservice.domain.user.UserIndex;
import com.example.searchservice.domain.user.repository.UserRepository;
import com.example.searchservice.dto.SearchResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class SearchControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("키워드 검색시 해시태그와 유저 통합 조회")
    @Test
    void findByKeyword() {
        // given
        ContentIndex contentIndex1 = ContentIndex.builder()
                .contentId(1L)
                .hashtags(List.of("aaa", "bbb", "dlwlrma", "이지금",  "아이유", "라일락"))
                .count(50000)
                .createdAt(1605571906149L)
                .build();
        ContentIndex contentIndex3 = ContentIndex.builder()
                .contentId(3L)
                .hashtags(List.of("아이유"))
                .count(10000)
                .createdAt(1675652691641L)
                .build();
        contentRepository.saveAll(List.of(contentIndex1, contentIndex3));

        UserIndex userIndex1 = UserIndex.builder()
                .userId(1L)
                .name("이지금 IU")
                .loginId("dlwlrma")
                .followerCount(29_150_000L)
                .build();
        UserIndex userIndex2 = UserIndex.builder()
                .userId(2L)
                .name("아이유 이지금")
                .loginId("iu12345")
                .followerCount(1000L)
                .build();
        userRepository.saveAll(List.of(userIndex1, userIndex2));

        final String keyword = "이지금";

        // when
        String api = "http://localhost:" + port + "?q=" + keyword;
        ExtractableResponse<Response> response = get(api);
        SearchResponse result = response.body().as(SearchResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getHashtagResponse().getName()).isEqualTo(keyword);
        assertThat(result.getUserResponse().size()).isEqualTo(2);
    }

    static ExtractableResponse<Response> get(final String api) {
        return RestAssured
                .given().log().all()
                .when().get(api)
                .then().log().all()
                .extract();
    }
}