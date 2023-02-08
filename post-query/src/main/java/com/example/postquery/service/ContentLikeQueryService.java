package com.example.postquery.service;

import com.example.postquery.repository.ContentLikeQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContentLikeQueryService {

    private final ContentLikeQueryRepository contentLikeQueryRepository;

    public Boolean isLikeContent(Long userId, Long contentId) {
        return contentLikeQueryRepository.existsByContentIdAndUserId(userId, contentId);
    }
}
