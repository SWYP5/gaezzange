package com.swyp.gaezzange.domain.routine.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {

  List<Routine> findAllByUserIdAndStartedDateLessThanEqualAndEndedDateGreaterThanEqualAndDeletedIsFalse(
      Long userId, LocalDate endDate, LocalDate startedDate);
}