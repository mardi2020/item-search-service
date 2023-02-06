package com.example.searchservice.domain.content;


import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Builder @AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = "contents")
public class ContentIndex {

    @Id
    @Field(type = FieldType.Long)
    private Long contentId;

    @Field(type = FieldType.Auto)
    private List<String> hashtags;

    @Field(type = FieldType.Integer)
    private int count;

    @Field(type = FieldType.Long)
    private Long createdAt;
}
