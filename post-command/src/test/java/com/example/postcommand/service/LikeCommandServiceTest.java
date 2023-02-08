package com.example.postcommand.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.postcommand.entity.Content;
import com.example.postcommand.entity.ContentLike;
import com.example.postcommand.entity.ImageUrl;
import com.example.postcommand.exception.ContentNotFoundException;
import com.example.postcommand.exception.LikeDuplicatedException;
import com.example.postcommand.repository.ContentCommandRepository;
import com.example.postcommand.repository.LikeCommandRepository;
import com.example.postcommand.util.ValidCheckUtil;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class LikeCommandServiceTest {

    @Autowired
    private LikeCommandService likeCommandServiceImpl;

    @Autowired
    private LikeCommandRepository likeCommandRepository;

    @Autowired
    private ContentCommandRepository contentCommandRepository;


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
                .build();

        contentCommandRepository.save(content);
    }

    @Test
    @DisplayName("게시글을 좋아요하면 좋아요 개수가 증가한다")
    void 게시글_좋아요_개수가_증가한다() {
        // given
        final Long contentId = 1L;
        final Long userId = 1L;

        // when
        int count = likeCommandServiceImpl.likeContent(userId, contentId);
       Content content = contentCommandRepository.findById(contentId)
               .orElseThrow(ContentNotFoundException::new);
        // then
        assertThat(count).isEqualTo(1);
        assertThat(count).isEqualTo(content.getLikes());
    }

    @Test
    @DisplayName("게시글의 좋아요는 중복될 수 없다")
    void 게시글의_좋아요는_중복될_수_없다() {
        // given
        final Long userId = 1L;
        final Long contentId = 1L;
        ContentLike contentLike = ContentLike.builder()
                .contentId(contentId)
                .userId(userId)
                .build();
        likeCommandRepository.save(contentLike);

        // when, then
        assertThrows(LikeDuplicatedException.class,
                () -> ValidCheckUtil.checkDuplicatedLike(userId, contentId, likeCommandRepository));

    }

    @Test
    @DisplayName("게시글의 좋아요를 취소하면 좋아요 개수가 줄어든다")
    void 좋아요_취소하면_좋아요_개수가_줄어든다() {
        // given
        final Long userId = 1L;
        final Long contentId = 1L;
        ContentLike contentLike = ContentLike.builder()
                .contentId(contentId)
                .userId(userId)
                .build();
        likeCommandRepository.save(contentLike);
        Content content = contentCommandRepository.findById(contentId)
                .orElseThrow(ContentNotFoundException::new);
        content.increaseLikes();
        contentCommandRepository.save(content);

        // when
        int count = likeCommandServiceImpl.unlikeContent(userId, contentId);
        content = contentCommandRepository.findById(contentId)
                .orElseThrow(ContentNotFoundException::new);

        // then
        assertThat(count).isEqualTo(0);
        assertThat(count).isEqualTo(content.getLikes());
    }
}