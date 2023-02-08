package com.example.postcommand.util;


import com.example.postcommand.exception.BookmarkDuplicatedException;
import com.example.postcommand.exception.LikeDuplicatedException;
import com.example.postcommand.repository.BookmarkCommandRepository;
import com.example.postcommand.repository.LikeCommandRepository;

public final class ValidCheckUtil {

    public static void checkDuplicatedLike(Long userId, Long contentId,
                                           LikeCommandRepository likeCommandRepository) {
        if (likeCommandRepository.existsByUserIdAndContentId(userId, contentId)) {
            throw new LikeDuplicatedException();
        }
    }

    public static void checkDuplicatedBookmark(Long userId, Long contentId,
                                               BookmarkCommandRepository bookmarkCommandRepository) {
        if (bookmarkCommandRepository.existsByUserIdAndContentId(userId, contentId)) {
            throw new BookmarkDuplicatedException();
        }
    }
}
