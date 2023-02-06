package com.example.searchservice.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.searchservice.domain.user.UserIndex;
import com.example.searchservice.domain.user.dto.response.UserResponse;
import com.example.searchservice.domain.user.repository.UserRepository;
import java.util.List;
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

    @Test
    void findAllByKeyword() {
        // given
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

        Pageable pageable = PageRequest.of(0, 20,
                Sort.by("followerCount").descending());
        final String keyword = "이지금";

        // when
        List<UserResponse> actual = userService.findAllByKeyword(keyword, pageable);

        // then
        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual).contains(new UserResponse(userIndex1), new UserResponse(userIndex2));
    }
}