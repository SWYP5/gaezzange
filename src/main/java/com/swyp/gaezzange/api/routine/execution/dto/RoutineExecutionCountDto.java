package com.swyp.gaezzange.api.routine.execution.dto;

import com.swyp.gaezzange.domain.tendency.Tendency;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
public class RoutineExecutionCountDto {

  LocalDate date;
  List<RoutineExecutionTendencyCount> count;

  @Setter
  public static class RoutineExecutionTendencyCount {

    Tendency tendency;
    Long count;
  }
}
