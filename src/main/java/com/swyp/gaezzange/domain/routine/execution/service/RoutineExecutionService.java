package com.swyp.gaezzange.domain.routine.execution.service;

import com.swyp.gaezzange.domain.routine.execution.repository.RoutineExecution;
import com.swyp.gaezzange.domain.routine.execution.repository.RoutineExecutionRepository;
import com.swyp.gaezzange.domain.routine.repository.Routine;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
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

  public List<RoutineExecution> listRoutineExecution(Long routineId) {
    return repository.findAllByRoutineId(routineId);
  }

  @Transactional
  public RoutineExecution save(RoutineExecution routineExecution) {
    return repository.save(routineExecution);
  }

  @Transactional
  public void delete(RoutineExecution routineExecution) {
    repository.delete(routineExecution);
  }

  @Transactional
  public void deleteRoutineExecutions(List<RoutineExecution> routineExecutions) {
    List<Long> ids = routineExecutions.stream().map(RoutineExecution::getRoutineExecutionId)
            .toList();
    repository.deleteAllByIdInQuery(ids);
  }

  public Map<Routine, List<RoutineExecution>> getRoutineExecutionsByRoutine(
      Long userId, LocalDate from, LocalDate to
  ) {
    return repository.findExecutionsByRoutine(userId, from, to);
  }
}
