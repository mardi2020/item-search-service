package com.example.postquery.controller;

import com.example.postquery.entity.Content;
import com.example.postquery.service.ContentQueryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ContentQueryController {

    private final ContentQueryService contentQueryService;

    @GetMapping("/contents")
    public ResponseEntity<?> findByContentId(@RequestParam(name = "c") Long contentId) {
        try {
            Content content = contentQueryService.findByContentId(contentId);
            return ResponseEntity.ok(content);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<Content>> findByUserId(@RequestParam(name = "u") Long userId) {
        try {
            List<Content> contents = contentQueryService.findByUserId(userId);
            return ResponseEntity.ok(contents);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
