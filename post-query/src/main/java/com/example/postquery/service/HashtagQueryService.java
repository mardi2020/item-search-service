package com.example.postquery.service;

import com.example.postquery.entity.Hashtag;
import com.example.postquery.exception.HashtagNotFoundException;
import com.example.postquery.repository.HashtagQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HashtagQueryService {

    private final HashtagQueryRepository hashtagQueryRepository;

    public Long getHashtagIdByName(String name) {
        Hashtag hashtag = hashtagQueryRepository.findByName(name)
                .orElseThrow(HashtagNotFoundException::new);

        return hashtag.getHashtagId();
    }
}
