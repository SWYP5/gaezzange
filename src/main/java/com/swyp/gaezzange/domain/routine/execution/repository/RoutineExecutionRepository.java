package com.swyp.gaezzange.domain.routine.execution.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutineExecutionRepository extends JpaRepository<RoutineExecution, Long>, RoutineExecutionRepositoryCustom {

  Optional<RoutineExecution> findByRoutineIdAndExecutedDate(Long routineId, LocalDate executedDate);
  List<RoutineExecution> findAllByRoutineId(Long routineId);

  @Modifying
  @Query("delete from RoutineExecution c where c.routineExecutionId in :ids")
  void deleteAllByIdInQuery(@Param("ids") List<Long> ids);
}