package com.swyp.gaezzange.api.routine.execution.dto;

import com.swyp.gaezzange.api.routine.dto.RoutineDto;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RoutineExecutionResultDto {

  private RoutineDto routine;
  private List<LocalDate> executionDates;

  public String getRoutineName() {
    return this.routine.getName();
  }

  public LocalTime getRoutineExecutionTime() {
    return this.routine.getExecutionTime();
  }

  public Long getRoutineId() {
    return this.routine.getRoutineId();
  }
}
