package com.example.userservice.dto;

import com.example.userservice.entity.JoinUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JoinUserDto {

    private Long userId;

    private String name;

    private String loginId;

    private String createdAt;

    public JoinUserDto(JoinUser joinUser) {
        this.name = joinUser.getName();
        this.loginId = joinUser.getLoginId();
        this.createdAt = joinUser.getCreatedAt();
    }
}
