package com.swyp.gaezzange.domain.routine.repository;

import jakarta.persistence.PrePersist;
import java.time.LocalDate;

public class RoutineListener {

  @PrePersist
  public void prePersist(Routine routine) {
    if (routine.getEndedDate() == null) {
      routine.setEndedDate(LocalDate.of(9999, 12, 31));
    }
  }

}