package com.example.postquery.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.postquery.entity.Content;
import com.example.postquery.entity.ImageUrl;
import com.example.postquery.repository.ContentQueryRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureDataMongo
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class ContentQueryControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private ContentQueryRepository contentQueryRepository;

    @BeforeEach
    void setUp() {
        Content content = Content.builder()
                .likes(1234)
                .contentId(1L)
                .text("텍스트 본문 내용 #aaa #aab")
                .imageUrl(List.of(new ImageUrl("http://test.jikji/image", 1, 1L)))
                .userId(1L)
                .hashtags(Set.of(1L, 2L))
                .createdAt("2023-01-01")
                .modifiedAt("2023-01-01")
                .build();
        contentQueryRepository.save(content);
    }

    @DisplayName("게시글 id로 세부 조회할 수 있다")
    @Test
    void findByContentId() {
        // given
        final long contentId = 1L;
        Content expected = Content.builder()
                .likes(1234)
                .contentId(1L)
                .text("텍스트 본문 내용 #aaa #aab")
                .imageUrl(List.of(new ImageUrl("http://test.jikji/image", 1, 1L)))
                .userId(1L)
                .hashtags(Set.of(1L, 2L))
                .createdAt("2023-01-01")
                .modifiedAt("2023-01-01")
                .build();

        // when
        String api = "http://localhost:" + port + "/contents?c=" + contentId;
        ExtractableResponse<Response> response = get(api);
        Content actual = response.body().as(Content.class);


        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(actual).usingRecursiveComparison()
                .ignoringActualNullFields()
                .ignoringExpectedNullFields()
                .isEqualTo(expected);
    }

    @DisplayName("사용자 id로 작성한 모든 게시글을 조회할 수 있다")
    @Test
    void findByUserId() {
        // given
        final long userId = 1L;
        List<Content> expected = List.of(
                Content.builder()
                        .likes(1234)
                        .contentId(1L)
                        .text("텍스트 본문 내용 #aaa #aab")
                        .imageUrl(List.of(new ImageUrl("http://test.jikji/image", 1, 1L)))
                        .userId(1L)
                        .hashtags(Set.of(1L, 2L))
                        .createdAt("2023-01-01")
                        .modifiedAt("2023-01-01")
                        .build()
        );

        // when
        String api = "http://localhost:" + port + "/users?u=" + userId;
        ExtractableResponse<Response> response = get(api);
        List<Content> actual = List.of(response.body().as(Content[].class));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(actual).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .ignoringActualNullFields()
                .isEqualTo(expected);
    }
    static ExtractableResponse<Response> get(final String api) {
        return RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .when().get(api)
                .then().log().all()
                .extract();
    }

}