package com.example.postcommand.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.postcommand.entity.Content;
import com.example.postcommand.entity.ContentLike;
import com.example.postcommand.entity.ImageUrl;
import com.example.postcommand.repository.ContentCommandRepository;
import com.example.postcommand.repository.LikeCommandRepository;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class LikeCommandControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private ContentCommandRepository contentCommandRepository;
    @Autowired
    private LikeCommandRepository likeCommandRepository;

    @BeforeEach
    void setUp() {
        Content content = Content.builder()
                .id(1L)
                .likes(0)
                .text("변경전 텍스트")
                .imageUrl(List.of(new ImageUrl("http://test.jikji/before-image", 1, 1L)))
                .userId(1L)
                .visibleComments(false)
                .visibleLikes(true)
                .hashtags(new HashSet<>())
                .build();

        contentCommandRepository.save(content);
    }

    @Test
    @DisplayName("좋아요를 추가한다")
    void like() {
        // given
        final long userId = 1;
        final long contentId = 1;

        // when
        String api = "http://localhost:" + port + "/" + userId + "/like/" + contentId;
        ExtractableResponse<Response> response = post(api);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("좋아요를 취소한다")
    void unlike() {
        // given
        final long userId = 1;
        final long contentId = 1;
        ContentLike contentLike = ContentLike.builder()
                .userId(userId)
                .contentId(contentId)
                .build();
        likeCommandRepository.save(contentLike);

        // when
        String api = "http://localhost:" + port + "/" + userId + "/unlike/" + contentId;
        ExtractableResponse<Response> response = post(api);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    static ExtractableResponse<Response> post(final String api) {
        return RestAssured
                .given().log().all()
                .when().post(api)
                .then().log().all()
                .extract();
    }
}