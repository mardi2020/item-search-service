package com.example.searchservice.controller;

import com.example.searchservice.domain.content.dto.response.HashtagResponse;
import com.example.searchservice.domain.content.service.ContentService;
import com.example.searchservice.domain.user.dto.response.UserResponse;
import com.example.searchservice.domain.user.service.UserService;
import com.example.searchservice.dto.SearchResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SearchController {

    private final ContentService contentService;

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> findByKeyword(@RequestParam("q") String keyword,
                                           @PageableDefault(size=20,
                                                   sort = "followerCount", direction = Direction.DESC)
                                           Pageable pageable) {
        HashtagResponse byHashtag = contentService.findHashtagInfo(keyword);
        List<UserResponse> byKeyword = userService.findAllByKeyword(keyword, pageable);

        return ResponseEntity.ok(new SearchResponse(byHashtag, byKeyword));
    }
}
