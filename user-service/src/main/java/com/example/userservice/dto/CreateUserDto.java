package com.example.userservice.dto;

import com.example.userservice.entity.JoinUser;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateUserDto {

    private String name;

    private String loginId;

    public JoinUser toEntity() {
        return new JoinUser(name, loginId);
    }
}
