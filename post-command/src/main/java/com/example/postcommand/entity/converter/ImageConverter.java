package com.example.postcommand.entity.converter;

import com.example.postcommand.entity.ImageUrl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

@Converter
@Slf4j
public class ImageConverter implements AttributeConverter<List<ImageUrl>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<ImageUrl> urls) {
        try {
            return objectMapper.writeValueAsString(urls);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<ImageUrl> convertToEntityAttribute(String data) {
        try {
            return Arrays.asList(objectMapper.readValue(data, ImageUrl[].class));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}