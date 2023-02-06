package com.example.searchservice.domain.user.dto.response;

import com.example.searchservice.domain.user.UserIndex;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class UserResponse {

    private Long userId;

    private String name;

    private String loginId;

    private Long followerCount;

    public UserResponse(UserIndex userIndex) {
        this.userId = userIndex.getUserId();
        this.name = userIndex.getName();
        this.loginId = userIndex.getLoginId();
        this.followerCount = userIndex.getFollowerCount();
    }
}
