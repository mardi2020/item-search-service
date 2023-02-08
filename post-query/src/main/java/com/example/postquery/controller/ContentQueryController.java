package com.example.postquery.controller;

import com.example.postquery.entity.Bookmark;
import com.example.postquery.entity.Content;
import com.example.postquery.exception.ContentNotFoundException;
import com.example.postquery.exception.HashtagNotFoundException;
import com.example.postquery.exception.UserNotFoundException;
import com.example.postquery.service.BookmarkQueryService;
import com.example.postquery.service.ContentLikeQueryService;
import com.example.postquery.service.ContentQueryService;
import com.example.postquery.service.HashtagQueryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ContentQueryController {

    private final ContentQueryService contentQueryService;

    private final HashtagQueryService hashtagQueryService;

    private final BookmarkQueryService bookmarkQueryService;

    private final ContentLikeQueryService contentLikeQueryService;

    @GetMapping("/contents")
    public ResponseEntity<?> findByContentId(@RequestParam(name = "c") Long contentId) {
        try {
            Content content = contentQueryService.findByContentId(contentId);
            return ResponseEntity.ok(content);
        } catch (ContentNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<Content>> findByUserId(@RequestParam(name = "u") Long userId) {
        try {
            List<Content> contents = contentQueryService.findByUserId(userId);
            return ResponseEntity.ok(contents);
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/hashtags")
    public ResponseEntity<?> findByHashtagId(@RequestParam(name = "h") String name) {
        try {
            Long id = hashtagQueryService.getHashtagIdByName(name);
            return ResponseEntity.ok(id);
        } catch (HashtagNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/bookmarks/{userId}")
    public ResponseEntity<List<Bookmark>> findBookmarkByUserId(@PathVariable Long userId) {
        List<Bookmark> result = bookmarkQueryService.findBookmarkByUserId(userId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/likes/{contentId}/{userId}")
    public ResponseEntity<Boolean> isLikeContent(@PathVariable Long userId,
                                           @PathVariable Long contentId) {
        Boolean likeContent = contentLikeQueryService.isLikeContent(userId, contentId);
        return ResponseEntity.ok(likeContent);
    }
}
