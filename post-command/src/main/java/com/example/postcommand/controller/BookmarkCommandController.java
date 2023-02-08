package com.example.postcommand.controller;

import com.example.postcommand.service.BookmarkCommandService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/bookmarks")
public class BookmarkCommandController {

    private final BookmarkCommandService bookmarkCommandService;

    @PostMapping("{userId}/save/{contentId}")
    public ResponseEntity<?> saveBookmark(@PathVariable Long userId,
                                          @PathVariable Long contentId) {
        try {
            Long savedBookmarkId = bookmarkCommandService.saveBookmark(userId, contentId);
            return ResponseEntity.created(new URI(savedBookmarkId.toString())).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("{userId}/unsave/{contentId}")
    public ResponseEntity<?> unsaveBookmark(@PathVariable Long userId,
                                            @PathVariable Long contentId) {

        try {
            bookmarkCommandService.unsaveBookmark(userId, contentId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
