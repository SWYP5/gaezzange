package com.swyp.gaezzange.domain.routine.execution.repository;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutineExecutionRepository extends JpaRepository<RoutineExecution, Long> {
  Optional<RoutineExecution> findByRoutineIdAndExecutedDate(Long routineId, LocalDate executedDate);
}