package com.example.postcommand.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.postcommand.entity.Bookmark;
import com.example.postcommand.entity.Content;
import com.example.postcommand.entity.ImageUrl;
import com.example.postcommand.repository.BookmarkCommandRepository;
import com.example.postcommand.repository.ContentCommandRepository;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.Set;
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
class BookmarkCommandControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private ContentCommandRepository contentRepository;


    private final static long userId = 1;
    private final static long contentId = 1;
    @Autowired
    private BookmarkCommandRepository bookmarkCommandRepository;

    @BeforeEach
    void setUp() {
        Content content = Content.builder()
                .id(contentId)
                .likes(0)
                .text("변경전 텍스트 #aaa #bb #ccc #dde")
                .hashtags(Set.of(1L, 2L, 3L, 4L))
                .imageUrl(List.of(new ImageUrl("http://test.jikji/before-image", 1, 1L)))
                .userId(userId)
                .visibleComments(false)
                .visibleLikes(true)
                .build();

        contentRepository.save(content);
    }

    @DisplayName("북마크를 추가한다")
    @Test
    void saveBookmark() {
        // when
        String api = "http://localhost:" + port + "/bookmarks/" + userId + "/save/" + contentId;
        ExtractableResponse<Response> response = post(api);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("북마크를 삭제한다")
    @Test
    void unsaveBookmark() {
        // given
        Bookmark bookmark = Bookmark.builder()
                .contentId(contentId)
                .userId(userId)
                .build();
        bookmarkCommandRepository.save(bookmark);

        // when
        String api = "http://localhost:" + port + "/bookmarks/" + userId + "/unsave/" + contentId;
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