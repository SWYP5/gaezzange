package com.swyp.gaezzange.domain.routine.execution.service;

import com.swyp.gaezzange.domain.routine.execution.repository.RoutineExecution;
import com.swyp.gaezzange.domain.routine.execution.repository.RoutineExecutionRepository;
import com.swyp.gaezzange.domain.routine.repository.Routine;
import com.swyp.gaezzange.domain.tendency.Tendency;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoutineExecutionService {

  private final RoutineExecutionRepository repository;

  public Optional<RoutineExecution> getRoutine(Long routineId, LocalDate executedDate) {
    return repository.findByRoutineIdAndExecutedDate(routineId, executedDate);
  }

  public RoutineExecution save(RoutineExecution routineExecution) {
    return repository.save(routineExecution);
  }

  public void delete(RoutineExecution routineExecution) {
    repository.delete(routineExecution);
  }

  public Map<Routine, List<RoutineExecution>> getRoutineExecutionsByRoutine(
      Long userId, LocalDate from, LocalDate to
  ) {
    return repository.findExecutionsByRoutine(userId, from, to);
  }

  public Map<LocalDate, Triple<Long/* routineId */, Tendency, Long/* count */>> countRoutineExecutionByDate(
      Long userId, LocalDate from, LocalDate to
  ) {
    return repository.countExecutionsByDate(userId, from, to);
  }
}
