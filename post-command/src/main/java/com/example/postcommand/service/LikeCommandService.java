package com.example.postcommand.service;

public interface LikeCommandService {

    int likeContent(Long userId, Long contentId);

    int unlikeContent(Long userId, Long contentId);
}
