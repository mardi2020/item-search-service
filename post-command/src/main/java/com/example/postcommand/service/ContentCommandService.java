package com.example.postcommand.service;


import com.example.postcommand.dto.request.ContentCreateRequest;
import com.example.postcommand.dto.request.ContentUpdateRequest;

public interface ContentCommandService {

    Long save(final ContentCreateRequest request);

    void update(final Long contentId, final ContentUpdateRequest request);

    void delete(final Long contentId);

    boolean visibilityLikes(Long contentId);

    boolean visibilityComments(Long contentId);
}
