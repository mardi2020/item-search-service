package com.example.postcommand.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageUrl {

    private static final int MAX_IMAGE_URL_LENGTH = 20480;

    private String url;

    private int order;

    private Long userId;

    @Builder
    public ImageUrl(String url, int order, Long userId) {
        this.url = url;
        this.order = order;
        this.userId = userId;
    }
}