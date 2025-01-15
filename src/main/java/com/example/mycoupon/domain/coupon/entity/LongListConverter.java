package com.example.mycoupon.domain.coupon.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class LongListConverter implements AttributeConverter<List<Long>, String> {

    private static final String DELIMITER = ",";

    // List<Long> -> String 변환
    @Override
    public String convertToDatabaseColumn(List<Long> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return null;
        }
        return attribute.stream()
                .map(String::valueOf) // Long -> String
                .collect(Collectors.joining(DELIMITER));
    }

    // String -> List<Long> 변환
    @Override
    public List<Long> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        return Arrays.stream(dbData.split(DELIMITER))
                .map(Long::valueOf) // String -> Long
                .collect(Collectors.toList());
    }
}
