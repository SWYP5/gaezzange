package com.swyp.gaezzange.util.jpa;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Converter
public class DaysOfWeekConverter implements AttributeConverter<Set<DayOfWeek>, String> {

  @Override
  public String convertToDatabaseColumn(Set<DayOfWeek> attribute) {
    if (attribute == null || attribute.isEmpty()) {
      return "";
    }
    return attribute.stream()
        .map(Enum::name)
        .collect(Collectors.joining(","));
  }

  @Override
  public Set<DayOfWeek> convertToEntityAttribute(String dbData) {
    if (dbData == null || dbData.isEmpty()) {
      return new HashSet<>();
    }

    return Arrays.stream(dbData.split(","))
        .map(DayOfWeek::valueOf)
        .collect(Collectors.toSet());
  }
}