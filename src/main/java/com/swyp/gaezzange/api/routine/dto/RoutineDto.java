package com.swyp.gaezzange.api.routine.dto;

import com.swyp.gaezzange.domain.category.RoutineCategory;
import com.swyp.gaezzange.domain.routine.repository.Routine;
import com.swyp.gaezzange.domain.tendency.Tendency;
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
  private Long routineId;
  private Long userId;
  private Tendency tendency;
  private RoutineCategory category;
  private String name;
  private Long emoji;
  private Set<DayOfWeek> daysOfWeek;
  private LocalDate startedDate;
  @Nullable
  private LocalDate endedDate;
  @Nullable
  private LocalTime executionTime;

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
