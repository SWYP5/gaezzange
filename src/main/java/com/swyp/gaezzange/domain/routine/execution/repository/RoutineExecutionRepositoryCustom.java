package com.swyp.gaezzange.domain.routine.execution.repository;

import com.swyp.gaezzange.domain.routine.repository.Routine;
import com.swyp.gaezzange.domain.tendency.Tendency;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.tuple.Triple;

public interface RoutineExecutionRepositoryCustom {

  Map<Routine, List<RoutineExecution>> findExecutionsByRoutine(Long userId, LocalDate from, LocalDate to);
  Map<LocalDate, Triple<Long, Tendency, Long>> countExecutionsByDate(Long userId, LocalDate from, LocalDate to);
}