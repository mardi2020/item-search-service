package com.example.searchservice.domain.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

@Getter
@Builder @AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = "users")
@Mapping(mappingPath = "/static/elastic/user-mapping.json")
@Setting(settingPath = "/static/elastic/user-setting.json")
public class UserIndex {

    @Id
    @Field(type = FieldType.Long)
    private Long userId;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Text)
    private String loginId;

    @Field(type = FieldType.Long)
    private Long followerCount;

}
