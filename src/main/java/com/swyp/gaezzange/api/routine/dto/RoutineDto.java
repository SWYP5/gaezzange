package com.swyp.gaezzange.api.routine.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.swyp.gaezzange.domain.category.RoutineCategory;
import com.swyp.gaezzange.domain.routine.repository.Routine;
import com.swyp.gaezzange.domain.tendency.UserTendency;
import jakarta.annotation.Nullable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RoutineDto {
  Long routineId;
  Long userId;
  UserTendency tendency;
  RoutineCategory category;
  String name;
  String emoji;
  Set<DayOfWeek> daysOfWeek;
  LocalDate startedDate;
  @Nullable
  LocalDate endedDate;
  @Nullable
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss", timezone = "Asia/Seoul")
  LocalTime executionTime;

  public static RoutineDto from(Routine routine) {
    return RoutineDto.builder()
        .routineId(routine.getRoutineId())
        .userId(routine.getUserId())
        .tendency(routine.getTendency())
        .category(routine.getCategory())
        .name(routine.getName())
        .emoji(routine.getEmoji())
        .daysOfWeek(routine.getDaysOfWeek())
        .startedDate(routine.getStartedDate())
        .endedDate(routine.getStartedDate())
        .executionTime(routine.getExecutionTime())
        .build();
  }
}
