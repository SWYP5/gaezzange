package com.swyp.gaezzange.domain.routine.execution.dto;

import com.swyp.gaezzange.domain.tendency.Tendency;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoutineExecutionCount {

  private int baezzangeCount;
  private int gaemiCount;

  public void plusByTendency(Tendency tendency) {
    if (Tendency.BAEZZANGE == tendency) {
      baezzangeCount++;
    } else {
      gaemiCount++;
    }
  }
}