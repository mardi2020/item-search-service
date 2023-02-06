package com.example.searchservice.dto;

import com.example.searchservice.domain.content.dto.response.HashtagResponse;
import com.example.searchservice.domain.user.dto.response.UserResponse;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SearchResponse {

    private HashtagResponse hashtagResponse;

    private List<UserResponse> userResponse;

    public SearchResponse(HashtagResponse h, List<UserResponse> u) {
        hashtagResponse = h;
        userResponse = u;
    }
}
