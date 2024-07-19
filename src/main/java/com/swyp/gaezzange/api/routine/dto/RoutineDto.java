package com.swyp.gaezzange.api.routine.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
  String name;
  String emoji;
  Set<DayOfWeek> daysOfWeek;
  LocalDate startedDate;
  @Nullable
  LocalDate endedDate;
  @Nullable
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss", timezone = "Asia/Seoul")
  LocalTime executionTime;
}
