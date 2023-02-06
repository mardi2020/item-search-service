package com.example.searchservice.domain.content.controller;


import com.example.searchservice.domain.content.dto.response.ContentResponse;
import com.example.searchservice.domain.content.service.ContentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/contents")
public class ContentController {

    private final ContentService contentService;

    @GetMapping
    public ResponseEntity<List<ContentResponse>> findByHashtag(@RequestParam(value = "q") String hashtag,
                                                         @PageableDefault(size=20)
                                           @SortDefault.SortDefaults({
                                                   @SortDefault(sort = "createdAt", direction = Direction.DESC),
                                                   @SortDefault(sort = "count", direction = Direction.DESC)
                                           }) Pageable pageable) {
        List<ContentResponse> result = contentService.findByHashtag(hashtag, pageable);
        return ResponseEntity.ok(result);
    }
}
