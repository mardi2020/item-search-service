package com.example.postquery.service;


import com.example.postquery.entity.Content;
import java.util.List;

public interface ContentQueryService {

    Content findByContentId(Long contentId);

    List<Content> findByUserId(Long userId);
}
