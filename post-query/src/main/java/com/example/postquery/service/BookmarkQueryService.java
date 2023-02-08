package com.example.postquery.service;

import com.example.postquery.entity.Bookmark;
import com.example.postquery.repository.BookmarkQueryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkQueryService {

    private final BookmarkQueryRepository bookmarkQueryRepository;

    public List<Bookmark> findBookmarkByUserId(Long userId) {
        return bookmarkQueryRepository.findAllByUserId(userId);
    }
}
