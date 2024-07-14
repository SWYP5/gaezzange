package com.swyp.gaezzange.domain.routine.execution.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutineExecutionRepository extends JpaRepository<RoutineExecution, String> {

}