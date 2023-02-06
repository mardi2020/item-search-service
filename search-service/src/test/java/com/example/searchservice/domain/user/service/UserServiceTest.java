package com.example.searchservice.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.searchservice.domain.user.UserIndex;
import com.example.searchservice.domain.user.dto.response.UserResponse;
import com.example.searchservice.domain.user.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private UserIndex userIndex1;
    private UserIndex userIndex2;
    private UserIndex userIndex3;
    private UserIndex userIndex4;

    @BeforeEach
    void setUp() {
        userIndex1 = UserIndex.builder()
                .userId(1L)
                .name("이지금 IU")
                .loginId("dlwlrma")
                .followerCount(29_150_000L)
                .build();
        userIndex2 = UserIndex.builder()
                .userId(2L)
                .name("아이유 이지금 dlwlrma")
                .loginId("iu12345")
                .followerCount(1000L)
                .build();
        userIndex3 = UserIndex.builder()
                .userId(3L)
                .name("dlwlrma1580372521850")
                .loginId("dlwlrma12345")
                .followerCount(12230L)
                .build();
        userIndex4 = UserIndex.builder()
                .userId(4L)
                .name("dlwlrma1580372521850")
                .loginId("dlwlrmaxxxa1")
                .followerCount(11111L)
                .build();

        userRepository.saveAll(List.of(userIndex1, userIndex2, userIndex3, userIndex4));
    }


    @DisplayName("키워드가 포함된 사용자 이름, 사용자 아이디를 찾는다")
    @Test
    void findAllByKeyword() {
        // given
        Pageable pageable = PageRequest.of(0, 20,
                Sort.by("followerCount").descending());
        final String keyword = "dlwlrma";

        // when
        List<UserResponse> actual = userService.findAllByKeyword(keyword, pageable);

        // then
        assertThat(actual.size()).isEqualTo(4);
        assertThat(actual).contains(new UserResponse(userIndex1), new UserResponse(userIndex2),
                new UserResponse(userIndex3), new UserResponse(userIndex4));
    }
}