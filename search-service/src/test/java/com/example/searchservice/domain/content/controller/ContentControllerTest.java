package com.example.searchservice.domain.content.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.searchservice.domain.content.ContentIndex;
import com.example.searchservice.domain.content.dto.response.ContentResponse;
import com.example.searchservice.domain.content.repository.ContentRepository;
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
class ContentControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private ContentRepository contentRepository;

    @DisplayName("해시태그가 포함된 게시글을 20개씩 조회한다")
    @Test
    void findByHashtag() {
        // given
        ContentIndex contentIndex1 = ContentIndex.builder()
                .contentId(1L)
                .hashtags(List.of("aaa", "bbb", "ccc", "ddd", "ee",  "아이유", "라일락"))
                .count(50000)
                .createdAt(1605571906149L)
                .build();

        ContentIndex contentIndex2 = ContentIndex.builder()
                .contentId(2L)
                .hashtags(List.of("라일락"))
                .count(10)
                .createdAt(1675652708358L)
                .build();

        ContentIndex contentIndex3 = ContentIndex.builder()
                .contentId(3L)
                .hashtags(List.of("아이유"))
                .count(10000)
                .createdAt(1675652691641L)
                .build();

        List<ContentIndex> data = List.of(contentIndex1, contentIndex3, contentIndex2);
        contentRepository.saveAll(data);

        final String hashtag = "아이유";

        // when
        String api = "http://localhost:" + port + "/contents?q=" + hashtag;
        ExtractableResponse<Response> response = get(api);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().as(ContentResponse[].class).length).isEqualTo(2);
    }

    static ExtractableResponse<Response> get(final String api) {
        return RestAssured
                .given().log().all()
                .when().get(api)
                .then().log().all()
                .extract();
    }
}