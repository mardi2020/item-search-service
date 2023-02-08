package com.example.postcommand.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.postcommand.dto.request.ContentCreateRequest;
import com.example.postcommand.dto.request.ContentUpdateRequest;
import com.example.postcommand.entity.ImageUrl;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class ContentCommandControllerTest {

    @LocalServerPort
    int port;

    @Test
    @DisplayName("content를 저장한다")
    void save() {
        // given
        Long userId = 1L;
        List<ImageUrl> imageUrl = List.of(new ImageUrl("https://mockup.url", 1, userId));
        ContentCreateRequest contentCreateRequest = ContentCreateRequest.builder()
                .userId(userId)
                .visibleComments(true)
                .visibleLikes(true)
                .imageUrl(imageUrl)
                .text("변경전 텍스트 #aaa #bb #ccc #dde")
                .hashtags(Set.of(1L, 2L, 3L, 4L))
                .build();

        // when
        ExtractableResponse<Response> response = saveContent("http://localhost:"+ port + "/contents", contentCreateRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("content를 수정한다")
    void update() {
        // given
        final long contentId = 1L;
        final Long userId = 1L;

        ContentCreateRequest createRequest = ContentCreateRequest.builder()
                .userId(userId)
                .visibleComments(true)
                .visibleLikes(true)
                .imageUrl(List.of(new ImageUrl("https://before.url", 1, userId)))
                .text("변경전 텍스트 #aaa #bb #ccc #dde")
                .hashtags(Set.of(1L, 2L, 3L, 4L))
                .build();

        ContentUpdateRequest updateRequest = ContentUpdateRequest.builder()
                .text("변경된 텍스트 #aaa")
                .hashtags(Set.of(1L))
                .visibleLikes(false)
                .visibleComments(false)
                .imageUrl(List.of(new ImageUrl("https://after.url", 1, userId)))
                .userId(userId)
                .build();
        // when
        final String api = "http://localhost:" + port + "/contents";
        saveContent(api, createRequest);
        ExtractableResponse<Response> response = updateContent(api + "/" + contentId, updateRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("content를 삭제한다")
    void delete() {
        // given
        final long contentId = 1L;
        final Long userId = 1L;

        ContentCreateRequest createRequest = ContentCreateRequest.builder()
                .userId(userId)
                .visibleComments(true)
                .visibleLikes(true)
                .imageUrl(List.of(new ImageUrl("https://before.url", 1, userId)))
                .text("description")
                .hashtags(new HashSet<>())
                .build();

        // when
        final String api = "http://localhost:" + port + "/contents";
        saveContent(api, createRequest);
        ExtractableResponse<Response> response = deleteContent(api + "/" + contentId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("좋아요 수를 가린다")
    void hide_likes() {
        // given
        final long contentId = 1;
        ContentCreateRequest createRequest = ContentCreateRequest.builder()
                .userId(1L)
                .visibleComments(true)
                .visibleLikes(true)
                .imageUrl(List.of(new ImageUrl("https://before.url", 1, 1L)))
                .text("description")
                .hashtags(new HashSet<>())
                .build();
        final String api = "http://localhost:" + port + "/contents";
        saveContent(api, createRequest);

        // when
        ExtractableResponse<Response> response = saveVisibility(api + "/visibility/likes/" + contentId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("댓글을 숨긴다")
    void hide_comments() {
        // given
        final long contentId = 1;
        ContentCreateRequest createRequest = ContentCreateRequest.builder()
                .userId(1L)
                .visibleComments(true)
                .visibleLikes(true)
                .imageUrl(List.of(new ImageUrl("https://before.url", 1, 1L)))
                .text("description")
                .hashtags(new HashSet<>())
                .build();
        final String api = "http://localhost:" + port + "/contents";
        saveContent(api, createRequest);

        // when
        ExtractableResponse<Response> response = saveVisibility(api + "/visibility/comments/" + contentId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    static ExtractableResponse<Response> saveContent(final String api, final ContentCreateRequest request) {
        return RestAssured
                .given().log().all()
                .accept("application/json")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post(api)
                .then().log().all()
                .extract();
    }

    static ExtractableResponse<Response> saveVisibility(final String api) {
        return RestAssured
                .given().log().all()
                .when().post(api)
                .then().log().all()
                .extract();
    }

    static ExtractableResponse<Response> updateContent(final String api, final ContentUpdateRequest request) {
        return RestAssured
                .given().log().all()
                .accept("application/json")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().patch(api)
                .then().log().all()
                .extract();
    }

    static ExtractableResponse<Response> deleteContent(final String api) {
        return RestAssured
                .given().log().all()
                .when().delete(api)
                .then().log().all()
                .extract();
    }
}