package com.swyp.gaezzange.domain.routine.execution.repository;

import com.swyp.gaezzange.domain.routine.repository.Routine;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface RoutineExecutionRepositoryCustom {

  Map<Routine, List<RoutineExecution>> findExecutionsByRoutine(Long userId, LocalDate from, LocalDate to);
}