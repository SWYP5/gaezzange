package com.swyp.gaezzange.api.routine.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class RoutineSearchCondition {
  @NotNull
  LocalDate targetDate;
}
