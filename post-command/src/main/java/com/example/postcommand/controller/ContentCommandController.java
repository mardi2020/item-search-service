package com.example.postcommand.controller;


import com.example.postcommand.dto.request.ContentCreateRequest;
import com.example.postcommand.dto.request.ContentUpdateRequest;
import com.example.postcommand.exception.ContentNotFoundException;
import com.example.postcommand.service.ContentCommandService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/contents")
public class ContentCommandController {

    private final ContentCommandService contentCommandService;

    @PostMapping
    public ResponseEntity<Void> createContent(@RequestBody ContentCreateRequest request) {
        Long savedId = contentCommandService.save(request);
        return ResponseEntity.created(URI.create("/contents" + savedId)).build();
    }

    @PatchMapping("/{contentId}")
    public ResponseEntity<?> updateContent(@RequestBody ContentUpdateRequest request,
                                           @PathVariable Long contentId) {
        try {
            contentCommandService.update(contentId, request);
            return ResponseEntity.ok(contentId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{contentId}")
    public ResponseEntity<?> deleteContent(@PathVariable Long contentId) {
        contentCommandService.delete(contentId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/visibility/likes/{contentId}")
    public ResponseEntity<?> visibilityLikes(@PathVariable Long contentId) {
        try {
            boolean result = contentCommandService.visibilityLikes(contentId);
            return ResponseEntity.ok(result);
        } catch (ContentNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/visibility/comments/{contentId}")
    public ResponseEntity<?> visibilityComments(@PathVariable Long contentId) {
        try {
            boolean result = contentCommandService.visibilityComments(contentId);
            return ResponseEntity.ok(result);
        } catch (ContentNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
