package com.swyp.gaezzange.api.routine.dto;

import com.swyp.gaezzange.domain.category.RoutineCategory;
import com.swyp.gaezzange.domain.tendency.Tendency;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import lombok.Getter;

@Getter
public class RoutineForm {

  @NotNull
  private Tendency tendency;

  @NotNull
  private RoutineCategory category;

  @NotBlank
  private String name;

  @Nullable
  private Long emoji;

  @NotNull
  private LocalDate startedDate;

  @Nullable
  private LocalDate endedDate;

  @Nullable
  private LocalTime executionTime;

  @NotEmpty
  private Set<DayOfWeek> daysOfWeek;
}
