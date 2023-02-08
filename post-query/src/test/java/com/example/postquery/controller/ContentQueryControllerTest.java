package com.example.postquery.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.postquery.entity.Bookmark;
import com.example.postquery.entity.Content;
import com.example.postquery.entity.ContentLike;
import com.example.postquery.entity.Hashtag;
import com.example.postquery.entity.ImageUrl;
import com.example.postquery.repository.BookmarkQueryRepository;
import com.example.postquery.repository.ContentLikeQueryRepository;
import com.example.postquery.repository.ContentQueryRepository;
import com.example.postquery.repository.HashtagQueryRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
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

    @Autowired
    private HashtagQueryRepository hashtagQueryRepository;

    @Autowired
    private BookmarkQueryRepository bookmarkQueryRepository;

    @Autowired
    private ContentLikeQueryRepository contentLikeQueryRepository;

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

    @AfterEach
    void tearDown() {
        contentQueryRepository.deleteAll();
        hashtagQueryRepository.deleteAll();
        bookmarkQueryRepository.deleteAll();
        contentLikeQueryRepository.deleteAll();
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

    @DisplayName("해시태그 이름으로 id를 얻는다")
    @Test
    void findByHashtagId() {
        // given
        final String name = "아이유";
        final Long expected = 1L;
        String api = "http://localhost:" + port + "/hashtags?h=" + name;
        hashtagQueryRepository.save(new Hashtag(expected, name));

        // when
        ExtractableResponse<Response> response = get(api);
        Long actual = response.body().as(Long.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("사용자의 북마크 리스트를 조회한다")
    @Test
    void findBookmarkByUserId() {
        // given
        final Long userId = 1L;
        String api = "http://localhost:" + port + "/bookmarks/" + userId;
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
        ExtractableResponse<Response> response = get(api);
        Bookmark[] result = response.body().as(Bookmark[].class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.length).isEqualTo(3);
    }

    @DisplayName("사용자가 게시글을 좋아요했는지 확인한다")
    @Test
    void isLikeContent() {
        // given
        final Long userId = 1L;
        final Long contentId = 1L;
        String api = "http://localhost:" + port + "/likes/" + contentId + "/" + userId;
        contentLikeQueryRepository.save(new ContentLike(1L, userId, contentId));

        // when
        ExtractableResponse<Response> response = get(api);
        Boolean result = response.body().as(Boolean.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(result).isEqualTo(true);
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