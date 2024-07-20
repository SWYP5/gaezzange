package com.swyp.gaezzange.domain.routine.execution.service;

import com.swyp.gaezzange.domain.routine.execution.repository.RoutineExecution;
import com.swyp.gaezzange.domain.routine.execution.repository.RoutineExecutionRepository;
import java.time.LocalDate;
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

  public RoutineExecution save(RoutineExecution routineExecution) {
    return repository.save(routineExecution);
  }

  public void delete(RoutineExecution routineExecution) {
    repository.delete(routineExecution);
  }
}
