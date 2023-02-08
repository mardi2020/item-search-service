package com.example.postcommand.entity.converter;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

@Converter
@Slf4j
public class HashtagConverter implements AttributeConverter<Set<Long>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Set<Long> data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Set<Long> convertToEntityAttribute(String data) {
        try {
            Long[] result = objectMapper.readValue(data, Long[].class);
            if (result.length == 0) {
                return new HashSet<>();
            }
            return new HashSet<>(Arrays.asList(result));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
