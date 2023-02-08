package com.example.postcommand.controller;

import com.example.postcommand.service.LikeCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LikeCommandController {

    private final LikeCommandService likeCommandService;

    @PostMapping("{userId}/like/{contentId}")
    public ResponseEntity<?> likeContent(@PathVariable Long userId, @PathVariable Long contentId) {
        try {
            long count = likeCommandService.likeContent(userId, contentId);
            return new ResponseEntity<>(count, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("{userId}/unlike/{contentId}")
    public ResponseEntity<?> unlikeContent(@PathVariable Long userId, @PathVariable Long contentId) {
        try {
            int count = likeCommandService.unlikeContent(userId, contentId);
            return new ResponseEntity<>(count, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
