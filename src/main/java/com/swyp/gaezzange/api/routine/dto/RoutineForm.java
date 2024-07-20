package com.swyp.gaezzange.api.routine.dto;

import com.swyp.gaezzange.domain.category.RoutineCategory;
import com.swyp.gaezzange.domain.tendency.UserTendency;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import lombok.Getter;

@Getter
public class RoutineForm {

  @NotNull
  UserTendency tendency;

  @NotNull
  RoutineCategory category;

  @NotBlank
  String name;

  @Size(max = 1)
  String emoji;

  @NotNull
  LocalDate startedDate;

  @Nullable
  LocalDate endedDate;

  @Nullable
  LocalTime executionTime;

  @NotEmpty
  Set<DayOfWeek> daysOfWeek;
}
