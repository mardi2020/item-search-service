package com.example.postcommand.controller;

import com.example.postcommand.service.HashtagCommandService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/hashtags")
public class HashtagCommandController {

    private final HashtagCommandService hashtagCommandService;

    @PostMapping("/{name}")
    public ResponseEntity<?> save(@PathVariable String name) {
        Long result = hashtagCommandService.save(name);
        if (result == -1L) {
            return new ResponseEntity<>("중복된 해시태그입니다.", HttpStatus.CONFLICT);
        }
        return ResponseEntity.created(URI.create("/hashtags" + result)).build();
    }
}
